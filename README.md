### Authentication
| **기능**          | **Method** | **URL**          | **Request**                                    | **Response**                                    | **상태 코드** |
|--------------------|------------|------------------|-----------------------------------------------|-----------------------------------------------|---------------|
| 로그인             | POST       | /login           | ```json { "email": "user@example.com", "password": "UserPass123!" }``` | 완료                                           | 200           |
| 로그아웃           | POST       | /logout          | -                                             | 완료                                           | 200           |

---

### Users
| **기능**          | **Method** | **URL**          | **Request**                                    | **Response**                                    | **상태 코드** |
|--------------------|------------|------------------|-----------------------------------------------|-----------------------------------------------|---------------|
| 사용자 생성        | POST       | /users           | ```json { "email": "user@example.com", "name": "홍길동", "password": "UserPass123!" }``` | ```json { "email": "user@example.com", "name": "홍길동", "createdAt": "2024-01-01" }``` | 201           |
| 사용자 업데이트    | PATCH      | /users           | ```json { "updateName": "새이름" }```         | ```json { "updateName": "새이름", "updatedAt": "2024-02-01" }``` | 200           |
| 비밀번호 업데이트 | PATCH      | /users/password  | ```json { "currentPassword": "OldPass123!", "updatePassword": "NewPass123!" }``` | ```json { "message": "비밀번호가 성공적으로 수정되었습니다." }``` | 200           |
| 사용자 조회        | GET        | /users/{userId}  | -                                             | ```json { "email": "user@example.com", "name": "홍길동", "createdAt": "2024-01-01", "updatedAt": "2024-02-01" }``` | 200           |
| 사용자 삭제        | DELETE     | /users           | ```json { "currentPassword": "UserPass123!" }``` | ```json { "message": "사용자가 성공적으로 삭제되었습니다." }``` | 200           |

---

### Posts
| **기능**          | **Method** | **URL**                | **Request**                                    | **Response**                                    | **상태 코드** |
|--------------------|------------|------------------------|-----------------------------------------------|-----------------------------------------------|---------------|
| 게시물 생성        | POST       | /posts                 | ```json { "title": "게시물 제목", "content": "내용" }``` | ```json { "id": 1, "title": "게시물 제목", "createdAt": "2024-01-01" }``` | 201           |
| 게시물 조회        | GET        | /posts                 | -                                             | ```json [ { "id": 1, "title": "게시물 제목", "createdAt": "2024-01-01" } ]``` | 200           |
| 게시물 갱신        | PATCH      | /posts/{postId}        | ```json { "title": "새 제목", "content": "새 내용" }``` | ```json { "id": 1, "title": "새 제목", "updatedAt": "2024-02-01" }``` | 200           |
| 게시물 삭제        | DELETE     | /posts/{postId}        | -                                             | ```json { "message": "게시물이 성공적으로 삭제되었습니다." }``` | 200           |
| 좋아요 추가        | POST       | /posts/{postId}/likes  | -                                             | ```json { "message": "좋아요가 추가되었습니다." }``` | 200           |
| 좋아요 삭제        | DELETE     | /posts/{postId}/likes  | -                                             | ```json { "message": "좋아요가 삭제되었습니다." }``` | 200           |

---

### Comments
| **기능**          | **Method** | **URL**                | **Request**                                    | **Response**                                    | **상태 코드** |
|--------------------|------------|------------------------|-----------------------------------------------|-----------------------------------------------|---------------|
| 댓글 생성          | POST       | /comments              | ```json { "content": "댓글 내용" }```          | ```json { "username": "홍길동", "content": "댓글 내용", "createdAt": "2024-01-01" }``` | 201           |
| 댓글 갱신          | PATCH      | /comments/{commentId}  | ```json { "updateContent": "수정된 댓글 내용" }``` | ```json { "content": "수정된 댓글 내용", "updatedAt": "2024-02-01" }``` | 200           |
| 댓글 조회          | GET        | /comments/{commentId}  | -                                             | ```json { "username": "홍길동", "content": "댓글 내용", "createdAt": "2024-01-01", "updatedAt": "2024-02-01" }``` | 200           |
| 댓글 삭제          | DELETE     | /comments/{commentId}  | -                                             | ```json { "message": "댓글이 성공적으로 삭제되었습니다." }``` | 200           |
| 좋아요 추가        | POST       | /comments/{commentId}/likes | -                                         | ```json { "message": "좋아요가 추가되었습니다." }``` | 200           |
| 좋아요 삭제        | DELETE     | /comments/{commentId}/likes | -                                         | ```json { "message": "좋아요가 삭제되었습니다." }``` | 200           |

---

### Friends
| **기능**          | **Method** | **URL**                 | **Request**                                    | **Response**                                    | **상태 코드** |
|--------------------|------------|-------------------------|-----------------------------------------------|-----------------------------------------------|---------------|
| 팔로우 추가        | POST       | /friends                | ```json { "followee": "followeeId" }```        | ```json { "status": "팔로우 추가됨", "followee": "followeeId" }``` | 201           |
| 팔로우 상태 변경   | PATCH      | /friends/{friendId}     | ```json { "status": "변경된 상태" }```         | ```json { "status": "변경됨", "follower": "followerId", "followee": "followeeId" }``` | 200           |
| 팔로워 목록 조회   | GET        | /friends/follower       | -                                             | ```json [ { "id": 1, "name": "팔로워 이름" } ]``` | 200           |
| 팔로잉 목록 조회   | GET        | /friends/followee       | -                                             | ```json [ { "id": 2, "name": "팔로잉 이름" } ]``` | 200           |
| 팔로우 삭제        | DELETE     | /friends/{friendId}     | -                                             | ```json { "message": "팔로우가 성공적으로 삭제되었습니다." }``` | 200           |
