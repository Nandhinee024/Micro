import React, { useState, useEffect } from 'react';
import { commentApi } from '../services/api';
import { useApp } from '../context/AppContext';
import { Send, Trash2 } from 'lucide-react';

export default function CommentSection({ postId, postAuthorId, onCommentCountChange }) {
  const { currentUser, setIsAuthModalOpen, viewUserProfile } = useApp();
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    fetchComments();
  }, [postId]);

  const fetchComments = async () => {
    try {
      setLoading(true);
      const data = await commentApi.getCommentsByPostId(postId);
      if (Array.isArray(data)) {
        setComments(data);
        if (onCommentCountChange) {
          onCommentCountChange(data.length);
        }
      }
    } catch (e) {
      console.warn('Could not fetch comments:', e);
    } finally {
      setLoading(false);
    }
  };

  const handleAddComment = async (e) => {
    e.preventDefault();
    if (!currentUser) {
      setIsAuthModalOpen(true);
      return;
    }
    if (!newComment.trim()) return;

    try {
      setSubmitting(true);
      const commentData = {
        postId,
        userId: currentUser.id,
        username: currentUser.username,
        userAvatarUrl: currentUser.profilePictureUrl,
        content: newComment.trim(),
        postAuthorId
      };
      const created = await commentApi.addComment(commentData);
      if (created) {
        setComments(prev => [...prev, created]);
        setNewComment('');
        if (onCommentCountChange) {
          onCommentCountChange(comments.length + 1);
        }
      }
    } catch (err) {
      alert(err.message || 'Failed to add comment');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteComment = async (commentId) => {
    try {
      await commentApi.deleteComment(commentId);
      setComments(prev => prev.filter(c => c.id !== commentId));
      if (onCommentCountChange) {
        onCommentCountChange(Math.max(0, comments.length - 1));
      }
    } catch (err) {
      alert(err.message || 'Failed to delete comment');
    }
  };

  return (
    <div style={{
      marginTop: '16px',
      paddingTop: '16px',
      borderTop: '1px solid var(--border-color)',
      animation: 'fadeIn 0.2s ease'
    }}>
      {/* Input form */}
      <form onSubmit={handleAddComment} style={{ display: 'flex', gap: '10px', marginBottom: '16px' }}>
        <img
          src={currentUser?.profilePictureUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}
          alt="me"
          className="avatar avatar-sm"
        />
        <input
          type="text"
          className="form-input"
          placeholder={currentUser ? "Write a comment..." : "Login to comment..."}
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          disabled={submitting}
          style={{ flex: 1, padding: '8px 12px', fontSize: '0.9rem' }}
        />
        <button
          type="submit"
          className="gradient-btn"
          disabled={submitting || !newComment.trim()}
          style={{ padding: '8px 14px', borderRadius: 'var(--radius-md)' }}
        >
          <Send size={16} />
        </button>
      </form>

      {/* Comment List */}
      {loading ? (
        <div style={{ fontSize: '0.85rem', color: 'var(--text-muted)', textAlign: 'center', padding: '8px' }}>
          Loading comments...
        </div>
      ) : comments.length === 0 ? (
        <div style={{ fontSize: '0.85rem', color: 'var(--text-muted)', textAlign: 'center', padding: '8px' }}>
          No comments yet. Be the first to share your thoughts!
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {comments.map((comment) => (
            <div
              key={comment.id}
              style={{
                display: 'flex',
                gap: '10px',
                padding: '8px 12px',
                background: 'rgba(255, 255, 255, 0.03)',
                borderRadius: 'var(--radius-md)',
                alignItems: 'flex-start'
              }}
            >
              <img
                src={comment.userAvatarUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}
                alt={comment.username}
                className="avatar avatar-sm"
                onClick={() => viewUserProfile(comment.userId)}
                style={{ cursor: 'pointer' }}
              />
              <div style={{ flex: 1 }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <span
                    onClick={() => viewUserProfile(comment.userId)}
                    style={{ fontWeight: '700', fontSize: '0.85rem', cursor: 'pointer' }}
                  >
                    @{comment.username}
                  </span>
                  <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                    {comment.createdAt ? new Date(comment.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : 'just now'}
                  </span>
                </div>
                <p style={{ fontSize: '0.9rem', marginTop: '2px', color: 'var(--text-primary)' }}>
                  {comment.content}
                </p>
              </div>

              {currentUser && (currentUser.id === comment.userId || currentUser.id === postAuthorId) && (
                <button
                  className="action-btn"
                  onClick={() => handleDeleteComment(comment.id)}
                  title="Delete comment"
                  style={{ padding: '4px', color: 'var(--text-muted)' }}
                >
                  <Trash2 size={14} />
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
