import React from 'react';
import { useApp } from '../context/AppContext';
import { Sparkles, Bell, User, PlusCircle } from 'lucide-react';

export default function Navbar() {
  const {
    activeTab,
    unreadNotificationCount,
    setActiveTab,
    setIsCreatePostModalOpen,
    setIsAuthModalOpen,
    currentUser
  } = useApp();

  const getTitle = () => {
    switch (activeTab) {
      case 'feed': return 'Home Feed';
      case 'explore': return 'Explore Creators';
      case 'notifications': return 'Notifications';
      case 'profile': return 'User Profile';
      default: return 'Sphere';
    }
  };

  return (
    <header style={{
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginBottom: '24px',
      paddingBottom: '16px',
      borderBottom: '1px solid var(--border-color)'
    }}>
      <div>
        <h1 style={{ fontSize: '1.6rem', fontWeight: '800' }}>
          {getTitle()}
        </h1>
        <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
          Powered by 5 microservices & Spring Cloud Gateway
        </p>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
        <button
          className="gradient-btn"
          onClick={() => setIsCreatePostModalOpen(true)}
          style={{ padding: '8px 14px', fontSize: '0.85rem' }}
        >
          <PlusCircle size={16} />
          <span>New Post</span>
        </button>

        <button
          className="btn-secondary"
          onClick={() => setIsAuthModalOpen(true)}
          style={{ padding: '8px 12px', fontSize: '0.85rem' }}
          title="Switch active user"
        >
          <User size={16} />
          <span>{currentUser ? `@${currentUser.username}` : 'Login'}</span>
        </button>
      </div>
    </header>
  );
}
