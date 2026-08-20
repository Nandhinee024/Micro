import React, { useState, useEffect } from 'react';
import { useApp } from '../context/AppContext';
import { likeApi, postApi } from '../services/api';
import CommentSection from './CommentSection';
import { Heart, MessageCircle, Share2, Trash2, MoreHorizontal } from 'lucide-react';

export default function PostCard({ post, onDelete }) {
  const { currentUser, setIsAuthModalOpen, viewUserProfile } = useApp();
  const [liked, setLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(post.likeCount || 0);
  const [commentCount, setCommentCount] = useState(post.commentCount || 0);
  const [showComments, setShowComments] = useState(false);
  const [isLiking, setIsLiking] = useState(false);

  useEffect(() => {
    checkLikeStatus();
  }, [post.id, currentUser?.id]);

  const checkLikeStatus = async () => {
    if (!currentUser?.id || !post?.id) return;
    try {
      const res = await likeApi.getLikeStatus(post.id, currentUser.id);
      if (res) {
        setLiked(res.liked);
        if (typeof res.likeCount === 'number') {
          setLikeCount(res.likeCount);
        }
      }
    } catch (e) {
      // Backend may be starting
    }
  };

  const handleLikeToggle = async () => {
    if (!currentUser) {
      setIsAuthModalOpen(true);
      return;
    }
    if (isLiking) return;

    setIsLiking(true);
    const previousLiked = liked;
    const previousCount = likeCount;

    // Optimistic update
    setLiked(!previousLiked);
    setLikeCount(previousLiked ? Math.max(0, previousCount - 1) : previousCount + 1);

    try {
      if (previousLiked) {
        await likeApi.unlikePost(post.id, currentUser.id);
      } else {
        await likeApi.likePost({
          postId: post.id,
          userId: currentUser.id,
          username: currentUser.username,
          postAuthorId: post.userId
        });
      }
    } catch (err) {
      // Revert on error
      setLiked(previousLiked);
      setLikeCount(previousCount);
      console.warn('Like toggle error:', err);
    } finally {
      setIsLiking(false);
    }
  };

  const handleDeletePost = async () => {
    if (!window.confirm('Are you sure you want to delete this post?')) return;
    try {
      await postApi.deletePost(post.id);
      if (onDelete) {
        onDelete(post.id);
      }
    } catch (err) {
      alert(err.message || 'Failed to delete post');
    }
  };

  const formatTime = (dateString) => {
    if (!dateString) return 'recently';
    const date = new Date(dateString);
    const now = new Date();
    const diffSec = Math.floor((now - date) / 1000);

    if (diffSec < 60) return 'just now';
    if (diffSec < 3600) return `${Math.floor(diffSec / 60)}m ago`;
    if (diffSec < 86400) return `${Math.floor(diffSec / 3600)}h ago`;
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
  };

  return (
    <div className="glass-card post-card animate-fade">
      {/* Header */}
      <div className="post-header">
        <div className="post-user-info" onClick={() => viewUserProfile(post.userId)}>
          <img
            src={post.authorAvatarUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}
            alt={post.authorUsername}
            className="avatar"
          />
          <div>
            <div className="post-username">@{post.authorUsername || `user_${post.userId}`}</div>
            <div className="post-time">{formatTime(post.createdAt)}</div>
          </div>
        </div>

        {currentUser && currentUser.id === post.userId && (
          <button
            className="action-btn"
            onClick={handleDeletePost}
            title="Delete post"
            style={{ color: 'var(--text-muted)' }}
          >
            <Trash2 size={16} />
          </button>
        )}
      </div>

      {/* Content */}
      <div className="post-content">
        {post.content}
      </div>

      {/* Media Image */}
      {post.mediaUrl && (
        <img
          src={post.mediaUrl}
          alt="post media"
          className="post-media"
          onError={(e) => { e.target.style.display = 'none'; }}
        />
      )}

      {/* Actions */}
      <div className="post-actions">
        <button
          className={`action-btn ${liked ? 'liked' : ''}`}
          onClick={handleLikeToggle}
          title={liked ? 'Unlike' : 'Like'}
        >
          <Heart size={18} fill={liked ? 'var(--like-color)' : 'none'} />
          <span>{likeCount}</span>
        </button>

        <button
          className="action-btn"
          onClick={() => setShowComments(!showComments)}
          title="Comments"
        >
          <MessageCircle size={18} />
          <span>{commentCount}</span>
        </button>

        <button
          className="action-btn"
          onClick={() => {
            navigator.clipboard.writeText(window.location.href);
            alert('Post link copied to clipboard!');
          }}
          title="Share"
        >
          <Share2 size={18} />
          <span>Share</span>
        </button>
      </div>

      {/* Comments Drawer */}
      {showComments && (
        <CommentSection
          postId={post.id}
          postAuthorId={post.userId}
          onCommentCountChange={(newCount) => setCommentCount(newCount)}
        />
      )}
    </div>
  );
}
