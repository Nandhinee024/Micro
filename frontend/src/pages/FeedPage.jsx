import React, { useState, useEffect } from 'react';
import { useApp } from '../context/AppContext';
import { postApi } from '../services/api';
import PostCard from '../components/PostCard';
import { Sparkles, RefreshCw, Image, Send } from 'lucide-react';

const INITIAL_FALLBACK_POSTS = [
  {
    id: 101,
    userId: 2,
    authorUsername: 'sarah_tech',
    authorAvatarUrl: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150',
    content: 'Just migrated our entire backend architecture to 5 independent Spring Boot 3.2 microservices with Java 21! Zero security bottlenecks, blazing-fast Eureka discovery, and unified API Gateway routing. 🚀⚡',
    mediaUrl: 'https://images.unsplash.com/photo-1518770660439-4636190af475?w=800',
    likeCount: 14,
    commentCount: 3,
    createdAt: new Date(Date.now() - 3600000).toISOString()
  },
  {
    id: 102,
    userId: 3,
    authorUsername: 'marcus_design',
    authorAvatarUrl: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150',
    content: 'Glassmorphism and subtle micro-animations make all the difference in modern web apps. What are your thoughts on dark mode aesthetics? 🎨✨',
    mediaUrl: 'https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800',
    likeCount: 28,
    commentCount: 7,
    createdAt: new Date(Date.now() - 7200000).toISOString()
  }
];

export default function FeedPage() {
  const { currentUser, setIsCreatePostModalOpen, feedRefreshKey, refreshFeed } = useApp();
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchPosts();
  }, [feedRefreshKey]);

  const fetchPosts = async () => {
    try {
      setLoading(true);
      const data = await postApi.getAllPosts();
      if (Array.isArray(data) && data.length > 0) {
        setPosts(data);
      } else {
        setPosts(INITIAL_FALLBACK_POSTS);
      }
    } catch (err) {
      console.warn('API Gateway offline, using demo feed:', err.message);
      setPosts(INITIAL_FALLBACK_POSTS);
    } finally {
      setLoading(false);
    }
  };

  const handleDeletePost = (postId) => {
    setPosts(prev => prev.filter(p => p.id !== postId));
  };

  return (
    <div>
      {/* Quick Composer Card */}
      <div
        className="glass-card"
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: '12px',
          marginBottom: '24px',
          cursor: 'pointer'
        }}
        onClick={() => setIsCreatePostModalOpen(true)}
      >
        <img
          src={currentUser?.profilePictureUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}
          alt="me"
          className="avatar"
        />
        <div style={{
          flex: 1,
          padding: '12px 16px',
          background: 'rgba(255, 255, 255, 0.05)',
          borderRadius: 'var(--radius-full)',
          color: 'var(--text-muted)',
          fontSize: '0.95rem'
        }}>
          What's on your mind, {currentUser?.firstName || currentUser?.username || 'Creator'}?
        </div>
        <button className="gradient-btn" style={{ padding: '8px 14px' }}>
          <Sparkles size={16} />
          <span>Post</span>
        </button>
      </div>

      {/* Feed Controls */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
        <h3 style={{ fontSize: '1.1rem', fontWeight: '700' }}>Recent Posts</h3>
        <button
          className="btn-secondary"
          onClick={fetchPosts}
          disabled={loading}
          style={{ padding: '6px 12px', fontSize: '0.8rem' }}
        >
          <RefreshCw size={14} className={loading ? 'animate-spin' : ''} />
          <span>{loading ? 'Refreshing...' : 'Refresh'}</span>
        </button>
      </div>

      {/* Posts List */}
      <div>
        {posts.map((post) => (
          <PostCard
            key={post.id}
            post={post}
            onDelete={handleDeletePost}
          />
        ))}
      </div>
    </div>
  );
}
