import React, { useState, useEffect } from 'react';
import { userApi, postApi, likeApi, commentApi, notificationApi } from './services/api';

const DEFAULT_USERS = [
  { id: 1, username: 'alex', firstName: 'Alex', lastName: 'Rivers' },
  { id: 2, username: 'sarah', firstName: 'Sarah', lastName: 'Chen' },
  { id: 3, username: 'marcus', firstName: 'Marcus', lastName: 'Vance' }
];

export default function App() {
  const [currentUser, setCurrentUser] = useState(DEFAULT_USERS[0]);
  const [activeTab, setActiveTab] = useState('feed'); // 'feed', 'users', 'notifications'

  // Data states
  const [posts, setPosts] = useState([]);
  const [users, setUsers] = useState(DEFAULT_USERS);
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);

  // Form states
  const [newPostContent, setNewPostContent] = useState('');
  const [newPostImage, setNewPostImage] = useState('');
  const [newUsername, setNewUsername] = useState('');
  const [newFullName, setNewFullName] = useState('');
  const [showNewUserForm, setShowNewUserForm] = useState(false);

  // Comment states per post: { [postId]: { list: [], open: false, text: '' } }
  const [commentsState, setCommentsState] = useState({});
  // Like states per post: { [postId]: { count: 0, liked: false } }
  const [likesState, setLikesState] = useState({});
  // Follow states per user: { [userId]: boolean }
  const [followState, setFollowState] = useState({});

  useEffect(() => {
    loadAllData();
  }, [currentUser.id]);

  const loadAllData = async () => {
    loadPosts();
    loadUsers();
    loadNotifications();
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
      console.warn('Could not load posts from API Gateway:', e.message);
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
    if (userId === currentUser.id) return;
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

  const handleRegisterUser = async (e) => {
    e.preventDefault();
    if (!newUsername.trim()) return;

    try {
      const res = await userApi.register({
        username: newUsername.trim(),
        email: `${newUsername.trim()}@example.com`,
        password: 'password123',
        firstName: newFullName.trim() || newUsername.trim(),
        lastName: 'User'
      });
      if (res) {
        const newUser = { id: res.id, username: res.username, firstName: res.firstName, lastName: res.lastName };
        setUsers([...users, newUser]);
        setCurrentUser(newUser);
      }
    } catch (e) {
      const newUser = { id: Date.now(), username: newUsername.trim(), firstName: newFullName.trim() || newUsername.trim() };
      setUsers([...users, newUser]);
      setCurrentUser(newUser);
    }
    setNewUsername('');
    setNewFullName('');
    setShowNewUserForm(false);
  };

  // --- NOTIFICATIONS ---
  const loadNotifications = async () => {
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

  return (
    <div className="container">
      {/* Top Header */}
      <div className="header">
        <div className="logo">Sphere Social</div>

        <div className="user-selector">
          <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>Active User:</span>
          <select
            className="form-input"
            style={{ width: 'auto', marginBottom: 0, padding: '4px 8px' }}
            value={currentUser.id}
            onChange={(e) => {
              const selected = users.find(u => u.id === Number(e.target.value)) || currentUser;
              setCurrentUser(selected);
            }}
          >
            {users.map(u => (
              <option key={u.id} value={u.id}>@{u.username}</option>
            ))}
          </select>
          <button
            className="btn btn-secondary btn-sm"
            onClick={() => setShowNewUserForm(!showNewUserForm)}
          >
            + New
          </button>
        </div>
      </div>

      {/* New User Register Form */}
      {showNewUserForm && (
        <form onSubmit={handleRegisterUser} className="card" style={{ marginBottom: '16px' }}>
          <h4 style={{ marginBottom: '10px' }}>Add / Register New User</h4>
          <input
            type="text"
            className="form-input"
            placeholder="Username (e.g. john)"
            value={newUsername}
            onChange={(e) => setNewUsername(e.target.value)}
            required
          />
          <input
            type="text"
            className="form-input"
            placeholder="Full Name (e.g. John Doe)"
            value={newFullName}
            onChange={(e) => setNewFullName(e.target.value)}
          />
          <div style={{ display: 'flex', gap: '8px' }}>
            <button type="submit" className="btn btn-primary btn-sm">Create User</button>
            <button type="button" className="btn btn-secondary btn-sm" onClick={() => setShowNewUserForm(false)}>Cancel</button>
          </div>
        </form>
      )}

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
