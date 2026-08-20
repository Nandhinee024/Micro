# Social Media Platform - Independent Microservices & React Frontend

A decoupled, scalable social media platform built with **Spring Boot 3.2.5**, **Java 21**, **Maven**, and a modern **React Frontend**.

---

## 🏛️ System Architecture

### 3 Infrastructure Services
| Service | Port | Description |
| :--- | :--- | :--- |
| **`eureka-server`** | `8761` | Service Registry & Discovery |
| **`config-server`** | `8888` | Centralized Configuration Repository |
| **`api-gateway`** | `8080` | Spring Cloud Gateway with global CORS & Resilience4j routing |

### 5 Business Microservices
| Service | Port | Database (H2/MySQL) | Description |
| :--- | :--- | :--- | :--- |
| **`user-service`** | `8081` | `userdb` | User profiles, registration, login, follow/unfollow |
| **`post-service`** | `8082` | `postdb` | Create, read, update, and delete posts |
| **`like-service`** | `8083` | `likedb` | Like/unlike posts, like counts, likers list |
| **`comment-service`** | `8084` | `commentdb` | Add comments, post comment feeds, comment counts |
| **`notification-service`** | `8085` | `notificationdb` | Real-time notifications for likes, comments, and follows |

### 1 React Frontend
| Application | Port | Stack |
| :--- | :--- | :--- |
| **`frontend`** | `5173` | React 18, Vite 5, Lucide Icons, Glassmorphism CSS |

---

## 🚀 Key Architectural Improvements
1. **100% Independent Projects**: The parent `pom.xml` has been removed. Each service is a standalone Maven project with `spring-boot-starter-parent` 3.2.5 and Java 21.
2. **Zero Security/JWT Complexity**: All Spring Security and JWT filters have been removed for a clean, direct REST API design.
3. **Global Exception Handling**: Every service includes `@RestControllerAdvice` handling `MethodArgumentNotValidException`, `ResourceNotFoundException`, `DuplicateResourceException`, and `BadRequestException`.
4. **Jakarta Validation**: Robust input validation on all request DTOs (`@NotBlank`, `@NotNull`, `@Size`, `@Email`).
5. **Standard Naming Conventions**: Standardized Controller, Service, Repository, Entity, DTO, Client, and Exception classes.
6. **Zero-Setup In-Memory Database**: Each service runs on an in-memory H2 database by default (with easy switch to MySQL in `application.properties`).

---

## 🛠️ How to Run

### Step 1: Start Infrastructure Services
Start services in the following order:
```bash
# 1. Eureka Server (Port 8761)
cd eureka-server
mvn spring-boot:run

# 2. Config Server (Port 8888)
cd ../config-server
mvn spring-boot:run

# 3. API Gateway (Port 8080)
cd ../api-gateway
mvn spring-boot:run
```

### Step 2: Start Business Microservices
Run each in a separate terminal:
```bash
# User Service (Port 8081)
cd user-service && mvn spring-boot:run

# Post Service (Port 8082)
cd post-service && mvn spring-boot:run

# Like Service (Port 8083)
cd like-service && mvn spring-boot:run

# Comment Service (Port 8084)
cd comment-service && mvn spring-boot:run

# Notification Service (Port 8085)
cd notification-service && mvn spring-boot:run
```

### Step 3: Start React Frontend
```bash
cd frontend
npm run dev
```
Open `http://localhost:5173` in your browser.

---

## 🌐 API Gateway Endpoints (`http://localhost:8080`)

### User Service (`/api/users` & `/api/auth`)
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/search?query=...` - Search users
- `PUT /api/users/{id}/profile` - Update profile
- `POST /api/users/{id}/follow?followerId=...` - Follow user
- `DELETE /api/users/{id}/follow?followerId=...` - Unfollow user

### Post Service (`/api/posts`)
- `POST /api/posts` - Create post
- `GET /api/posts` - Get all posts
- `GET /api/posts/{id}` - Get post by ID
- `GET /api/posts/user/{userId}` - Get user posts
- `PUT /api/posts/{id}` - Update post
- `DELETE /api/posts/{id}` - Delete post

### Like Service (`/api/likes`)
- `POST /api/likes` - Like a post
- `DELETE /api/likes?postId={postId}&userId={userId}` - Unlike a post
- `GET /api/likes/post/{postId}/status?userId={userId}` - Check like status
- `GET /api/likes/post/{postId}/count` - Get like count
- `GET /api/likes/post/{postId}` - List likes for post

### Comment Service (`/api/comments`)
- `POST /api/comments` - Add comment
- `GET /api/comments/post/{postId}` - Get comments for post
- `GET /api/comments/post/{postId}/count` - Get comment count
- `DELETE /api/comments/{id}` - Delete comment

### Notification Service (`/api/notifications`)
- `GET /api/notifications/user/{userId}` - Get user notifications
- `GET /api/notifications/user/{userId}/unread-count` - Get unread count
- `PUT /api/notifications/{id}/read` - Mark notification as read
- `PUT /api/notifications/user/{userId}/read-all` - Mark all as read
- `DELETE /api/notifications/{id}` - Delete notification
