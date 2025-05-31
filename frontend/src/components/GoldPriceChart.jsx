import React, { useEffect, useState } from 'react';
import { Line } from 'react-chartjs-2';
import axios from 'axios';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, Filler);

const GoldPriceChart = () => {
  const [chartData, setChartData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [goldNames, setGoldNames] = useState([]);
  const [selectedGold, setSelectedGold] = useState('');

  useEffect(() => {
    axios.get('https://goldenages.online/api/gold-prices/history')
      .then((response) => {
        if (response.data && Array.isArray(response.data)) {
          const names = [...new Set(response.data.map(item => item.goldName))];
          setGoldNames(names);
          // Mặc định chọn loại đầu tiên
          setSelectedGold(names[0] || '');
        }
      })
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    if (!selectedGold) return;
    setLoading(true);
    axios.get('https://goldenages.online/api/gold-prices/history')
      .then((response) => {
        if (response.data && Array.isArray(response.data)) {
          const filtered = response.data.filter(item => item.goldName === selectedGold);
          const sorted = [...filtered].sort((a, b) => new Date(a.updatedTime) - new Date(b.updatedTime));
          setChartData({
            labels: sorted.map(item =>
              new Date(item.updatedTime).toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit', day: '2-digit', month: '2-digit' })
            ),
            datasets: [
              {
                label: 'Giá mua',
                data: sorted.map(item => item.purchasePrice),
                borderColor: 'rgba(255, 206, 86, 1)',
                backgroundColor: 'rgba(255, 206, 86, 0.2)',
                fill: true,
                tension: 0.4,
              },
              {
                label: 'Giá bán',
                data: sorted.map(item => item.sellPrice),
                borderColor: 'rgba(54, 162, 235, 1)',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                fill: true,
                tension: 0.4,
              }
            ]
          });
        }
      })
      .finally(() => setLoading(false));
  }, [selectedGold]);

  const options = {
    responsive: true,
    plugins: {
      legend: { position: 'top' },
      title: { display: true, text: `Biểu đồ giá vàng ${selectedGold} theo thời gian` },
    },
    scales: {
      y: {
        beginAtZero: false,
        ticks: {
          callback: function(value) {
            return value.toLocaleString('vi-VN') + ' VNĐ';
          }
        }
      }
    }
  };

  return (
    <div>
      <div style={{ margin: '20px 0' }}>
        <label>Chọn loại vàng: </label>
        <select value={selectedGold} onChange={e => setSelectedGold(e.target.value)}>
          {goldNames.map(name => (
            <option key={name} value={name}>{name}</option>
          ))}
        </select>
      </div>
      {loading && <div>Đang tải biểu đồ...</div>}
      {!loading && chartData && <Line data={chartData} options={options} />}
      {!loading && !chartData && <div>Không có dữ liệu biểu đồ</div>}
    </div>
  );
};

export default GoldPriceChart;
