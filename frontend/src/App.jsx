import React, { useState, useEffect } from 'react';
import { userApi, postApi, likeApi, commentApi, notificationApi } from './services/api';

const DEMO_USERS = [
  { id: 1, username: 'alex', firstName: 'Alex', lastName: 'Rivers', email: 'alex@example.com' },
  { id: 2, username: 'sarah', firstName: 'Sarah', lastName: 'Chen', email: 'sarah@example.com' },
  { id: 3, username: 'marcus', firstName: 'Marcus', lastName: 'Vance', email: 'marcus@example.com' }
];

export default function App() {
  // If null, show Login/Register Front Page
  const [currentUser, setCurrentUser] = useState(() => {
    const saved = localStorage.getItem('sphere_current_user');
    if (saved) {
      try { return JSON.parse(saved); } catch (e) {}
    }
    return null; // Start on Login/Register page by default
  });

  // Auth Page States
  const [authMode, setAuthMode] = useState('login'); // 'login', 'register', 'demo'
  const [authForm, setAuthForm] = useState({
    username: '',
    email: '',
    password: '',
    firstName: '',
    lastName: ''
  });
  const [authError, setAuthError] = useState('');
  const [authLoading, setAuthLoading] = useState(false);

  // App Dashboard States
  const [activeTab, setActiveTab] = useState('feed'); // 'feed', 'users', 'notifications'
  const [posts, setPosts] = useState([]);
  const [users, setUsers] = useState(DEMO_USERS);
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);

  // Form states
  const [newPostContent, setNewPostContent] = useState('');
  const [newPostImage, setNewPostImage] = useState('');

  // Comment & Like & Follow states
  const [commentsState, setCommentsState] = useState({});
  const [likesState, setLikesState] = useState({});
  const [followState, setFollowState] = useState({});

  // Sync current user with localStorage
  useEffect(() => {
    if (currentUser) {
      localStorage.setItem('sphere_current_user', JSON.stringify(currentUser));
      loadAllData();
    } else {
      localStorage.removeItem('sphere_current_user');
    }
  }, [currentUser]);

  const loadAllData = async () => {
    loadPosts();
    loadUsers();
    loadNotifications();
  };

  // --- AUTHENTICATION HANDLERS ---
  const handleAuthChange = (e) => {
    setAuthForm({ ...authForm, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    if (!authForm.username || !authForm.password) {
      setAuthError('Please fill in all fields');
      return;
    }

    try {
      setAuthLoading(true);
      setAuthError('');
      const res = await userApi.login({
        usernameOrEmail: authForm.username.trim(),
        password: authForm.password
      });

      if (res && res.id) {
        setCurrentUser(res);
      } else {
        // Fallback for offline demo
        const found = users.find(u => u.username === authForm.username.trim()) || {
          id: Date.now(),
          username: authForm.username.trim(),
          firstName: authForm.username.trim(),
          email: `${authForm.username.trim()}@example.com`
        };
        setCurrentUser(found);
      }
    } catch (err) {
      setAuthError(err.message || 'Invalid credentials');
    } finally {
      setAuthLoading(false);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    if (!authForm.username || !authForm.email || !authForm.password || !authForm.firstName) {
      setAuthError('Please fill in all required fields');
      return;
    }

    try {
      setAuthLoading(true);
      setAuthError('');
      const res = await userApi.register({
        username: authForm.username.trim(),
        email: authForm.email.trim(),
        password: authForm.password,
        firstName: authForm.firstName.trim(),
        lastName: authForm.lastName.trim() || ''
      });

      if (res && res.id) {
        const newUser = {
          id: res.id,
          username: res.username,
          firstName: res.firstName,
          lastName: res.lastName,
          email: res.email
        };
        setUsers(prev => [...prev, newUser]);
        setCurrentUser(newUser);
      } else {
        const newUser = {
          id: Date.now(),
          username: authForm.username.trim(),
          firstName: authForm.firstName.trim(),
          lastName: authForm.lastName.trim() || '',
          email: authForm.email.trim()
        };
        setUsers(prev => [...prev, newUser]);
        setCurrentUser(newUser);
      }
    } catch (err) {
      setAuthError(err.message || 'Registration failed');
    } finally {
      setAuthLoading(false);
    }
  };

  const handleDemoLogin = (user) => {
    setCurrentUser(user);
  };

  const handleLogout = () => {
    setCurrentUser(null);
    setAuthMode('login');
    setAuthError('');
  };

  // --- POSTS & LIKES & COMMENTS ---
  const loadPosts = async () => {
    try {
      const data = await postApi.getAllPosts();
      if (Array.isArray(data) && data.length > 0) {
        setPosts(data);
        data.forEach(p => loadPostLikes(p.id));
      } else {
        const sample = [
          {
            id: 1,
            userId: 2,
            authorUsername: 'sarah',
            content: 'Welcome to the Microservices Social Platform! 🚀',
            mediaUrl: 'https://images.unsplash.com/photo-1518770660439-4636190af475?w=600',
            createdAt: new Date().toISOString()
          }
        ];
        setPosts(sample);
      }
    } catch (e) {
      console.warn('Could not load posts:', e.message);
    }
  };

  const handleCreatePost = async (e) => {
    e.preventDefault();
    if (!newPostContent.trim()) return;

    try {
      const newPost = await postApi.createPost({
        userId: currentUser.id,
        authorUsername: currentUser.username,
        content: newPostContent.trim(),
        mediaUrl: newPostImage.trim() || null
      });
      if (newPost) {
        setPosts([newPost, ...posts]);
      }
    } catch (e) {
      const localPost = {
        id: Date.now(),
        userId: currentUser.id,
        authorUsername: currentUser.username,
        content: newPostContent.trim(),
        mediaUrl: newPostImage.trim() || null,
        createdAt: new Date().toISOString()
      };
      setPosts([localPost, ...posts]);
    }
    setNewPostContent('');
    setNewPostImage('');
  };

  const handleDeletePost = async (postId) => {
    try {
      await postApi.deletePost(postId);
    } catch (e) {}
    setPosts(posts.filter(p => p.id !== postId));
  };

  const loadPostLikes = async (postId) => {
    if (!currentUser) return;
    try {
      const status = await likeApi.getLikeStatus(postId, currentUser.id);
      if (status) {
        setLikesState(prev => ({
          ...prev,
          [postId]: { count: status.likeCount || 0, liked: status.liked || false }
        }));
      }
    } catch (e) {}
  };

  const handleToggleLike = async (postId, postAuthorId) => {
    const current = likesState[postId] || { count: 0, liked: false };
    const newLiked = !current.liked;
    const newCount = newLiked ? current.count + 1 : Math.max(0, current.count - 1);

    setLikesState(prev => ({
      ...prev,
      [postId]: { count: newCount, liked: newLiked }
    }));

    try {
      if (newLiked) {
        await likeApi.likePost({
          postId,
          userId: currentUser.id,
          username: currentUser.username,
          postAuthorId
        });
      } else {
        await likeApi.unlikePost(postId, currentUser.id);
      }
    } catch (e) {
      console.warn('Like action error:', e.message);
    }
  };

  const toggleComments = async (postId) => {
    const current = commentsState[postId] || { list: [], open: false, text: '' };
    const willOpen = !current.open;

    setCommentsState(prev => ({
      ...prev,
      [postId]: { ...current, open: willOpen }
    }));

    if (willOpen && current.list.length === 0) {
      try {
        const list = await commentApi.getCommentsByPostId(postId);
        if (Array.isArray(list)) {
          setCommentsState(prev => ({
            ...prev,
            [postId]: { ...prev[postId], list }
          }));
        }
      } catch (e) {}
    }
  };

  const handleAddComment = async (postId, postAuthorId) => {
    const current = commentsState[postId] || { list: [], open: true, text: '' };
    if (!current.text?.trim()) return;

    const commentText = current.text.trim();
    try {
      const saved = await commentApi.addComment({
        postId,
        userId: currentUser.id,
        username: currentUser.username,
        content: commentText,
        postAuthorId
      });
      if (saved) {
        setCommentsState(prev => ({
          ...prev,
          [postId]: { ...current, list: [...current.list, saved], text: '' }
        }));
      }
    } catch (e) {
      const localComment = {
        id: Date.now(),
        postId,
        userId: currentUser.id,
        username: currentUser.username,
        content: commentText
      };
      setCommentsState(prev => ({
        ...prev,
        [postId]: { ...current, list: [...current.list, localComment], text: '' }
      }));
    }
  };

  const handleDeleteComment = async (postId, commentId) => {
    try {
      await commentApi.deleteComment(commentId);
    } catch (e) {}
    setCommentsState(prev => ({
      ...prev,
      [postId]: {
        ...prev[postId],
        list: prev[postId].list.filter(c => c.id !== commentId)
      }
    }));
  };

  // --- USERS & FOLLOW ---
  const loadUsers = async () => {
    try {
      const data = await userApi.getAllUsers();
      if (Array.isArray(data) && data.length > 0) {
        setUsers(data);
        data.forEach(u => checkFollow(u.id));
      }
    } catch (e) {}
  };

  const checkFollow = async (userId) => {
    if (!currentUser || userId === currentUser.id) return;
    try {
      const res = await userApi.isFollowing(userId, currentUser.id);
      if (res) {
        setFollowState(prev => ({ ...prev, [userId]: res.isFollowing }));
      }
    } catch (e) {}
  };

  const handleToggleFollow = async (userId) => {
    const isFollowing = followState[userId] || false;
    setFollowState(prev => ({ ...prev, [userId]: !isFollowing }));

    try {
      if (isFollowing) {
        await userApi.unfollowUser(userId, currentUser.id);
      } else {
        await userApi.followUser(userId, currentUser.id);
      }
    } catch (e) {
      console.warn('Follow error:', e.message);
    }
  };

  // --- NOTIFICATIONS ---
  const loadNotifications = async () => {
    if (!currentUser) return;
    try {
      const data = await notificationApi.getNotifications(currentUser.id);
      if (Array.isArray(data)) {
        setNotifications(data);
        setUnreadCount(data.filter(n => !n.isRead).length);
      }
    } catch (e) {}
  };

  const handleMarkAllNotifications = async () => {
    try {
      await notificationApi.markAllAsRead(currentUser.id);
    } catch (e) {}
    setNotifications(notifications.map(n => ({ ...n, isRead: true })));
    setUnreadCount(0);
  };

  // ==========================================
  // VIEW 1: LOGIN & REGISTER FRONT PAGE
  // ==========================================
  if (!currentUser) {
    return (
      <div className="auth-wrapper">
        <div className="auth-card">
          <div className="auth-header">
            <h1 className="auth-title">Sphere Social</h1>
            <p className="auth-subtitle">Microservices-Powered Social Platform</p>
          </div>

          {/* Auth Mode Tabs */}
          <div className="auth-tabs">
            <button
              type="button"
              className={`auth-tab-btn ${authMode === 'login' ? 'active' : ''}`}
              onClick={() => { setAuthMode('login'); setAuthError(''); }}
            >
              Sign In
            </button>
            <button
              type="button"
              className={`auth-tab-btn ${authMode === 'register' ? 'active' : ''}`}
              onClick={() => { setAuthMode('register'); setAuthError(''); }}
            >
              Register
            </button>
            <button
              type="button"
              className={`auth-tab-btn ${authMode === 'demo' ? 'active' : ''}`}
              onClick={() => { setAuthMode('demo'); setAuthError(''); }}
            >
              Demo Accounts
            </button>
          </div>

          {authError && <div className="alert-error">{authError}</div>}

          {/* SIGN IN FORM */}
          {authMode === 'login' && (
            <form onSubmit={handleLogin}>
              <div className="form-group">
                <label className="form-label">Username or Email</label>
                <input
                  type="text"
                  name="username"
                  className="form-input"
                  placeholder="e.g. alex"
                  value={authForm.username}
                  onChange={handleAuthChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Password</label>
                <input
                  type="password"
                  name="password"
                  className="form-input"
                  placeholder="••••••••"
                  value={authForm.password}
                  onChange={handleAuthChange}
                  required
                />
              </div>

              <button
                type="submit"
                className="btn btn-primary"
                style={{ width: '100%', marginTop: '10px' }}
                disabled={authLoading}
              >
                {authLoading ? 'Signing in...' : 'Sign In'}
              </button>
            </form>
          )}

          {/* REGISTER FORM */}
          {authMode === 'register' && (
            <form onSubmit={handleRegister}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px' }}>
                <div className="form-group">
                  <label className="form-label">First Name</label>
                  <input
                    type="text"
                    name="firstName"
                    className="form-input"
                    placeholder="John"
                    value={authForm.firstName}
                    onChange={handleAuthChange}
                    required
                  />
                </div>
                <div className="form-group">
                  <label className="form-label">Last Name</label>
                  <input
                    type="text"
                    name="lastName"
                    className="form-input"
                    placeholder="Doe"
                    value={authForm.lastName}
                    onChange={handleAuthChange}
                  />
                </div>
              </div>

              <div className="form-group">
                <label className="form-label">Username</label>
                <input
                  type="text"
                  name="username"
                  className="form-input"
                  placeholder="johndoe"
                  value={authForm.username}
                  onChange={handleAuthChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Email</label>
                <input
                  type="email"
                  name="email"
                  className="form-input"
                  placeholder="john@example.com"
                  value={authForm.email}
                  onChange={handleAuthChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Password</label>
                <input
                  type="password"
                  name="password"
                  className="form-input"
                  placeholder="••••••••"
                  value={authForm.password}
                  onChange={handleAuthChange}
                  required
                />
              </div>

              <button
                type="submit"
                className="btn btn-primary"
                style={{ width: '100%', marginTop: '10px' }}
                disabled={authLoading}
              >
                {authLoading ? 'Creating account...' : 'Create Account'}
              </button>
            </form>
          )}

          {/* QUICK DEMO USERS */}
          {authMode === 'demo' && (
            <div>
              <p style={{ fontSize: '0.85rem', color: '#94a3b8', marginBottom: '14px' }}>
                Click any profile to instantly sign in and test the platform:
              </p>
              {DEMO_USERS.map((user) => (
                <div
                  key={user.id}
                  className="demo-user-card"
                  onClick={() => handleDemoLogin(user)}
                >
                  <div>
                    <div style={{ fontWeight: '700' }}>{user.firstName} {user.lastName}</div>
                    <div style={{ fontSize: '0.8rem', color: '#94a3b8' }}>@{user.username}</div>
                  </div>
                  <button className="btn btn-primary btn-sm">Enter →</button>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    );
  }

  // ==========================================
  // VIEW 2: AUTHENTICATED SOCIAL DASHBOARD
  // ==========================================
  return (
    <div className="container">
      {/* Top Header */}
      <div className="header">
        <div className="logo">Sphere Social</div>

        <div className="user-profile-badge">
          <div style={{ textAlign: 'right' }}>
            <div style={{ fontWeight: '700', fontSize: '0.9rem' }}>{currentUser.firstName} {currentUser.lastName || ''}</div>
            <div style={{ fontSize: '0.75rem', color: '#94a3b8' }}>@{currentUser.username}</div>
          </div>
          <button className="btn btn-secondary btn-sm" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>

      {/* Tabs */}
      <div className="tabs">
        <button
          className={`tab-btn ${activeTab === 'feed' ? 'active' : ''}`}
          onClick={() => setActiveTab('feed')}
        >
          📰 Feed
        </button>
        <button
          className={`tab-btn ${activeTab === 'users' ? 'active' : ''}`}
          onClick={() => setActiveTab('users')}
        >
          👥 Users ({users.length})
        </button>
        <button
          className={`tab-btn ${activeTab === 'notifications' ? 'active' : ''}`}
          onClick={() => setActiveTab('notifications')}
        >
          🔔 Notifications {unreadCount > 0 && <span className="badge">{unreadCount}</span>}
        </button>
      </div>

      {/* TAB 1: FEED */}
      {activeTab === 'feed' && (
        <div>
          {/* Create Post Form */}
          <form onSubmit={handleCreatePost} className="card">
            <h4 style={{ marginBottom: '10px' }}>Create Post as @{currentUser.username}</h4>
            <textarea
              className="form-input"
              placeholder="What's on your mind?"
              value={newPostContent}
              onChange={(e) => setNewPostContent(e.target.value)}
              required
            />
            <input
              type="url"
              className="form-input"
              placeholder="Image URL (optional)"
              value={newPostImage}
              onChange={(e) => setNewPostImage(e.target.value)}
            />
            <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>
              Publish Post
            </button>
          </form>

          {/* Posts List */}
          <div>
            {posts.map(post => {
              const likeInfo = likesState[post.id] || { count: 0, liked: false };
              const commentInfo = commentsState[post.id] || { list: [], open: false, text: '' };

              return (
                <div key={post.id} className="card">
                  <div className="post-header">
                    <div>
                      <span className="post-author">@{post.authorUsername || `user_${post.userId}`}</span>
                      <span className="post-time" style={{ marginLeft: '8px' }}>
                        {post.createdAt ? new Date(post.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : ''}
                      </span>
                    </div>
                    {post.userId === currentUser.id && (
                      <button
                        className="btn btn-danger btn-sm"
                        onClick={() => handleDeletePost(post.id)}
                      >
                        Delete
                      </button>
                    )}
                  </div>

                  <div className="post-content">{post.content}</div>

                  {post.mediaUrl && (
                    <img
                      src={post.mediaUrl}
                      alt="post media"
                      className="post-img"
                      onError={(e) => { e.target.style.display = 'none'; }}
                    />
                  )}

                  {/* Actions */}
                  <div className="post-actions">
                    <button
                      className={`action-btn ${likeInfo.liked ? 'liked' : ''}`}
                      onClick={() => handleToggleLike(post.id, post.userId)}
                    >
                      {likeInfo.liked ? '❤️ Liked' : '🤍 Like'} ({likeInfo.count})
                    </button>

                    <button
                      className="action-btn"
                      onClick={() => toggleComments(post.id)}
                    >
                      💬 Comments ({commentInfo.list.length})
                    </button>
                  </div>

                  {/* Comments Box */}
                  {commentInfo.open && (
                    <div className="comments-box">
                      <div style={{ display: 'flex', gap: '8px', marginBottom: '10px' }}>
                        <input
                          type="text"
                          className="form-input"
                          style={{ marginBottom: 0 }}
                          placeholder="Write a comment..."
                          value={commentInfo.text || ''}
                          onChange={(e) => {
                            const val = e.target.value;
                            setCommentsState(prev => ({
                              ...prev,
                              [post.id]: { ...prev[post.id], text: val }
                            }));
                          }}
                        />
                        <button
                          className="btn btn-primary btn-sm"
                          onClick={() => handleAddComment(post.id, post.userId)}
                        >
                          Send
                        </button>
                      </div>

                      {commentInfo.list.map(c => (
                        <div key={c.id} className="comment-item">
                          <div>
                            <span className="comment-author">@{c.username}:</span>
                            <span className="comment-text">{c.content}</span>
                          </div>
                          {(c.userId === currentUser.id || post.userId === currentUser.id) && (
                            <button
                              className="btn btn-danger btn-sm"
                              style={{ padding: '2px 6px', fontSize: '0.7rem' }}
                              onClick={() => handleDeleteComment(post.id, c.id)}
                            >
                              ✕
                            </button>
                          )}
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </div>
      )}

      {/* TAB 2: USERS */}
      {activeTab === 'users' && (
        <div>
          <h3 style={{ marginBottom: '16px' }}>All Users</h3>
          {users.map(u => {
            const isSelf = u.id === currentUser.id;
            const isFollowing = followState[u.id] || false;

            return (
              <div key={u.id} className="user-item">
                <div>
                  <div style={{ fontWeight: '700' }}>{u.firstName ? `${u.firstName} ${u.lastName || ''}` : u.username}</div>
                  <div style={{ fontSize: '0.8rem', color: '#94a3b8' }}>@{u.username}</div>
                </div>

                {isSelf ? (
                  <span style={{ fontSize: '0.8rem', color: '#818cf8', fontWeight: 'bold' }}>You</span>
                ) : (
                  <button
                    className={`btn ${isFollowing ? 'btn-secondary' : 'btn-primary'} btn-sm`}
                    onClick={() => handleToggleFollow(u.id)}
                  >
                    {isFollowing ? '✓ Following' : '+ Follow'}
                  </button>
                )}
              </div>
            );
          })}
        </div>
      )}

      {/* TAB 3: NOTIFICATIONS */}
      {activeTab === 'notifications' && (
        <div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
            <h3>Notifications for @{currentUser.username}</h3>
            {notifications.length > 0 && (
              <button className="btn btn-secondary btn-sm" onClick={handleMarkAllNotifications}>
                Mark All Read
              </button>
            )}
          </div>

          {notifications.length === 0 ? (
            <div className="card" style={{ textAlign: 'center', color: '#94a3b8' }}>
              No notifications yet.
            </div>
          ) : (
            notifications.map(n => (
              <div key={n.id} className={`notif-item ${!n.isRead ? 'unread' : ''}`}>
                <div>
                  <div style={{ fontSize: '0.9rem' }}>{n.message}</div>
                  <div style={{ fontSize: '0.75rem', color: '#94a3b8' }}>
                    {n.createdAt ? new Date(n.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : ''}
                  </div>
                </div>
                {!n.isRead && <span className="badge">New</span>}
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
}
