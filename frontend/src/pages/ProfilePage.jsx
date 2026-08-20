import React, { useState, useEffect } from 'react';
import { useApp } from '../context/AppContext';
import { userApi, postApi } from '../services/api';
import PostCard from '../components/PostCard';
import { Edit3, UserCheck, UserPlus, MapPin, Globe, Calendar, Image, Check } from 'lucide-react';

export default function ProfilePage() {
  const { currentUser, selectedUserId, setCurrentUser } = useApp();
  const targetUserId = selectedUserId || currentUser?.id;

  const [user, setUser] = useState(null);
  const [posts, setPosts] = useState([]);
  const [isFollowing, setIsFollowing] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({
    firstName: '',
    lastName: '',
    bio: '',
    website: '',
    location: '',
    profilePictureUrl: ''
  });
  const [saving, setSaving] = useState(false);
  const [loading, setLoading] = useState(false);

  const isSelf = currentUser && currentUser.id === targetUserId;

  useEffect(() => {
    if (targetUserId) {
      fetchUserData();
      fetchUserPosts();
      if (!isSelf && currentUser) {
        checkFollowStatus();
      }
    }
  }, [targetUserId, currentUser?.id]);

  const fetchUserData = async () => {
    try {
      setLoading(true);
      const data = await userApi.getUserById(targetUserId);
      if (data) {
        setUser(data);
        setEditForm({
          firstName: data.firstName || '',
          lastName: data.lastName || '',
          bio: data.bio || '',
          website: data.website || '',
          location: data.location || '',
          profilePictureUrl: data.profilePictureUrl || ''
        });
      }
    } catch (e) {
      if (isSelf && currentUser) {
        setUser(currentUser);
        setEditForm({
          firstName: currentUser.firstName || '',
          lastName: currentUser.lastName || '',
          bio: currentUser.bio || '',
          website: '',
          location: '',
          profilePictureUrl: currentUser.profilePictureUrl || ''
        });
      }
    } finally {
      setLoading(false);
    }
  };

  const fetchUserPosts = async () => {
    try {
      const data = await postApi.getUserPosts(targetUserId);
      if (Array.isArray(data)) {
        setPosts(data);
      }
    } catch (e) {
      console.warn('Could not fetch user posts:', e);
    }
  };

  const checkFollowStatus = async () => {
    try {
      const res = await userApi.isFollowing(targetUserId, currentUser.id);
      if (res) setIsFollowing(res.isFollowing);
    } catch (e) {}
  };

  const handleFollowToggle = async () => {
    const previous = isFollowing;
    setIsFollowing(!previous);
    try {
      if (previous) {
        await userApi.unfollowUser(targetUserId, currentUser.id);
      } else {
        await userApi.followUser(targetUserId, currentUser.id);
      }
      fetchUserData();
    } catch (e) {
      setIsFollowing(previous);
    }
  };

  const handleSaveProfile = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      const updated = await userApi.updateProfile(currentUser.id, editForm);
      if (updated) {
        setUser(updated);
        setCurrentUser(prev => ({ ...prev, ...updated }));
      }
      setIsEditing(false);
    } catch (err) {
      alert(err.message || 'Failed to update profile');
    } finally {
      setSaving(false);
    }
  };

  if (!user) {
    return (
      <div className="glass-card" style={{ textAlign: 'center', padding: '40px' }}>
        <p style={{ color: 'var(--text-muted)' }}>Loading user profile...</p>
      </div>
    );
  }

  return (
    <div>
      {/* Profile Header Card */}
      <div className="glass-card" style={{ marginBottom: '24px', position: 'relative', overflow: 'hidden' }}>
        {/* Banner */}
        <div style={{
          height: '140px',
          margin: '-20px -20px 0 -20px',
          background: 'var(--accent-gradient)',
          opacity: 0.85
        }} />

        {/* Profile Details */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginTop: '-50px', marginBottom: '16px' }}>
          <img
            src={user.profilePictureUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}
            alt={user.username}
            className="avatar avatar-lg"
            style={{ border: '4px solid var(--bg-secondary)', background: 'var(--bg-secondary)' }}
          />

          <div>
            {isSelf ? (
              <button
                className="btn-secondary"
                onClick={() => setIsEditing(!isEditing)}
              >
                <Edit3 size={16} />
                <span>{isEditing ? 'Cancel' : 'Edit Profile'}</span>
              </button>
            ) : (
              <button
                className={isFollowing ? "btn-secondary" : "gradient-btn"}
                onClick={handleFollowToggle}
              >
                {isFollowing ? (
                  <>
                    <UserCheck size={16} /> Following
                  </>
                ) : (
                  <>
                    <UserPlus size={16} /> Follow
                  </>
                )}
              </button>
            )}
          </div>
        </div>

        {/* Name and Bio */}
        <div style={{ marginBottom: '16px' }}>
          <h2 style={{ fontSize: '1.4rem', fontWeight: '800' }}>
            {user.firstName ? `${user.firstName} ${user.lastName}` : user.username}
          </h2>
          <div style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>
            @{user.username}
          </div>
          {user.bio && (
            <p style={{ marginTop: '10px', fontSize: '0.95rem', lineHeight: '1.5' }}>
              {user.bio}
            </p>
          )}
        </div>

        {/* Stats */}
        <div style={{ display: 'flex', gap: '24px', paddingTop: '14px', borderTop: '1px solid var(--border-color)', fontSize: '0.9rem' }}>
          <div>
            <strong style={{ color: 'var(--text-primary)' }}>{user.followerCount || 0}</strong> <span style={{ color: 'var(--text-muted)' }}>Followers</span>
          </div>
          <div>
            <strong style={{ color: 'var(--text-primary)' }}>{user.followingCount || 0}</strong> <span style={{ color: 'var(--text-muted)' }}>Following</span>
          </div>
          <div>
            <strong style={{ color: 'var(--text-primary)' }}>{posts.length}</strong> <span style={{ color: 'var(--text-muted)' }}>Posts</span>
          </div>
        </div>
      </div>

      {/* Edit Profile Form Modal / Drawer */}
      {isEditing && (
        <form onSubmit={handleSaveProfile} className="glass-card" style={{ marginBottom: '24px' }}>
          <h3 style={{ fontSize: '1.1rem', marginBottom: '16px' }}>Edit Profile Information</h3>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', marginBottom: '12px' }}>
            <div className="form-group">
              <label className="form-label">First Name</label>
              <input
                type="text"
                className="form-input"
                value={editForm.firstName}
                onChange={(e) => setEditForm({ ...editForm, firstName: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label className="form-label">Last Name</label>
              <input
                type="text"
                className="form-input"
                value={editForm.lastName}
                onChange={(e) => setEditForm({ ...editForm, lastName: e.target.value })}
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Bio</label>
            <textarea
              className="form-input"
              rows={2}
              value={editForm.bio}
              onChange={(e) => setEditForm({ ...editForm, bio: e.target.value })}
            />
          </div>

          <div className="form-group">
            <label className="form-label">Avatar Image URL</label>
            <input
              type="url"
              className="form-input"
              value={editForm.profilePictureUrl}
              onChange={(e) => setEditForm({ ...editForm, profilePictureUrl: e.target.value })}
            />
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px' }}>
            <button type="button" className="btn-secondary" onClick={() => setIsEditing(false)}>
              Cancel
            </button>
            <button type="submit" className="gradient-btn" disabled={saving}>
              <Check size={16} />
              <span>{saving ? 'Saving...' : 'Save Changes'}</span>
            </button>
          </div>
        </form>
      )}

      {/* User's Posts */}
      <h3 style={{ fontSize: '1.1rem', marginBottom: '16px', fontWeight: '700' }}>Posts</h3>
      {posts.length === 0 ? (
        <div className="glass-card" style={{ textAlign: 'center', padding: '32px' }}>
          <p style={{ color: 'var(--text-muted)' }}>No posts published yet.</p>
        </div>
      ) : (
        posts.map((post) => (
          <PostCard
            key={post.id}
            post={post}
            onDelete={(id) => setPosts(prev => prev.filter(p => p.id !== id))}
          />
        ))
      )}
    </div>
  );
}
