import React, { useState, useEffect } from 'react';
import { useApp } from '../context/AppContext';
import { notificationApi } from '../services/api';
import { Bell, Heart, MessageCircle, UserPlus, CheckCheck, Trash2, RefreshCw } from 'lucide-react';

const SAMPLE_NOTIFICATIONS = [
  {
    id: 1,
    recipientId: 1,
    senderId: 2,
    senderUsername: 'sarah_tech',
    senderAvatarUrl: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150',
    type: 'LIKE',
    message: 'sarah_tech liked your post',
    isRead: false,
    createdAt: new Date(Date.now() - 1800000).toISOString()
  },
  {
    id: 2,
    recipientId: 1,
    senderId: 3,
    senderUsername: 'marcus_design',
    senderAvatarUrl: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150',
    type: 'COMMENT',
    message: 'marcus_design commented: "Awesome microservices setup!"',
    isRead: false,
    createdAt: new Date(Date.now() - 3600000).toISOString()
  },
  {
    id: 3,
    recipientId: 1,
    senderId: 4,
    senderUsername: 'elena_ai',
    senderAvatarUrl: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150',
    type: 'FOLLOW',
    message: 'elena_ai started following you',
    isRead: true,
    createdAt: new Date(Date.now() - 86400000).toISOString()
  }
];

export default function NotificationsPage() {
  const { currentUser, setUnreadNotificationCount, fetchUnreadCount, viewUserProfile } = useApp();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (currentUser?.id) {
      fetchNotifications();
    }
  }, [currentUser?.id]);

  const fetchNotifications = async () => {
    try {
      setLoading(true);
      const data = await notificationApi.getNotifications(currentUser.id);
      if (Array.isArray(data) && data.length > 0) {
        setNotifications(data);
      } else {
        setNotifications(SAMPLE_NOTIFICATIONS);
      }
      fetchUnreadCount();
    } catch (e) {
      setNotifications(SAMPLE_NOTIFICATIONS);
    } finally {
      setLoading(false);
    }
  };

  const handleMarkAsRead = async (id) => {
    try {
      await notificationApi.markAsRead(id);
      setNotifications(prev => prev.map(n => n.id === id ? { ...n, isRead: true } : n));
      setUnreadNotificationCount(prev => Math.max(0, prev - 1));
    } catch (e) {
      setNotifications(prev => prev.map(n => n.id === id ? { ...n, isRead: true } : n));
    }
  };

  const handleMarkAllAsRead = async () => {
    try {
      await notificationApi.markAllAsRead(currentUser.id);
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      setUnreadNotificationCount(0);
    } catch (e) {
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      setUnreadNotificationCount(0);
    }
  };

  const handleDelete = async (id) => {
    try {
      await notificationApi.deleteNotification(id);
      setNotifications(prev => prev.filter(n => n.id !== id));
    } catch (e) {
      setNotifications(prev => prev.filter(n => n.id !== id));
    }
  };

  const getIcon = (type) => {
    switch (type) {
      case 'LIKE':
        return <Heart size={18} style={{ color: 'var(--like-color)' }} />;
      case 'COMMENT':
        return <MessageCircle size={18} style={{ color: 'var(--accent-primary)' }} />;
      case 'FOLLOW':
        return <UserPlus size={18} style={{ color: 'var(--success)' }} />;
      default:
        return <Bell size={18} style={{ color: 'var(--accent-primary)' }} />;
    }
  };

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '20px' }}>
        <h2 style={{ fontSize: '1.2rem', fontWeight: '800' }}>Your Notifications</h2>
        <div style={{ display: 'flex', gap: '8px' }}>
          <button
            className="btn-secondary"
            onClick={handleMarkAllAsRead}
            style={{ padding: '6px 12px', fontSize: '0.8rem' }}
          >
            <CheckCheck size={14} />
            <span>Mark All Read</span>
          </button>
          <button
            className="btn-secondary"
            onClick={fetchNotifications}
            disabled={loading}
            style={{ padding: '6px 10px', fontSize: '0.8rem' }}
          >
            <RefreshCw size={14} className={loading ? 'animate-spin' : ''} />
          </button>
        </div>
      </div>

      {notifications.length === 0 ? (
        <div className="glass-card" style={{ textAlign: 'center', padding: '40px' }}>
          <Bell size={32} style={{ color: 'var(--text-muted)', marginBottom: '12px' }} />
          <p style={{ color: 'var(--text-secondary)' }}>You are all caught up! No notifications.</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
          {notifications.map((notif) => (
            <div
              key={notif.id}
              className="glass-card"
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '14px',
                padding: '14px',
                borderColor: notif.isRead ? 'var(--border-color)' : 'var(--accent-primary)',
                background: notif.isRead ? 'var(--bg-card)' : 'rgba(99, 102, 241, 0.08)'
              }}
            >
              <div style={{ padding: '8px', borderRadius: 'var(--radius-full)', background: 'rgba(255,255,255,0.05)' }}>
                {getIcon(notif.type)}
              </div>

              <img
                src={notif.senderAvatarUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}
                alt={notif.senderUsername}
                className="avatar avatar-sm"
                onClick={() => notif.senderId && viewUserProfile(notif.senderId)}
                style={{ cursor: 'pointer' }}
              />

              <div style={{ flex: 1 }}>
                <p style={{ fontSize: '0.9rem', color: 'var(--text-primary)' }}>
                  {notif.message}
                </p>
                <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                  {notif.createdAt ? new Date(notif.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : 'recently'}
                </span>
              </div>

              <div style={{ display: 'flex', gap: '6px' }}>
                {!notif.isRead && (
                  <button
                    className="action-btn"
                    onClick={() => handleMarkAsRead(notif.id)}
                    title="Mark as read"
                    style={{ padding: '6px' }}
                  >
                    <CheckCheck size={16} />
                  </button>
                )}
                <button
                  className="action-btn"
                  onClick={() => handleDelete(notif.id)}
                  title="Delete notification"
                  style={{ padding: '6px', color: 'var(--text-muted)' }}
                >
                  <Trash2 size={16} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
