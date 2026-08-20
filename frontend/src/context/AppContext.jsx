import React, { createContext, useContext, useState, useEffect } from 'react';
import { userApi, notificationApi } from '../services/api';

const AppContext = createContext();

export function AppProvider({ children }) {
  // Default demo user or saved user from localStorage
  const [currentUser, setCurrentUser] = useState(() => {
    const saved = localStorage.getItem('sphere_user');
    if (saved) {
      try { return JSON.parse(saved); } catch (e) {}
    }
    return {
      id: 1,
      username: 'alex_rivers',
      firstName: 'Alex',
      lastName: 'Rivers',
      email: 'alex@example.com',
      profilePictureUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150',
      bio: 'Digital Creator & Fullstack Developer | Building the future 🚀'
    };
  });

  const [activeTab, setActiveTab] = useState('feed'); // 'feed', 'explore', 'notifications', 'profile'
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [unreadNotificationCount, setUnreadNotificationCount] = useState(0);
  const [theme, setTheme] = useState('dark');
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const [isCreatePostModalOpen, setIsCreatePostModalOpen] = useState(false);
  const [feedRefreshKey, setFeedRefreshKey] = useState(0);

  // Sync user with localStorage
  useEffect(() => {
    if (currentUser) {
      localStorage.setItem('sphere_user', JSON.stringify(currentUser));
      fetchUnreadCount();
    }
  }, [currentUser]);

  // Apply theme
  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme(prev => prev === 'dark' ? 'light' : 'dark');
  };

  const refreshFeed = () => {
    setFeedRefreshKey(prev => prev + 1);
  };

  const fetchUnreadCount = async () => {
    if (!currentUser?.id) return;
    try {
      const res = await notificationApi.getUnreadCount(currentUser.id);
      if (res && typeof res.unreadCount === 'number') {
        setUnreadNotificationCount(res.unreadCount);
      }
    } catch (e) {
      // Backend may be starting up
    }
  };

  const switchUser = (user) => {
    setCurrentUser(user);
    refreshFeed();
  };

  const viewUserProfile = (userId) => {
    setSelectedUserId(userId);
    setActiveTab('profile');
  };

  return (
    <AppContext.Provider
      value={{
        currentUser,
        setCurrentUser,
        activeTab,
        setActiveTab,
        selectedUserId,
        setSelectedUserId,
        viewUserProfile,
        unreadNotificationCount,
        setUnreadNotificationCount,
        fetchUnreadCount,
        theme,
        toggleTheme,
        isAuthModalOpen,
        setIsAuthModalOpen,
        isCreatePostModalOpen,
        setIsCreatePostModalOpen,
        feedRefreshKey,
        refreshFeed,
        switchUser
      }}
    >
      {children}
    </AppContext.Provider>
  );
}

export function useApp() {
  return useContext(AppContext);
}
