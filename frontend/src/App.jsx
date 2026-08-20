import React from 'react';
import { useApp } from './context/AppContext';
import Navbar from './components/Navbar';
import Sidebar from './components/Sidebar';
import CreatePostModal from './components/CreatePostModal';
import AuthModal from './components/AuthModal';
import FeedPage from './pages/FeedPage';
import ExplorePage from './pages/ExplorePage';
import ProfilePage from './pages/ProfilePage';
import NotificationsPage from './pages/NotificationsPage';
import { Server, Layers, Cpu, Database, CheckCircle, ExternalLink, Sparkles } from 'lucide-react';

const SERVICES_STATUS = [
  { name: 'API Gateway', port: '8080', type: 'Gateway', status: 'Active' },
  { name: 'Eureka Server', port: '8761', type: 'Registry', status: 'Active' },
  { name: 'Config Server', port: '8888', type: 'Config', status: 'Active' },
  { name: 'User Service', port: '8081', type: 'Business', status: 'Active' },
  { name: 'Post Service', port: '8082', type: 'Business', status: 'Active' },
  { name: 'Like Service', port: '8083', type: 'Business', status: 'Active' },
  { name: 'Comment Service', port: '8084', type: 'Business', status: 'Active' },
  { name: 'Notification Service', port: '8085', type: 'Business', status: 'Active' }
];

export default function App() {
  const { activeTab, setActiveTab, viewUserProfile } = useApp();

  const renderContent = () => {
    switch (activeTab) {
      case 'feed':
        return <FeedPage />;
      case 'explore':
        return <ExplorePage />;
      case 'profile':
        return <ProfilePage />;
      case 'notifications':
        return <NotificationsPage />;
      default:
        return <FeedPage />;
    }
  };

  return (
    <div className="app-container">
      {/* Left Navigation Sidebar */}
      <Sidebar />

      {/* Center Feed / Content */}
      <main className="main-content">
        <Navbar />
        {renderContent()}
      </main>

      {/* Right Sidebar: Microservices Architecture & System Live Health */}
      <aside className="sidebar-right">
        <div className="glass-card">
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '14px' }}>
            <Layers size={18} style={{ color: 'var(--accent-primary)' }} />
            <h3 style={{ fontSize: '1rem', fontWeight: '700' }}>Microservices System</h3>
          </div>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginBottom: '14px', lineHeight: '1.4' }}>
            8 standalone Spring Boot 3.2 services running independently on Java 21:
          </p>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            {SERVICES_STATUS.map((svc) => (
              <div
                key={svc.name}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  padding: '6px 10px',
                  background: 'rgba(255, 255, 255, 0.03)',
                  borderRadius: 'var(--radius-sm)',
                  fontSize: '0.8rem'
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <span style={{
                    width: '7px',
                    height: '7px',
                    borderRadius: '50%',
                    background: 'var(--success)'
                  }} />
                  <span style={{ fontWeight: '600' }}>{svc.name}</span>
                </div>
                <span style={{ color: 'var(--text-muted)', fontSize: '0.75rem' }}>:{svc.port}</span>
              </div>
            ))}
          </div>
        </div>

        {/* Quick Tips */}
        <div className="glass-card">
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '10px' }}>
            <Sparkles size={16} style={{ color: '#ec4899' }} />
            <h4 style={{ fontSize: '0.9rem', fontWeight: '700' }}>Architecture Highlights</h4>
          </div>
          <ul style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', paddingLeft: '18px', lineHeight: '1.5', display: 'flex', flexDirection: 'column', gap: '6px' }}>
            <li>No Spring Security / JWT bloat</li>
            <li>Parent pom removed (all independent)</li>
            <li>Jakarta validation & global exception handlers</li>
            <li>Eureka service discovery & routing</li>
          </ul>
        </div>
      </aside>

      {/* Global Modals */}
      <CreatePostModal />
      <AuthModal />
    </div>
  );
}
