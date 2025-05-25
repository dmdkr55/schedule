CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '유저 식별자',
    username VARCHAR(100) NOT NULL COMMENT '이름',
    password VARCHAR(100) NOT NULL COMMENT '비밀번호'
);

CREATE TABLE todo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '메모 식별자',
    title VARCHAR(255) COMMENT '제목',
    content TEXT COMMENT '내용',
    created_at DATETIME COMMENT '작성일',
    modified_at DATETIME COMMENT '수정일',
    user_id BIGINT COMMENT '유저 식별자',
    FOREIGN KEY (user_id) REFERENCES user(id)
);
