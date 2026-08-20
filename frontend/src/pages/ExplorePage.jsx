import React, { useState, useEffect } from 'react';
import { userApi } from '../services/api';
import UserCard from '../components/UserCard';
import { Search, Users, RefreshCw } from 'lucide-react';

const DEMO_USERS_FALLBACK = [
  {
    id: 1,
    username: 'alex_rivers',
    firstName: 'Alex',
    lastName: 'Rivers',
    email: 'alex@example.com',
    profilePictureUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150',
    bio: 'Digital Creator & Fullstack Developer | Building the future 🚀'
  },
  {
    id: 2,
    username: 'sarah_tech',
    firstName: 'Sarah',
    lastName: 'Chen',
    email: 'sarah@example.com',
    profilePictureUrl: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150',
    bio: 'Cloud Architect & Microservices enthusiast ☁️ | Coffee lover ☕'
  },
  {
    id: 3,
    username: 'marcus_design',
    firstName: 'Marcus',
    lastName: 'Vance',
    email: 'marcus@example.com',
    profilePictureUrl: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150',
    bio: 'Product Designer & UI/UX Specialist ✨ Minimalist vibes'
  },
  {
    id: 4,
    username: 'elena_ai',
    firstName: 'Elena',
    lastName: 'Rostova',
    email: 'elena@example.com',
    profilePictureUrl: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150',
    bio: 'AI & ML Researcher | Open Source Contributor 🤖'
  }
];

export default function ExplorePage() {
  const [users, setUsers] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const data = await userApi.getAllUsers();
      if (Array.isArray(data) && data.length > 0) {
        setUsers(data);
      } else {
        setUsers(DEMO_USERS_FALLBACK);
      }
    } catch (e) {
      setUsers(DEMO_USERS_FALLBACK);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      const data = await userApi.searchUsers(searchQuery);
      if (Array.isArray(data) && data.length > 0) {
        setUsers(data);
      } else {
        const filtered = DEMO_USERS_FALLBACK.filter(u =>
          u.username.toLowerCase().includes(searchQuery.toLowerCase()) ||
          u.firstName.toLowerCase().includes(searchQuery.toLowerCase()) ||
          u.lastName.toLowerCase().includes(searchQuery.toLowerCase())
        );
        setUsers(filtered);
      }
    } catch (e) {
      console.warn('Search failed:', e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* Search Header */}
      <form onSubmit={handleSearch} style={{ marginBottom: '24px' }}>
        <div style={{ position: 'relative' }}>
          <Search size={18} style={{ position: 'absolute', left: '16px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
          <input
            type="text"
            className="form-input"
            placeholder="Search creators by name or @username..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{ paddingLeft: '44px', borderRadius: 'var(--radius-full)' }}
          />
        </div>
      </form>

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
        <h3 style={{ fontSize: '1.1rem', fontWeight: '700', display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Users size={18} /> Discover People
        </h3>
        <button
          className="btn-secondary"
          onClick={fetchUsers}
          disabled={loading}
          style={{ padding: '6px 12px', fontSize: '0.8rem' }}
        >
          <RefreshCw size={14} className={loading ? 'animate-spin' : ''} />
          <span>Refresh</span>
        </button>
      </div>

      {/* User list */}
      <div>
        {users.map((user) => (
          <UserCard
            key={user.id}
            user={user}
            onFollowChange={fetchUsers}
          />
        ))}
      </div>
    </div>
  );
}
