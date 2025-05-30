package com.khang.goldenage.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.khang.goldenage.modal.GoldPrice;
import com.khang.goldenage.repository.GoldPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoldPriceService {

  @Autowired
  private GoldPriceRepository goldPriceRepository;

  // Phương thức chính để phân tích giá từ URL
  public List<GoldPrice> parsePriceFromUrl(String urlString) throws IOException {
    List<GoldPrice> goldPrices = new ArrayList<>();

    try {
      // Lấy dữ liệu từ URL
      String content = fetchPriceFromUrl(urlString);

      if (content == null || content.trim().isEmpty()) {
        throw new IOException("Không nhận được dữ liệu từ API.");
      }

      if (content.trim().startsWith("<?xml")) {
        // Nếu dữ liệu là XML
        goldPrices = parseXmlPrice(content);
      } else if (content.trim().startsWith("{") || content.trim().startsWith("[")) {
        // Nếu dữ liệu là JSON
        goldPrices = parseJsonPrice(content);
      } else {
        throw new IOException("Dữ liệu không phải là XML hay JSON.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Lưu danh sách vào cơ sở dữ liệu nếu có dữ liệu
    if (!goldPrices.isEmpty()) {
      goldPriceRepository.saveAll(goldPrices);
    }
    return goldPrices;
  }

  // Phân tích dữ liệu XML
  private List<GoldPrice> parseXmlPrice(String xmlContent) throws Exception {
    List<GoldPrice> goldPrices = new ArrayList<>();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource inputSource = new InputSource(new StringReader(xmlContent));
    Document document = builder.parse(inputSource);

    XPathFactory xPathfactory = XPathFactory.newInstance();
    XPath xpath = xPathfactory.newXPath();
    String expression = "/DataList/Data";
    XPathExpression expr = xpath.compile(expression);

    NodeList dataNodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

    // Lặp qua các node
    for (int i = 0; i < dataNodes.getLength(); i++) {
      Node dataNode = dataNodes.item(i);

      String name = getAttributeValue(dataNode, "n_" + (i + 1));
      String purchasePrice = getAttributeValue(dataNode, "pb_" + (i + 1));
      String sellPrice = getAttributeValue(dataNode, "ps_" + (i + 1));
      String typePrice = getAttributeValue(dataNode, "k_" + (i + 1));
      String updatedTime = getAttributeValue(dataNode, "d_" + (i + 1));

      if (!isValidDate(updatedTime)) {
        continue;
      }

      GoldPrice goldPrice = new GoldPrice();
      goldPrice.setGoldName(name);
      goldPrice.setPurchasePrice(Integer.parseInt(purchasePrice));
      goldPrice.setSellPrice(Integer.parseInt(sellPrice));
      goldPrice.setGoldType(typePrice);
      goldPrice.setUpdatedTime(parseDate(updatedTime));
      Date now = new Date(); // Thời điểm fetch/crawl
      goldPrice.setFetchedTime(now);
      goldPrices.add(goldPrice);
    }

    return goldPrices;
  }

  // Phân tích dữ liệu JSON
  private List<GoldPrice> parseJsonPrice(String jsonContent) {
    List<GoldPrice> goldPrices = new ArrayList<>();
    Gson gson = new Gson();

    // Chuyển đổi chuỗi JSON thành đối tượng JsonObject
    JsonObject jsonObject = gson.fromJson(jsonContent, JsonObject.class);
    JsonArray dataArray = jsonObject.getAsJsonObject("DataList").getAsJsonArray("Data");

    // Duyệt qua từng mục trong mảng Data
    for (int i = 0; i < dataArray.size(); i++) {
      JsonObject data = dataArray.get(i).getAsJsonObject();

      String name = data.get("@n_" + (i + 1)).getAsString();
      String purchasePrice = data.get("@pb_" + (i + 1)).getAsString();
      String sellPrice = data.get("@ps_" + (i + 1)).getAsString();
      String typePrice = data.get("@k_" + (i + 1)).getAsString();
      String updatedTime = data.get("@d_" + (i + 1)).getAsString();

      if (!isValidDate(updatedTime)) {
        continue;
      }


      GoldPrice goldPrice = new GoldPrice();
      goldPrice.setGoldName(name);
      goldPrice.setPurchasePrice(Integer.parseInt(purchasePrice));
      goldPrice.setSellPrice(Integer.parseInt(sellPrice));
      goldPrice.setGoldType(typePrice);
      goldPrice.setUpdatedTime(parseDate(updatedTime));
      Date now = new Date(); // Thời điểm fetch/crawl
      goldPrice.setFetchedTime(now);

      goldPrices.add(goldPrice);
    }

    return goldPrices;
  }

  // Lấy giá trị của thuộc tính từ Node, trả về "" nếu không tìm thấy
  private String getAttributeValue(Node node, String attributeName) {
    if (node.getAttributes().getNamedItem(attributeName) != null) {
      return node.getAttributes().getNamedItem(attributeName).getNodeValue();
    }
    return "";
  }

  // Kiểm tra tính hợp lệ của ngày tháng
  private boolean isValidDate(String dateStr) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // ktra ngày thaang hoợp lệSimpleDateFormat
    dateFormat.setLenient(false); // đảm bảo ngày tháng chính xác
    try {
      dateFormat.parse(dateStr);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  // Chuyển đổi chuỗi ngày thành đối tượng java.sql.Date
  private java.util.Date parseDate(String dateStr) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    try {
      return dateFormat.parse(dateStr); // ✅ Trả về full date + time
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }


  // Lấy tất cả giá vàng từ cơ sở dữ liệu
  public List<GoldPrice> getGoldPrices() {
    return goldPriceRepository.findAll();
  }

  // Lấy nội dung từ URL
  public String fetchPriceFromUrl(String url) throws IOException {
    RestTemplate restTemplate = new RestTemplate();//RestTemplate gửi yêu cầu va nhan ve với dạng chuỗi
    String content = restTemplate.getForObject(url, String.class);
    if (content != null) {
      content = content.replaceAll("\uFEFF", "").trim(); // Loại bỏ BOM và trim các khoảng trắng
    }
    return content;
  }


  public List<GoldPrice> getLatestGoldPrices() {
    List<GoldPrice> latestGoldPrices = goldPriceRepository.findLatestGoldPricesByUpdatedTime();
    latestGoldPrices.forEach(price -> System.out.println(
      "[DEBUG] GoldType: " + price.getGoldType() +
        ", Buy: " + price.getPurchasePrice() +
        ", Sell: " + price.getSellPrice() +
        ", UpdatedTime: " + price.getUpdatedTime()));
    return latestGoldPrices;
  }
}
