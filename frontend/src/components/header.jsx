import React from "react";

export const Header = (props) => {
  return (
    <header id="header">
      <div className="intro" style={{
        backgroundImage: 'url("/img/intro-bg.jpg")',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        height: '100vh',
        position: 'relative'
      }}>
        <div className="overlay" style={{
          background: 'rgba(255, 255, 255, 0.8)',
          position: 'absolute',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0
        }}></div>
        <div className="container" style={{ 
          position: 'relative', 
          zIndex: 1,
          paddingTop: '150px',
          color: '#333'
        }}>
          <h1>Golden Age Jewelry</h1>
          <p>Discover our exquisite collection of fine jewelry, where timeless elegance meets modern design.</p>
          <button className="btn btn-custom" style={{
            border: '2px solid #333',
            color: '#333'
          }}>Learn More</button>
        </div>
      </div>
    </header>
  );
};
