import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function ExchangeRateTable() {
    const [exchangeRates, setExchangeRates] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Gọi API lấy tỷ giá hối đoái
        axios
            .get('https://goldenages.online/api/exchange-rate/current-exchange-rate')
            .then((response) => {
                if (response.data && Array.isArray(response.data)) {
                    const uniqueRates = filterUniqueCurrencyCodes(response.data);
                    setExchangeRates(uniqueRates);
                } else {
                    setExchangeRates([]);
                }
            })
            .catch((err) => {
                setError('Failed to load exchange rates');
            })
            .finally(() => setLoading(false));
    }, []);

    // Hàm lọc các tỷ giá trùng lặp
    const filterUniqueCurrencyCodes = (data) => {
        const seenCurrencyCodes = new Set();
        return data.filter((rate) => {
            if (seenCurrencyCodes.has(rate.currencyCode)) {
                return false;
            } else {
                seenCurrencyCodes.add(rate.currencyCode);
                return true;
            }
        });
    };

    if (loading) return <p>Loading exchange rates...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="container" style={{ marginTop: '100px' }}>
            <div className="row">
                <div className="col-12">
                    <h2 className="mb-4">Exchange Rate</h2>
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>Currency</th>
                                <th>Buy</th>
                                <th>Sell</th>
                            </tr>
                        </thead>
                        <tbody>
                            {exchangeRates.length > 0 ? (
                                exchangeRates.map((rate, index) => (
                                    <tr key={index}>
                                        <td>{rate.currencyCode}</td>
                                        <td>{rate.buyRate ? rate.buyRate.toLocaleString('vi-VN') : 'N/A'} VND</td>
                                        <td>{rate.sellRate ? rate.sellRate.toLocaleString('vi-VN') : 'N/A'} VND</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="3">No unique data available</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
