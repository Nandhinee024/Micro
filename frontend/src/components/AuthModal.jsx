import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { userApi } from '../services/api';
import { X, User, Mail, Lock, Sparkles, LogIn, UserPlus, Users } from 'lucide-react';

const DEMO_USERS = [
  {
    id: 1,
    username: 'alex_rivers',
    firstName: 'Alex',
    lastName: 'Rivers',
    email: 'alex@example.com',
    profilePictureUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150',
    bio: 'Digital Creator & Fullstack Developer | Building the future 🚀'
  },
  {
    id: 2,
    username: 'sarah_tech',
    firstName: 'Sarah',
    lastName: 'Chen',
    email: 'sarah@example.com',
    profilePictureUrl: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150',
    bio: 'Cloud Architect & Microservices enthusiast ☁️ | Coffee lover ☕'
  },
  {
    id: 3,
    username: 'marcus_design',
    firstName: 'Marcus',
    lastName: 'Vance',
    email: 'marcus@example.com',
    profilePictureUrl: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150',
    bio: 'Product Designer & UI/UX Specialist ✨ Minimalist vibes'
  }
];

export default function AuthModal() {
  const { isAuthModalOpen, setIsAuthModalOpen, switchUser, currentUser } = useApp();
  const [mode, setMode] = useState('demo'); // 'demo', 'login', 'register'
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    firstName: '',
    lastName: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  if (!isAuthModalOpen) return null;

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      setError('');
      const res = await userApi.register(formData);
      if (res) {
        switchUser({
          id: res.id,
          username: res.username,
          email: res.email,
          firstName: res.firstName,
          lastName: res.lastName,
          profilePictureUrl: res.profilePictureUrl,
          bio: res.bio
        });
        setIsAuthModalOpen(false);
      }
    } catch (err) {
      setError(err.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      setError('');
      const res = await userApi.login({
        usernameOrEmail: formData.username,
        password: formData.password
      });
      if (res) {
        switchUser({
          id: res.id,
          username: res.username,
          email: res.email,
          firstName: res.firstName,
          lastName: res.lastName,
          profilePictureUrl: res.profilePictureUrl,
          bio: res.bio
        });
        setIsAuthModalOpen(false);
      }
    } catch (err) {
      setError(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  const handleSelectDemo = (user) => {
    switchUser(user);
    setIsAuthModalOpen(false);
  };

  return (
    <div className="modal-backdrop" onClick={() => setIsAuthModalOpen(false)}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
          <h2 style={{ fontSize: '1.25rem' }}>Account & User Selection</h2>
          <button className="action-btn" onClick={() => setIsAuthModalOpen(false)}>
            <X size={20} />
          </button>
        </div>

        {/* Tab switch */}
        <div style={{ display: 'flex', gap: '6px', background: 'rgba(255,255,255,0.05)', padding: '4px', borderRadius: 'var(--radius-md)', marginBottom: '20px' }}>
          <button
            type="button"
            className="btn-secondary"
            onClick={() => { setMode('demo'); setError(''); }}
            style={{
              flex: 1,
              justifyContent: 'center',
              background: mode === 'demo' ? 'var(--accent-gradient)' : 'transparent',
              color: mode === 'demo' ? 'white' : 'var(--text-secondary)',
              border: 'none',
              padding: '6px 10px',
              fontSize: '0.85rem'
            }}
          >
            <Users size={14} /> Quick Switch
          </button>
          <button
            type="button"
            className="btn-secondary"
            onClick={() => { setMode('login'); setError(''); }}
            style={{
              flex: 1,
              justifyContent: 'center',
              background: mode === 'login' ? 'var(--accent-gradient)' : 'transparent',
              color: mode === 'login' ? 'white' : 'var(--text-secondary)',
              border: 'none',
              padding: '6px 10px',
              fontSize: '0.85rem'
            }}
          >
            <LogIn size={14} /> Login
          </button>
          <button
            type="button"
            className="btn-secondary"
            onClick={() => { setMode('register'); setError(''); }}
            style={{
              flex: 1,
              justifyContent: 'center',
              background: mode === 'register' ? 'var(--accent-gradient)' : 'transparent',
              color: mode === 'register' ? 'white' : 'var(--text-secondary)',
              border: 'none',
              padding: '6px 10px',
              fontSize: '0.85rem'
            }}
          >
            <UserPlus size={14} /> Register
          </button>
        </div>

        {error && (
          <div style={{
            background: 'rgba(239, 68, 68, 0.15)',
            border: '1px solid var(--danger)',
            color: '#fca5a5',
            padding: '10px 14px',
            borderRadius: 'var(--radius-md)',
            marginBottom: '16px',
            fontSize: '0.85rem'
          }}>
            {error}
          </div>
        )}

        {/* Quick Demo Switcher */}
        {mode === 'demo' && (
          <div>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '14px' }}>
              Select an active profile to test interactions across User, Post, Like, Comment, and Notification services:
            </p>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
              {DEMO_USERS.map((user) => {
                const isCurrent = currentUser && currentUser.id === user.id;
                return (
                  <div
                    key={user.id}
                    onClick={() => handleSelectDemo(user)}
                    className="glass-card"
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: '12px',
                      padding: '12px',
                      cursor: 'pointer',
                      borderColor: isCurrent ? 'var(--accent-primary)' : 'var(--border-color)',
                      background: isCurrent ? 'rgba(99, 102, 241, 0.15)' : 'var(--bg-card)'
                    }}
                  >
                    <img src={user.profilePictureUrl} alt={user.username} className="avatar avatar-sm" />
                    <div style={{ flex: 1 }}>
                      <div style={{ fontWeight: '700', fontSize: '0.9rem' }}>{user.firstName} {user.lastName}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>@{user.username}</div>
                    </div>
                    {isCurrent ? (
                      <span className="badge">Active</span>
                    ) : (
                      <button className="btn-secondary" style={{ padding: '4px 10px', fontSize: '0.75rem' }}>
                        Switch
                      </button>
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* Login Form */}
        {mode === 'login' && (
          <form onSubmit={handleLogin}>
            <div className="form-group">
              <label className="form-label">Username or Email</label>
              <input
                type="text"
                name="username"
                className="form-input"
                required
                value={formData.username}
                onChange={handleChange}
                placeholder="alex_rivers"
              />
            </div>
            <div className="form-group">
              <label className="form-label">Password</label>
              <input
                type="password"
                name="password"
                className="form-input"
                required
                value={formData.password}
                onChange={handleChange}
                placeholder="••••••••"
              />
            </div>
            <button
              type="submit"
              className="gradient-btn"
              disabled={loading}
              style={{ width: '100%', marginTop: '10px' }}
            >
              {loading ? 'Logging in...' : 'Sign In'}
            </button>
          </form>
        )}

        {/* Register Form */}
        {mode === 'register' && (
          <form onSubmit={handleRegister}>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px' }}>
              <div className="form-group">
                <label className="form-label">First Name</label>
                <input
                  type="text"
                  name="firstName"
                  className="form-input"
                  required
                  value={formData.firstName}
                  onChange={handleChange}
                  placeholder="John"
                />
              </div>
              <div className="form-group">
                <label className="form-label">Last Name</label>
                <input
                  type="text"
                  name="lastName"
                  className="form-input"
                  required
                  value={formData.lastName}
                  onChange={handleChange}
                  placeholder="Doe"
                />
              </div>
            </div>
            <div className="form-group">
              <label className="form-label">Username</label>
              <input
                type="text"
                name="username"
                className="form-input"
                required
                value={formData.username}
                onChange={handleChange}
                placeholder="johndoe"
              />
            </div>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input
                type="email"
                name="email"
                className="form-input"
                required
                value={formData.email}
                onChange={handleChange}
                placeholder="john@example.com"
              />
            </div>
            <div className="form-group">
              <label className="form-label">Password</label>
              <input
                type="password"
                name="password"
                className="form-input"
                required
                minLength={6}
                value={formData.password}
                onChange={handleChange}
                placeholder="••••••••"
              />
            </div>
            <button
              type="submit"
              className="gradient-btn"
              disabled={loading}
              style={{ width: '100%', marginTop: '10px' }}
            >
              {loading ? 'Creating Account...' : 'Create Account'}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}
