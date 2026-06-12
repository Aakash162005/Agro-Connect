import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { FiMail, FiLock, FiEye, FiEyeOff, FiUser, FiPhone } from 'react-icons/fi';
import { FaLeaf, FaBriefcase } from 'react-icons/fa';
import './Auth.css';

const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [role, setRole] = useState('farmer');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [agreeTerms, setAgreeTerms] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');

    // Validation
    if (!name || !email || !phone || !password || !confirmPassword) {
      setError('Please fill in all fields.');
      return;
    }

    if (!/\S+@\S+\.\S+/.test(email)) {
      setError('Please enter a valid email address.');
      return;
    }

    if (password.length < 6) {
      setError('Password must be at least 6 characters long.');
      return;
    }

    if (password !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }

    if (!agreeTerms) {
      setError('You must agree to the Terms of Service and Privacy Policy.');
      return;
    }

    setLoading(true);
    // Simulate API Call
    setTimeout(() => {
      setLoading(false);
      alert('Registration successful! Welcome to AgroConnect.');
    }, 1500);
  };

  return (
    <div className="auth-container" style={{ minHeight: '110vh', padding: '40px 20px' }}>
      <div className="auth-card" style={{ maxWidth: '520px' }}>
        {/* Brand Header */}
        <div className="auth-header">
          <div className="auth-logo">
            <FaLeaf />
          </div>
          <h2 className="auth-title">Create Account</h2>
          <p className="auth-subtitle">Join the AgroConnect farming network</p>
        </div>

        {/* Error Alert */}
        {error && <div className="auth-error">{error}</div>}

        {/* Form */}
        <form onSubmit={handleSubmit}>
          {/* Name input */}
          <div className="form-group">
            <label className="form-label">Full Name</label>
            <div className="input-wrapper">
              <input
                type="text"
                className="auth-input"
                placeholder="John Doe"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              <FiUser className="input-icon" />
            </div>
          </div>

          {/* Email input */}
          <div className="form-group">
            <label className="form-label">Email Address</label>
            <div className="input-wrapper">
              <input
                type="email"
                className="auth-input"
                placeholder="name@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <FiMail className="input-icon" />
            </div>
          </div>

          {/* Phone Number input */}
          <div className="form-group">
            <label className="form-label">Phone Number</label>
            <div className="input-wrapper">
              <input
                type="tel"
                className="auth-input"
                placeholder="+1 (555) 000-0000"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
              />
              <FiPhone className="input-icon" />
            </div>
          </div>

          {/* Role selector dropdown */}
          <div className="form-group">
            <label className="form-label">Select Your Role</label>
            <div className="input-wrapper">
              <select
                className="auth-input"
                value={role}
                onChange={(e) => setRole(e.target.value)}
                style={{ appearance: 'none', cursor: 'pointer' }}
              >
                <option value="farmer">Farmer (Seller)</option>
                <option value="trader">Trader / Merchant</option>
                <option value="retailer">Retailer / Store Owner</option>
                <option value="consumer">Consumer (Buyer)</option>
              </select>
              <FaBriefcase className="input-icon" />
              {/* Custom indicator arrow for select tag */}
              <div
                style={{
                  position: 'absolute',
                  right: '16px',
                  pointerEvents: 'none',
                  color: '#6b7280',
                  borderLeft: '5px solid transparent',
                  borderRight: '5px solid transparent',
                  borderTop: '5px solid #6b7280',
                }}
              />
            </div>
          </div>

          {/* Password input */}
          <div className="form-group">
            <label className="form-label">Password</label>
            <div className="input-wrapper">
              <input
                type={showPassword ? 'text' : 'password'}
                className="auth-input"
                placeholder="Min. 6 characters"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <FiLock className="input-icon" />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                aria-label={showPassword ? 'Hide password' : 'Show password'}
              >
                {showPassword ? <FiEyeOff /> : <FiEye />}
              </button>
            </div>
          </div>

          {/* Confirm Password input */}
          <div className="form-group">
            <label className="form-label">Confirm Password</label>
            <div className="input-wrapper">
              <input
                type={showPassword ? 'text' : 'password'}
                className="auth-input"
                placeholder="Repeat password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
              <FiLock className="input-icon" />
            </div>
          </div>

          {/* Agree to terms */}
          <div className="auth-utils">
            <label className="checkbox-container">
              <input
                type="checkbox"
                checked={agreeTerms}
                onChange={(e) => setAgreeTerms(e.target.checked)}
              />
              I agree to the <a href="#terms" className="forgot-link">Terms</a> & <a href="#privacy" className="forgot-link">Privacy Policy</a>
            </label>
          </div>

          {/* Register Button */}
          <button type="submit" className="auth-btn" disabled={loading}>
            {loading ? 'Creating Account...' : 'Sign Up'}
          </button>
        </form>

        {/* Divider */}
        <div className="divider" style={{ marginTop: '20px' }}>already have an account?</div>

        {/* Login Redirect Link */}
        <div className="auth-footer-text">
          Have an account? <Link to="/login">Sign In</Link>
        </div>
      </div>
    </div>
  );
};

export default Register;
