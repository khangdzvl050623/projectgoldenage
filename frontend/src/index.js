import React from 'react';
import ReactDOM from 'react-dom/client';  // Đảm bảo  sử dụng 'react-dom/client'
import { BrowserRouter as Router } from 'react-router-dom';

import App from './App';
import reportWebVitals from './reportWebVitals';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Router>
      <App />
    </Router>
  </React.StrictMode>
);

// Đo lường hiệu suất
reportWebVitals();
