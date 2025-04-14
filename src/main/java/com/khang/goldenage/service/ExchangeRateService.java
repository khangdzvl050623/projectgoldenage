package com.khang.goldenage.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.khang.goldenage.modal.ExchangeRate;
import com.khang.goldenage.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class ExchangeRateService {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    // Phương thức chính để phân tích giá từ URL
    public List<ExchangeRate> parseExchangeRateFromUrl(String urlString) throws IOException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {
            // Lấy dữ liệu từ URL
            String content = fetchPriceFromUrl(urlString);

            if (content == null || content.trim().isEmpty()) {
                throw new IOException("Không nhận được dữ liệu từ API.");
            }

            if (content.trim().startsWith("<?xml")) {
                // Nếu dữ liệu là XML
                exchangeRates = parseXmlExchangeRate(content);
            } else if (content.trim().startsWith("{") || content.trim().startsWith("[")) {
                // Nếu dữ liệu là JSON
                exchangeRates = parseJsonExchangerate(content);
            } else {
                throw new IOException("Dữ liệu không phải là XML hay JSON.");
            }
        } catch (Exception e) {
            logger.error("Lỗi trong khi phân tích dữ liệu từ URL: {}", e.getMessage(), e);
        }

        // Lưu danh sách vào cơ sở dữ liệu nếu có dữ liệu
        try {
            if (!exchangeRates.isEmpty()) {
                exchangeRateRepository.saveAll(exchangeRates);
            }
        } catch (Exception e) {
            logger.error("Không thể lưu dữ liệu tỷ giá vào cơ sở dữ liệu", e);
        }

        return exchangeRates;
    }

    // Phân tích dữ liệu XML
    private List<ExchangeRate> parseXmlExchangeRate(String xmlContent) throws Exception {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xmlContent));
        Document document = builder.parse(inputSource);

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        String globalTimeExpression = "/ExrateList/DateTime";
        XPathExpression timeExpression = xpath.compile(globalTimeExpression);
        String globalTimeStr = (String) timeExpression.evaluate(document, XPathConstants.STRING);

        Date globalTime = null;
        if (globalTimeStr != null && !globalTimeStr.trim().isEmpty()) {
            globalTime = parseDate(globalTimeStr); // chuyển đổi string thành kiểu date
        }

        String expression = "/ExrateList/Exrate";
        XPathExpression expr = xpath.compile(expression);

        NodeList dataNodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        // Lặp qua các node
        for (int i = 0; i < dataNodes.getLength(); i++) {
            Node dataNode = dataNodes.item(i);

            String currencyCode = getAttributeValue(dataNode, "CurrencyCode");
            String currencyName = getAttributeValue(dataNode, "CurrencyName");
            String buyValue = getAttributeValue(dataNode, "Buy");
            String transferValue = getAttributeValue(dataNode, "Transfer");
            String sellValue = getAttributeValue(dataNode, "Sell");

            BigDecimal buy = parseCurrency(buyValue);
            BigDecimal transfer = parseCurrency(transferValue);
            BigDecimal sell = parseCurrency(sellValue);

            if (isValidDate(globalTimeStr)) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setCurrencyCode(currencyCode);
                exchangeRate.setCurrencyName(currencyName);
                exchangeRate.setBuyRate(buy);
                exchangeRate.setTransferRate(transfer);
                exchangeRate.setSellRate(sell);
                exchangeRate.setUpdatedTime(globalTime);

                exchangeRates.add(exchangeRate);
            }
        }

        return exchangeRates;
    }

    // Phân tích dữ liệu JSON
    private List<ExchangeRate> parseJsonExchangerate(String jsonContent) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(jsonContent, JsonObject.class);
        String globalTimeStr = jsonObject.getAsJsonObject("ExrateList").get("DateTime").getAsString();
        Date globalTime = null;
        if (globalTimeStr != null && !globalTimeStr.trim().isEmpty()) {
            globalTime = parseDate(globalTimeStr);
        }

        JsonArray dataArray = jsonObject.getAsJsonObject("ExrateList").getAsJsonArray("Exrate");

        // Duyệt qua từng mục trong mảng Data
        for (int i = 0; i < dataArray.size(); i++) {
            JsonObject data = dataArray.get(i).getAsJsonObject();

            String currencyCode = data.get("CurrencyCode").getAsString();
            String currencyName = data.get("CurrencyName").getAsString();
            String buyValue = data.get("Buy").getAsString();
            String transferValue = data.get("Transfer").getAsString();
            String sellValue = data.get("Sell").getAsString();

            BigDecimal buy = parseCurrency(buyValue);
            BigDecimal transfer = parseCurrency(transferValue);
            BigDecimal sell = parseCurrency(sellValue);

            if (isValidDate(globalTimeStr)) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setCurrencyCode(currencyCode);
                exchangeRate.setCurrencyName(currencyName);
                exchangeRate.setBuyRate(buy);
                exchangeRate.setTransferRate(transfer);
                exchangeRate.setSellRate(sell);
                exchangeRate.setUpdatedTime(globalTime);

                exchangeRates.add(exchangeRate);
            }
        }

        return exchangeRates;
    }

    // Phương thức lấy giá trị thuộc tính từ Node XML
    private String getAttributeValue(Node node, String attributeName) {
        if (node != null && node.getAttributes() != null && node.getAttributes().getNamedItem(attributeName) != null) {
            return node.getAttributes().getNamedItem(attributeName).getNodeValue();
        }
        return ""; // Trả về chuỗi rỗng nếu không tìm thấy thuộc tính
    }

    // kiểm tra ngày
    public boolean isValidDate(String dateStr) {
        // Định dạng cho chuỗi ngày tháng với AM/PM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");

        try {
            // Phân tích chuỗi ngày tháng với định dạng
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
//            System.out.println("ngày tháng nhận được" +dateStr);
            return true;
        } catch (Exception e) {
            // Nếu có lỗi, ghi log lỗi và trả về false
            logger.warn("Ngày không hợp lệ: " + dateStr);

            return false;
        }
    }

        // Chuyển đổi chuỗi ngày thành đối tượng java.sql.Date
    public static Date parseDate(String dateString) {
        try {
            // Định dạng ngày tháng là "MM/dd/yyyy hh:mm:ss a"
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            Date parsedDate = dateFormat.parse(dateString);  // Chuyển đổi chuỗi thành đối tượng Date

            // Chuyển đổi java.util.Date thành java.sql.Date
            return new java.sql.Date(parsedDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy tất cả giá vàng từ cơ sở dữ liệu
    public List<ExchangeRate> getExchangeRate() {
        return exchangeRateRepository.findAll();
    }

    // Lấy nội dung từ URL
    public String fetchPriceFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        String xmlContent = stringBuilder.toString();
        if (xmlContent.startsWith("\uFEFF")) {
            xmlContent = xmlContent.substring(1); // Loại bỏ BOM nếu có
        }
        return xmlContent;
    }

    // Phân tích giá trị tiền tệ
    public BigDecimal parseCurrency(String value) {
        if (value == null || value.trim().isEmpty() || value.equals("-")) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value.replace(",", ""));
        } catch (NumberFormatException e) {
            logger.error("Giá trị không hợp lệ: {}", value, e);
            return BigDecimal.ZERO; // Trả về 0 nếu không parse được
        }
    }

    // Lấy tỷ giá cập nhật nhất
    public List<ExchangeRate> getCurentExchangeRates() {
        return exchangeRateRepository.findExchangeRateByLatestDate();
    }
}
