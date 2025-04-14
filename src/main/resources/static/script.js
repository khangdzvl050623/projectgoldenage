$(document).ready(function () {
    // Hàm xóa bỏ các bản ghi trùng lặp theo key
    function removeDuplicatesByKey(data, key) {
        let uniqueData = [];
        let uniqueSet = new Set();

        data.forEach((item) => {
            if (item && item[key] && !uniqueSet.has(item[key])) {
                uniqueSet.add(item[key]);
                uniqueData.push(item);
            }
        });

        return uniqueData;
    }

    // Lấy giá vàng hiện tại từ API
    $.ajax({
        url: 'http://localhost:8383/api/gold-prices/current-gold-prices',
        method: 'GET',
        success: function (data) {
            if (data && Array.isArray(data)) {
                let goldListHtml = '';
                let filteredData = removeDuplicatesByKey(data, 'goldName');

                filteredData.forEach((goldPrice, index) => {
                    goldListHtml += `
                        <tr>
                            <td>${index + 1}</td>
                            <td>${goldPrice.goldName || 'N/A'}</td>
                            <td>${goldPrice.goldType || 'N/A'}</td>
                            <td>${goldPrice.purchasePrice ? goldPrice.purchasePrice.toLocaleString('vi-VN') : 'N/A'} VND</td>
                            <td>${goldPrice.sellPrice ? goldPrice.sellPrice.toLocaleString('vi-VN') : 'N/A'} VND</td>
                        </tr>
                    `;
                });

                $('#gold-price-table tbody').html(goldListHtml);
            } else {
                $('#gold-price-table tbody').html('<tr><td colspan="5">No data available</td></tr>');
            }
        },
        error: function () {
            $('#gold-price-table tbody').html('<tr><td colspan="5">Failed to load data</td></tr>');
        }
    });

    // API tỷ giá hối đoái
    $.ajax({
        url: 'http://localhost:8383/api/exchange-rate/current-exchange-rate',  // URL API để lấy tỷ giá hối đoái
        method: 'GET',
        success: function (data) {
            if (data && Array.isArray(data)) {
                // Tạo một đối tượng để theo dõi các currencyCode đã xử lý
                let seenCurrencyCodes = new Set();
                let exchangeRateListHtml = '';

                data.forEach((rate, index) => {
                    // Kiểm tra nếu currencyCode đã tồn tại trong Set
                    if (!seenCurrencyCodes.has(rate.currencyCode)) {
                        // Nếu chưa tồn tại, thêm vào Set và tạo HTML cho dòng
                        seenCurrencyCodes.add(rate.currencyCode);
                        exchangeRateListHtml += `
                        <tr>
                            <td>${index + 1}</td>
                            <td>${rate.currencyCode}</td>
                            <td>${rate.currencyName.trim()}</td>  <!-- Loại bỏ khoảng trắng thừa -->
                            <td>${rate.buyRate ? rate.buyRate.toLocaleString('vi-VN') : 'N/A'} VND</td>
                            <td>${rate.transferRate ? rate.transferRate.toLocaleString('vi-VN') : 'N/A'} VND</td>
                            <td>${rate.sellRate ? rate.sellRate.toLocaleString('vi-VN') : 'N/A'} VND</td>
                        </tr>
                    `;
                    }
                });

                // Nếu không có dữ liệu hợp lệ, hiển thị thông báo
                if (exchangeRateListHtml) {
                    $('#currency-exchange-table tbody').html(exchangeRateListHtml);
                } else {
                    $('#currency-exchange-table tbody').html('<tr><td colspan="6">No unique data available</td></tr>');
                }
            } else {
                $('#currency-exchange-table tbody').html('<tr><td colspan="6">No data available</td></tr>');
            }
        },
        error: function () {
            $('#currency-exchange-table tbody').html('<tr><td colspan="6">Failed to load data</td></tr>');
        }
    });


    // Lấy dữ liệu lịch sử giá vàng từ API
    $.ajax({
        url: 'http://localhost:8383/api/gold-prices/history',
        method: 'GET',
        success: function (data) {
            if (data && Array.isArray(data)) {
                let groupedData = groupGoldHistoryByName(data);

                for (let goldName in groupedData) {
                    let chartId = `chart-${goldName.replace(/[^\w]/g, '_')}`;
                    let goldData = groupedData[goldName];

                    $('#gold-history-container').append(`
                        <div class="gold-chart-container">
                            <h3>${goldName}</h3>
                            <canvas id="${chartId}" width="400" height="200"></canvas>
                        </div>
                    `);

                    renderGoldHistoryChart(chartId, goldData);
                }
            } else {
                $('#gold-history-container').html('<p>No historical data available.</p>');
            }
        },
        error: function () {
            $('#gold-history-container').html('<p>Failed to load historical data.</p>');
        }
    });

    // Nhóm dữ liệu lịch sử theo `goldName`
    function groupGoldHistoryByName(data) {
        let groupedData = {};

        data.forEach((item) => {
            if (item && item.goldName) {
                if (!groupedData[item.goldName]) {
                    groupedData[item.goldName] = [];
                }
                groupedData[item.goldName].push({
                    date: new Date(item.updatedTime).toLocaleDateString('vi-VN'),
                    purchasePrice: item.purchasePrice,
                    sellPrice: item.sellPrice,
                });
            }
        });

        return groupedData;
    }

    // Vẽ biểu đồ lịch sử giá vàng
    function renderGoldHistoryChart(chartId, goldData) {
        let dates = goldData.map(item => item.date);
        let purchasePrices = goldData.map(item => item.purchasePrice);
        let sellPrices = goldData.map(item => item.sellPrice);

        let ctx = document.getElementById(chartId).getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: dates,
                datasets: [
                    {
                        label: 'Purchase Price (VND)',
                        data: purchasePrices,
                        borderColor: 'rgba(75, 192, 192, 1)',
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        tension: 0.1,
                    },
                    {
                        label: 'Sell Price (VND)',
                        data: sellPrices,
                        borderColor: 'rgba(255, 99, 132, 1)',
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        tension: 0.1,
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Gold Price History'
                    }
                },
                scales: {
                    x: {
                        title: { display: true, text: 'Date' }
                    },
                    y: {
                        title: { display: true, text: 'Price (VND)' },
                        beginAtZero: false
                    }
                }
            }
        });
    }
});
$.ajax({
    url: 'http://localhost:8383/api/exchange-rate/history',
    method: 'GET',
    success: function (data) {
        if (data && Array.isArray(data)) {
            // Lọc dữ liệu và lấy các thông tin cần thiết
            let labels = [];
            let buyRates = [];
            let transferRates = [];
            let sellRates = [];

            data.forEach(rate => {
                labels.push(rate.currencyCode);  // Mã tiền tệ làm nhãn
                buyRates.push(rate.buyRate);     // Tỷ giá mua
                transferRates.push(rate.transferRate); // Tỷ giá chuyển khoản
                sellRates.push(rate.sellRate);  // Tỷ giá bán
            });

            // Tạo biểu đồ
            var ctx = document.getElementById('exchangeRateChart').getContext('2d');
            var exchangeRateChart = new Chart(ctx, {
                type: 'line',  // Loại biểu đồ: line (đường)
                data: {
                    labels: labels,  // Nhãn cho các điểm dữ liệu (tiền tệ)
                    datasets: [{
                        label: 'Buy Rate', // Tên cho tỷ giá mua
                        data: buyRates,   // Dữ liệu tỷ giá mua
                        borderColor: 'rgba(75, 192, 192, 1)', // Màu cho tỷ giá mua
                        fill: false
                    }, {
                        label: 'Transfer Rate', // Tên cho tỷ giá chuyển khoản
                        data: transferRates,   // Dữ liệu tỷ giá chuyển khoản
                        borderColor: 'rgba(153, 102, 255, 1)', // Màu cho tỷ giá chuyển khoản
                        fill: false
                    }, {
                        label: 'Sell Rate',  // Tên cho tỷ giá bán
                        data: sellRates,    // Dữ liệu tỷ giá bán
                        borderColor: 'rgba(255, 159, 64, 1)', // Màu cho tỷ giá bán
                        fill: false
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: false,
                            ticks: {
                                callback: function(value) {
                                    return value.toLocaleString('vi-VN') + ' VND';  // Định dạng tiền tệ
                                }
                            }
                        }
                    }
                }
            });
        }
    },
    error: function () {
        alert('Failed to load data');
    }
});

