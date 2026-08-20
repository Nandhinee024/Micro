import React from 'react';
import { useApp } from '../context/AppContext';
import { Home, Compass, Bell, User, PlusCircle, LogIn, Moon, Sun } from 'lucide-react';

export default function Sidebar() {
  const {
    activeTab,
    setActiveTab,
    unreadNotificationCount,
    currentUser,
    setSelectedUserId,
    setIsCreatePostModalOpen,
    setIsAuthModalOpen,
    theme,
    toggleTheme
  } = useApp();

  const handleProfileClick = () => {
    if (currentUser) {
      setSelectedUserId(currentUser.id);
      setActiveTab('profile');
    } else {
      setIsAuthModalOpen(true);
    }
  };

  return (
    <aside className="sidebar-left">
      <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '32px', paddingLeft: '8px' }}>
        <div style={{
          width: '36px',
          height: '36px',
          borderRadius: '10px',
          background: 'var(--accent-gradient)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: 'white',
          fontWeight: 'bold',
          fontSize: '1.2rem'
        }}>
          S
        </div>
        <h2 style={{ fontSize: '1.5rem', fontWeight: '800', letterSpacing: '-0.03em' }}>
          Sphere<span style={{ color: 'var(--accent-primary)' }}>.</span>
        </h2>
      </div>

      <nav style={{ display: 'flex', flexDirection: 'column', gap: '8px', flex: 1 }}>
        <button
          className="btn-secondary"
          onClick={() => { setActiveTab('feed'); setSelectedUserId(null); }}
          style={{
            justifyContent: 'flex-start',
            padding: '12px 16px',
            fontSize: '1rem',
            background: activeTab === 'feed' ? 'var(--accent-gradient)' : 'transparent',
            color: activeTab === 'feed' ? 'white' : 'var(--text-primary)',
            border: activeTab === 'feed' ? 'none' : '1px solid transparent'
          }}
        >
          <Home size={20} />
          <span>Home Feed</span>
        </button>

        <button
          className="btn-secondary"
          onClick={() => setActiveTab('explore')}
          style={{
            justifyContent: 'flex-start',
            padding: '12px 16px',
            fontSize: '1rem',
            background: activeTab === 'explore' ? 'var(--accent-gradient)' : 'transparent',
            color: activeTab === 'explore' ? 'white' : 'var(--text-primary)',
            border: activeTab === 'explore' ? 'none' : '1px solid transparent'
          }}
        >
          <Compass size={20} />
          <span>Explore Users</span>
        </button>

        <button
          className="btn-secondary"
          onClick={() => setActiveTab('notifications')}
          style={{
            justifyContent: 'flex-start',
            padding: '12px 16px',
            fontSize: '1rem',
            position: 'relative',
            background: activeTab === 'notifications' ? 'var(--accent-gradient)' : 'transparent',
            color: activeTab === 'notifications' ? 'white' : 'var(--text-primary)',
            border: activeTab === 'notifications' ? 'none' : '1px solid transparent'
          }}
        >
          <Bell size={20} />
          <span>Notifications</span>
          {unreadNotificationCount > 0 && (
            <span className="badge" style={{ marginLeft: 'auto' }}>
              {unreadNotificationCount}
            </span>
          )}
        </button>

        <button
          className="btn-secondary"
          onClick={handleProfileClick}
          style={{
            justifyContent: 'flex-start',
            padding: '12px 16px',
            fontSize: '1rem',
            background: activeTab === 'profile' ? 'var(--accent-gradient)' : 'transparent',
            color: activeTab === 'profile' ? 'white' : 'var(--text-primary)',
            border: activeTab === 'profile' ? 'none' : '1px solid transparent'
          }}
        >
          <User size={20} />
          <span>My Profile</span>
        </button>

        <button
          className="gradient-btn"
          onClick={() => setIsCreatePostModalOpen(true)}
          style={{ marginTop: '16px', padding: '14px', width: '100%' }}
        >
          <PlusCircle size={20} />
          <span>Create Post</span>
        </button>
      </nav>

      {/* User profile bar & theme toggle at bottom */}
      <div style={{ paddingTop: '16px', borderTop: '1px solid var(--border-color)', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        {currentUser ? (
          <div
            onClick={handleProfileClick}
            style={{ display: 'flex', alignItems: 'center', gap: '10px', cursor: 'pointer', flex: 1, overflow: 'hidden' }}
          >
            <img src={currentUser.profilePictureUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'} alt={currentUser.username} className="avatar avatar-sm" />
            <div style={{ overflow: 'hidden' }}>
              <div style={{ fontWeight: '600', fontSize: '0.85rem', textOverflow: 'ellipsis', whiteSpace: 'nowrap', overflow: 'hidden' }}>
                {currentUser.firstName ? `${currentUser.firstName} ${currentUser.lastName}` : currentUser.username}
              </div>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>@{currentUser.username}</div>
            </div>
          </div>
        ) : (
          <button className="btn-secondary" onClick={() => setIsAuthModalOpen(true)}>
            <LogIn size={16} /> Login
          </button>
        )}

        <div style={{ display: 'flex', gap: '6px' }}>
          <button
            className="action-btn"
            onClick={toggleTheme}
            title="Toggle theme"
            style={{ padding: '8px' }}
          >
            {theme === 'dark' ? <Sun size={18} /> : <Moon size={18} />}
          </button>
          <button
            className="action-btn"
            onClick={() => setIsAuthModalOpen(true)}
            title="Switch or register user"
            style={{ padding: '8px' }}
          >
            <LogIn size={18} />
          </button>
        </div>
      </div>
    </aside>
  );
}
