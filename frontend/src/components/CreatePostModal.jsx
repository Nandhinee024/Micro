import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { postApi } from '../services/api';
import { X, Image, Sparkles, Send } from 'lucide-react';

const SAMPLE_IMAGES = [
  { label: 'Nature', url: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800' },
  { label: 'City', url: 'https://images.unsplash.com/photo-1477959858617-67f30bc75b82?w=800' },
  { label: 'Tech', url: 'https://images.unsplash.com/photo-1518770660439-4636190af475?w=800' },
  { label: 'Art', url: 'https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800' }
];

export default function CreatePostModal() {
  const { currentUser, isCreatePostModalOpen, setIsCreatePostModalOpen, refreshFeed, setIsAuthModalOpen } = useApp();
  const [content, setContent] = useState('');
  const [mediaUrl, setMediaUrl] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  if (!isCreatePostModalOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!currentUser) {
      setIsCreatePostModalOpen(false);
      setIsAuthModalOpen(true);
      return;
    }

    if (!content.trim()) {
      setError('Please write something for your post');
      return;
    }

    try {
      setSubmitting(true);
      setError('');
      await postApi.createPost({
        userId: currentUser.id,
        authorUsername: currentUser.username,
        authorAvatarUrl: currentUser.profilePictureUrl,
        content: content.trim(),
        mediaUrl: mediaUrl.trim() || null
      });

      setContent('');
      setMediaUrl('');
      setIsCreatePostModalOpen(false);
      refreshFeed();
    } catch (err) {
      setError(err.message || 'Failed to create post');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="modal-backdrop" onClick={() => setIsCreatePostModalOpen(false)}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
          <h2 style={{ fontSize: '1.25rem' }}>Create New Post</h2>
          <button className="action-btn" onClick={() => setIsCreatePostModalOpen(false)}>
            <X size={20} />
          </button>
        </div>

        {error && (
          <div style={{
            background: 'rgba(239, 68, 68, 0.15)',
            border: '1px solid var(--danger)',
            color: '#fca5a5',
            padding: '10px 14px',
            borderRadius: 'var(--radius-md)',
            marginBottom: '16px',
            fontSize: '0.85rem'
          }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div style={{ display: 'flex', gap: '12px', marginBottom: '16px' }}>
            <img
              src={currentUser?.profilePictureUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}
              alt="avatar"
              className="avatar"
            />
            <div style={{ flex: 1 }}>
              <textarea
                className="form-input"
                placeholder="What's happening in your universe?"
                rows={4}
                value={content}
                onChange={(e) => setContent(e.target.value)}
                maxLength={2000}
                style={{ fontSize: '1rem', border: 'none', background: 'transparent', padding: 0 }}
                autoFocus
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label" style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Image size={16} /> Image / Media URL (optional)
            </label>
            <input
              type="url"
              className="form-input"
              placeholder="https://images.unsplash.com/photo-..."
              value={mediaUrl}
              onChange={(e) => setMediaUrl(e.target.value)}
            />
          </div>

          {/* Quick Sample Media Pickers */}
          <div style={{ marginBottom: '16px' }}>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginRight: '8px' }}>Sample Photos:</span>
            <div style={{ display: 'inline-flex', gap: '6px', flexWrap: 'wrap', marginTop: '4px' }}>
              {SAMPLE_IMAGES.map((img) => (
                <button
                  type="button"
                  key={img.label}
                  className="btn-secondary"
                  style={{ padding: '3px 8px', fontSize: '0.75rem' }}
                  onClick={() => setMediaUrl(img.url)}
                >
                  {img.label}
                </button>
              ))}
            </div>
          </div>

          {/* Media Preview */}
          {mediaUrl && (
            <div style={{ position: 'relative', marginBottom: '16px' }}>
              <img
                src={mediaUrl}
                alt="preview"
                style={{ width: '100%', maxHeight: '200px', objectFit: 'cover', borderRadius: 'var(--radius-md)' }}
                onError={(e) => { e.target.style.display = 'none'; }}
              />
              <button
                type="button"
                className="action-btn"
                onClick={() => setMediaUrl('')}
                style={{ position: 'absolute', top: '8px', right: '8px', background: 'rgba(0,0,0,0.6)' }}
              >
                <X size={16} />
              </button>
            </div>
          )}

          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', paddingTop: '12px', borderTop: '1px solid var(--border-color)' }}>
            <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
              {content.length} / 2000
            </span>

            <div style={{ display: 'flex', gap: '10px' }}>
              <button
                type="button"
                className="btn-secondary"
                onClick={() => setIsCreatePostModalOpen(false)}
              >
                Cancel
              </button>
              <button
                type="submit"
                className="gradient-btn"
                disabled={submitting || !content.trim()}
              >
                <Send size={16} />
                <span>{submitting ? 'Publishing...' : 'Publish Post'}</span>
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}
