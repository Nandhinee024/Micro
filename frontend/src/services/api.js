const API_BASE_URL = 'http://localhost:8080/api';

// Helper for making HTTP requests
async function request(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;
  const defaultHeaders = {
    'Content-Type': 'application/json',
  };

  const response = await fetch(url, {
    ...options,
    headers: {
      ...defaultHeaders,
      ...options.headers,
    },
  });

  if (!response.ok) {
    let errorMessage = `HTTP Error ${response.status}`;
    try {
      const errorData = await response.json();
      errorMessage = errorData.message || errorData.error || errorMessage;
    } catch {
      // ignore
    }
    throw new Error(errorMessage);
  }

  // If response is empty or 204
  const contentType = response.headers.get('content-type');
  if (contentType && contentType.includes('application/json')) {
    return await response.json();
  }
  return null;
}

// User Service API
export const userApi = {
  register: (data) => request('/auth/register', { method: 'POST', body: JSON.stringify(data) }),
  login: (data) => request('/auth/login', { method: 'POST', body: JSON.stringify(data) }),
  getAllUsers: () => request('/users'),
  getUserById: (id) => request(`/users/${id}`),
  getUserByUsername: (username) => request(`/users/username/${username}`),
  searchUsers: (query) => request(`/users/search?query=${encodeURIComponent(query || '')}`),
  updateProfile: (id, data) => request(`/users/${id}/profile`, { method: 'PUT', body: JSON.stringify(data) }),
  followUser: (id, followerId) => request(`/users/${id}/follow?followerId=${followerId}`, { method: 'POST' }),
  unfollowUser: (id, followerId) => request(`/users/${id}/follow?followerId=${followerId}`, { method: 'DELETE' }),
  isFollowing: (id, followerId) => request(`/users/${id}/is-following?followerId=${followerId}`),
  getFollowers: (id) => request(`/users/${id}/followers`),
  getFollowing: (id) => request(`/users/${id}/following`),
};

// Post Service API
export const postApi = {
  createPost: (data) => request('/posts', { method: 'POST', body: JSON.stringify(data) }),
  getAllPosts: () => request('/posts'),
  getPostById: (id) => request(`/posts/${id}`),
  getUserPosts: (userId) => request(`/posts/user/${userId}`),
  updatePost: (id, data) => request(`/posts/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deletePost: (id) => request(`/posts/${id}`, { method: 'DELETE' }),
};

// Like Service API
export const likeApi = {
  likePost: (data) => request('/likes', { method: 'POST', body: JSON.stringify(data) }),
  unlikePost: (postId, userId) => request(`/likes?postId=${postId}&userId=${userId}`, { method: 'DELETE' }),
  getLikeStatus: (postId, userId) => request(`/likes/post/${postId}/status?userId=${userId || ''}`),
  getLikeCount: (postId) => request(`/likes/post/${postId}/count`),
  getLikesByPostId: (postId) => request(`/likes/post/${postId}`),
};

// Comment Service API
export const commentApi = {
  addComment: (data) => request('/comments', { method: 'POST', body: JSON.stringify(data) }),
  getCommentsByPostId: (postId) => request(`/comments/post/${postId}`),
  getCommentCount: (postId) => request(`/comments/post/${postId}/count`),
  deleteComment: (id) => request(`/comments/${id}`, { method: 'DELETE' }),
};

// Notification Service API
export const notificationApi = {
  getNotifications: (userId) => request(`/notifications/user/${userId}`),
  getUnreadCount: (userId) => request(`/notifications/user/${userId}/unread-count`),
  markAsRead: (id) => request(`/notifications/${id}/read`, { method: 'PUT' }),
  markAllAsRead: (userId) => request(`/notifications/user/${userId}/read-all`, { method: 'PUT' }),
  deleteNotification: (id) => request(`/notifications/${id}`, { method: 'DELETE' }),
};
