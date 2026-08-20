import React, { useState, useEffect } from 'react';
import { useApp } from '../context/AppContext';
import { userApi } from '../services/api';
import { UserCheck, UserPlus } from 'lucide-react';

export default function UserCard({ user, onFollowChange }) {
  const { currentUser, setIsAuthModalOpen, viewUserProfile } = useApp();
  const [isFollowing, setIsFollowing] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    checkFollowStatus();
  }, [user.id, currentUser?.id]);

  const checkFollowStatus = async () => {
    if (!currentUser?.id || !user?.id || currentUser.id === user.id) return;
    try {
      const res = await userApi.isFollowing(user.id, currentUser.id);
      if (res && typeof res.isFollowing === 'boolean') {
        setIsFollowing(res.isFollowing);
      }
    } catch (e) {
      // Backend may be starting
    }
  };

  const handleFollowToggle = async (e) => {
    e.stopPropagation();
    if (!currentUser) {
      setIsAuthModalOpen(true);
      return;
    }
    if (loading) return;

    setLoading(true);
    const previousState = isFollowing;
    setIsFollowing(!previousState);

    try {
      if (previousState) {
        await userApi.unfollowUser(user.id, currentUser.id);
      } else {
        await userApi.followUser(user.id, currentUser.id);
      }
      if (onFollowChange) onFollowChange();
    } catch (err) {
      setIsFollowing(previousState);
      alert(err.message || 'Follow action failed');
    } finally {
      setLoading(false);
    }
  };

  const isSelf = currentUser && currentUser.id === user.id;

  return (
    <div
      className="glass-card"
      onClick={() => viewUserProfile(user.id)}
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        cursor: 'pointer',
        padding: '16px',
        marginBottom: '12px'
      }}
    >
      <div style={{ display: 'flex', alignItems: 'center', gap: '14px', flex: 1, overflow: 'hidden' }}>
        <img
          src={user.profilePictureUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}
          alt={user.username}
          className="avatar"
        />
        <div style={{ overflow: 'hidden' }}>
          <div style={{ fontWeight: '700', fontSize: '0.95rem' }}>
            {user.firstName ? `${user.firstName} ${user.lastName}` : user.username}
          </div>
          <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
            @{user.username}
          </div>
          {user.bio && (
            <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginTop: '4px', textOverflow: 'ellipsis', overflow: 'hidden', whiteSpace: 'nowrap' }}>
              {user.bio}
            </div>
          )}
        </div>
      </div>

      {!isSelf && (
        <button
          className={isFollowing ? "btn-secondary" : "gradient-btn"}
          onClick={handleFollowToggle}
          disabled={loading}
          style={{ padding: '6px 14px', fontSize: '0.85rem' }}
        >
          {isFollowing ? (
            <>
              <UserCheck size={14} /> Following
            </>
          ) : (
            <>
              <UserPlus size={14} /> Follow
            </>
          )}
        </button>
      )}
    </div>
  );
}
