import React from 'react';
import { Routes, Route, Link } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import heroImg from './assets/hero.png';
import reactLogo from './assets/react.svg';
import viteLogo from './assets/vite.svg';
import './App.css';

// Simple Landing Page Component
const Home = () => {
  return (
    <>
      <section id="center" style={{ padding: '60px 20px 40px' }}>
        <div className="hero" style={{ display: 'flex', justifyContent: 'center', gap: '15px', marginBottom: '30px' }}>
          <img src={heroImg} className="base" width="120" height="126" alt="AgroConnect Hero" />
          <img src={reactLogo} className="framework" alt="React logo" style={{ height: '50px' }} />
          <img src={viteLogo} className="vite" alt="Vite logo" style={{ height: '50px' }} />
        </div>
        
        <div style={{ marginBottom: '30px' }}>
          <h1 style={{ fontSize: '42px', color: 'var(--text-h)', marginBottom: '16px' }}>
            Welcome to AgroConnect
          </h1>
          <p style={{ fontSize: '18px', maxWidth: '600px', margin: '0 auto', color: 'var(--text)' }}>
            The premier digital gateway connecting local farmers, traders, and markets. Share crops, compare prices, and grow together.
          </p>
        </div>

        {/* Access Links */}
        <div style={{ display: 'flex', justifyContent: 'center', gap: '20px', margin: '30px 0' }}>
          <Link 
            to="/login" 
            style={{ 
              padding: '12px 28px', 
              background: 'linear-gradient(135deg, #2d6a4f 0%, #40916c 100%)', 
              color: '#fff', 
              textDecoration: 'none', 
              borderRadius: '8px', 
              fontWeight: '600',
              boxShadow: '0 4px 12px rgba(45, 106, 79, 0.2)',
              transition: 'all 0.2s ease'
            }}
            onMouseOver={(e) => {
              e.currentTarget.style.transform = 'translateY(-2px)';
              e.currentTarget.style.boxShadow = '0 6px 15px rgba(45, 106, 79, 0.3)';
            }}
            onMouseOut={(e) => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 4px 12px rgba(45, 106, 79, 0.2)';
            }}
          >
            Sign In
          </Link>
          <Link 
            to="/register" 
            style={{ 
              padding: '12px 28px', 
              background: 'transparent', 
              color: '#52b788', 
              border: '1.5px solid #52b788',
              textDecoration: 'none', 
              borderRadius: '8px', 
              fontWeight: '600',
              transition: 'all 0.2s ease'
            }}
            onMouseOver={(e) => {
              e.currentTarget.style.background = 'rgba(82, 183, 136, 0.1)';
              e.currentTarget.style.transform = 'translateY(-2px)';
            }}
            onMouseOut={(e) => {
              e.currentTarget.style.background = 'transparent';
              e.currentTarget.style.transform = 'translateY(0)';
            }}
          >
            Register Now
          </Link>
        </div>
      </section>

      <div className="ticks"></div>

      <section id="next-steps" style={{ padding: '40px 20px', display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '30px', textAlign: 'left' }}>
        <div id="docs">
          <h2>About Platform</h2>
          <p style={{ color: 'var(--text)' }}>
            AgroConnect leverages modern digital tools to reduce middlemen and increase farmer profits. Explore interactive price charts, regional trade options, and direct supply lines.
          </p>
        </div>
        <div id="social">
          <h2>Developer Sandbox</h2>
          <p style={{ color: 'var(--text)', marginBottom: '12px' }}>
            Quickly toggle between routes or visit files under:
          </p>
          <code style={{ fontSize: '14px', background: 'var(--code-bg)', padding: '6px 12px' }}>
            src/pages/Login.jsx
          </code>
          <code style={{ fontSize: '14px', background: 'var(--code-bg)', padding: '6px 12px', marginLeft: '10px' }}>
            src/pages/Register.jsx
          </code>
        </div>
      </section>

      <div className="ticks"></div>
      <section id="spacer" style={{ height: '50px' }}></section>
    </>
  );
};

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
    </Routes>
  );
}

export default App;
