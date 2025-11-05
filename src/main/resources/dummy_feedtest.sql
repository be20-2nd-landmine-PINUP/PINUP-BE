INSERT INTO users (login_type, user_name, email, nickname, gender, birth_date, preferred_category, preferred_season)
VALUES
    ('GOOGLE', '이름01', 'id01@example.com', 'nickname1', 'M', '1990-05-10', '문화', '가을'),
    ('KAKAO', '이름02', 'id02@example.com', 'nickname2', 'F', '1992-08-15', '역사', '봄'),
    ('GOOGLE', '이름03', 'id03@example.com', 'nickname3', 'M', '1988-12-25', '자연', '겨울');

INSERT INTO feed (user_id, title, content, image_url, like_count, created_at, updated_at)
VALUES
    (1, '제목01', '내용01', 'https://backend.pinup/feed/jeju1.jpg', 12, '2025-11-05 09:20:00', '2025-11-05 09:20:00'),
    (2, '제목02', '내용02', 'https://backend.pinup/feed/busan1.jpg', 9, '2025-11-04 15:40:00', '2025-11-04 15:40:00'),
    (3, '제목03', '내용03', NULL, 15, '2025-11-03 11:10:00', '2025-11-03 11:10:00'),
    (3, '제목04', '내용04', 'https://backend.pinup/feed/seoul1.jpg', 22, '2025-11-02 17:30:00', '2025-11-02 17:30:00'),
    (1, '제목05', '내용05', 'https://backend.pinup/feed/gangneung1.jpg', 7, '2025-11-01 14:00:00', '2025-11-01 14:00:00'),
    (2, '제목06', '내용06', NULL, 4, '2025-10-31 09:45:00', '2025-10-31 09:45:00');