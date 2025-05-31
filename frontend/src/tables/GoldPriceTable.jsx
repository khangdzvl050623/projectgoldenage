import React, { useState, useEffect } from 'react';
import axios from 'axios';

const GoldPriceTable = () => {
    const [goldPrices, setGoldPrices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        axios
            .get('https://goldenages.online/api/gold-prices/current-gold-prices')
            .then((response) => {
                if (response.data && Array.isArray(response.data)) {
                    const filteredData = getLatestGoldPrices(response.data, 'goldName', 'updatedTime');
                    setGoldPrices(filteredData);
                } else {
                    setGoldPrices([]);
                }
            })
            .catch((err) => {
                console.error('Error fetching gold prices:', err);
                setError('Failed to load gold prices');
            })
            .finally(() => setLoading(false));
    }, []);

    const getLatestGoldPrices = (array, key, timeKey) => {
        const map = {};
        array.forEach(item => {
            if (!map[item[key]] || new Date(item[timeKey]) > new Date(map[item[key]][timeKey])) {
                map[item[key]] = item;
            }
        });
        return Object.values(map);
    };

    if (loading) return (
        <div className="container" style={{ marginTop: '100px' }}>
            <div className="text-center">Loading gold prices...</div>
        </div>
    );

    if (error) return (
        <div className="container" style={{ marginTop: '100px' }}>
            <div className="alert alert-danger">{error}</div>
        </div>
    );

    return (
        <div className="container" style={{ marginTop: '100px' }}>
            <h2 className="mb-4">Gold Price</h2>
            <div className="table-responsive">
                <table className="table table-striped table-bordered">
                    <thead className="table-dark">
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Tên Vàng</th>
                            <th scope="col">Loại</th>
                            <th scope="col">Giá Mua</th>
                            <th scope="col">Giá Bán</th>
                        </tr>
                    </thead>
                    <tbody>
                        {goldPrices.length > 0 ? (
                            goldPrices.map((goldPrice, index) => (
                                <tr key={index}>
                                    <td>{index + 1}</td>
                                    <td>{goldPrice.goldName || 'N/A'}</td>
                                    <td>{goldPrice.goldType || 'N/A'}</td>
                                    <td className="text-end">
                                        {goldPrice.purchasePrice
                                            ? `${goldPrice.purchasePrice.toLocaleString('vi-VN')} VNĐ`
                                            : 'N/A'}
                                    </td>
                                    <td className="text-end">
                                        {goldPrice.sellPrice
                                            ? `${goldPrice.sellPrice.toLocaleString('vi-VN')} VNĐ`
                                            : 'N/A'}
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5" className="text-center">Không có dữ liệu</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default GoldPriceTable;
