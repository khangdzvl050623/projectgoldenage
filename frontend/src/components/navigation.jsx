import React from "react";
import { Link, useNavigate } from "react-router-dom";

export const Navigation = () => {
  // Lấy thông tin user từ localStorage
  const user = JSON.parse(localStorage.getItem('user'));
  const navigate = useNavigate();

  const handleLogout = () => {
    // Xóa token và user info
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    // Chuyển về trang chủ
    navigate('/');
    // Refresh page để cập nhật state
    window.location.reload();
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
      <div className="container">
        <Link className="navbar-brand" to="/">
          React Landing Page
        </Link>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            <li className="nav-item">
              <Link className="nav-link" to="/exchangerate">Exchange Rate</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/goldprice">Gold Price</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/news">Tin tức mới nhất</Link>
            </li>
            {/* Phần login/user info */}
            {user ? (
              <>
                <li className="nav-item">
                  <span className="nav-link">
                    Xin chào, {user.email}
                  </span>
                </li>
                <li className="nav-item">
                  <button
                    onClick={handleLogout}
                    className="nav-link btn btn-link"
                  >
                    Logout
                  </button>
                </li>
              </>
            ) : (
              <li className="nav-item">
                <Link className="nav-link" to="/login">Login</Link>
              </li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};
