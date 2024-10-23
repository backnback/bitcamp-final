-- 스토리사진
DROP TABLE IF EXISTS story_photo RESTRICT;

-- 좋아요
DROP TABLE IF EXISTS story_like RESTRICT;

-- 스토리
DROP TABLE IF EXISTS story RESTRICT;

-- 회원
DROP TABLE IF EXISTS user RESTRICT;

-- 지역
DROP TABLE IF EXISTS location RESTRICT;

-- 자주 묻는 질문
DROP TABLE IF EXISTS faq RESTRICT;

-- 회원
CREATE TABLE user (
    user_id  INTEGER      NOT NULL COMMENT '회원번호', -- 회원번호
    email    VARCHAR(40)  NOT NULL COMMENT '이메일', -- 이메일
    password VARCHAR(255)  NOT NULL COMMENT '비밀번호', -- 비밀번호
    nickname VARCHAR(50)  NOT NULL COMMENT '닉네임', -- 닉네임
    path     VARCHAR(255) NULL     COMMENT '프로필 사진' -- 프로필 사진
)
COMMENT '회원';

-- 회원
ALTER TABLE user
    ADD CONSTRAINT PK_user -- 회원 기본키
    PRIMARY KEY (
    user_id -- 회원번호
    );

-- 회원 유니크 인덱스
CREATE UNIQUE INDEX UIX_user
    ON user ( -- 회원
        email ASC -- 이메일
    );

ALTER TABLE user
    MODIFY COLUMN user_id INTEGER NOT NULL AUTO_INCREMENT COMMENT '회원번호';

-- 스토리
CREATE TABLE story (
    story_id        INTEGER     NOT NULL COMMENT '스토리번호', -- 스토리번호
    user_id         INTEGER     NOT NULL COMMENT '회원번호', -- 회원번호
    location_id     INTEGER     NOT NULL COMMENT '지역번호', -- 지역번호
    title           TEXT        NOT NULL COMMENT '제목', -- 제목
    travel_date     DATE        NOT NULL COMMENT '날짜', -- 날짜
    create_date     DATETIME    NOT NULL DEFAULT now() COMMENT '등록날짜', -- 등록날짜
    location_detail VARCHAR(50) NULL     COMMENT '지역상세', -- 지역상세
    content         TEXT        NOT NULL COMMENT '내용', -- 내용
    share           TINYINT     NOT NULL COMMENT '공개여부' -- 공개여부
)
COMMENT '스토리';

-- 스토리
ALTER TABLE story
    ADD CONSTRAINT PK_story -- 스토리 기본키
    PRIMARY KEY (
    story_id -- 스토리번호
    );

-- 스토리 인덱스
CREATE INDEX IX_story
    ON story( -- 스토리
        create_date ASC -- 등록날짜
    );

-- 스토리 인덱스2
CREATE INDEX IX_story2
    ON story( -- 스토리
        travel_date ASC -- 날짜
    );

ALTER TABLE story
    MODIFY COLUMN story_id INTEGER NOT NULL AUTO_INCREMENT COMMENT '스토리번호';

-- 스토리사진
CREATE TABLE story_photo (
    photo_id   INTEGER      NOT NULL COMMENT '사진번호', -- 사진번호
    story_id   INTEGER      NOT NULL COMMENT '스토리번호', -- 스토리번호
    main_photo TINYINT      NOT NULL COMMENT '대표사진(T/F)', -- 대표사진(T/F)
    path       VARCHAR(255) NOT NULL COMMENT '사진경로' -- 사진경로
)
COMMENT '스토리사진';

-- 스토리사진
ALTER TABLE story_photo
    ADD CONSTRAINT PK_story_photo -- 스토리사진 기본키
    PRIMARY KEY (
    photo_id -- 사진번호
    );

ALTER TABLE story_photo
    MODIFY COLUMN photo_id INTEGER NOT NULL AUTO_INCREMENT COMMENT '사진번호';

-- 좋아요
CREATE TABLE story_like (
    user_id   INTEGER  NOT NULL COMMENT '회원번호', -- 회원번호
    story_id  INTEGER  NOT NULL COMMENT '스토리번호', -- 스토리번호
    like_date DATETIME NOT NULL DEFAULT now() COMMENT '일시', -- 일시
    view      TINYINT  NOT NULL COMMENT '조회여부' -- 조회여부
)
COMMENT '좋아요';

-- 좋아요
ALTER TABLE story_like
    ADD CONSTRAINT PK_story_like -- 좋아요 기본키
    PRIMARY KEY (
    user_id,  -- 회원번호
    story_id  -- 스토리번호
    );

-- 지역
CREATE TABLE location (
    location_id INTEGER     NOT NULL COMMENT '지역번호', -- 지역번호
    first_name  VARCHAR(50) NOT NULL COMMENT '시/도', -- 시/도
    second_name VARCHAR(50) NOT NULL COMMENT '시/군/구' -- 시/군/구
)
COMMENT '지역';

-- 지역
ALTER TABLE location
    ADD CONSTRAINT PK_location -- 지역 기본키
    PRIMARY KEY (
    location_id -- 지역번호
    );

ALTER TABLE location
    MODIFY COLUMN location_id INTEGER NOT NULL AUTO_INCREMENT COMMENT '지역번호';

-- 자주 묻는 질문
CREATE TABLE faq (
    faq_id  INTEGER NOT NULL COMMENT '질문번호', -- 질문번호
    title   TEXT    NOT NULL COMMENT '제목', -- 제목
    content TEXT    NOT NULL COMMENT '내용' -- 내용
)
COMMENT '자주 묻는 질문';

-- 자주 묻는 질문
ALTER TABLE faq
    ADD CONSTRAINT PK_faq -- 자주 묻는 질문 기본키
    PRIMARY KEY (
    faq_id -- 질문번호
    );

ALTER TABLE faq
    MODIFY COLUMN faq_id INTEGER NOT NULL AUTO_INCREMENT COMMENT '질문번호';

-- 스토리
ALTER TABLE story
    ADD CONSTRAINT FK_user_TO_story -- 회원 -> 스토리
    FOREIGN KEY (
    user_id -- 회원번호
    )
    REFERENCES user ( -- 회원
    user_id -- 회원번호
    );

-- 스토리
ALTER TABLE story
    ADD CONSTRAINT FK_location_TO_story -- 지역 -> 스토리
    FOREIGN KEY (
    location_id -- 지역번호
    )
    REFERENCES location ( -- 지역
    location_id -- 지역번호
    );

-- 스토리사진
ALTER TABLE story_photo
    ADD CONSTRAINT FK_story_TO_story_photo -- 스토리 -> 스토리사진
    FOREIGN KEY (
    story_id -- 스토리번호
    )
    REFERENCES story ( -- 스토리
    story_id -- 스토리번호
    );

-- 좋아요
ALTER TABLE story_like
    ADD CONSTRAINT FK_user_TO_story_like -- 회원 -> 좋아요
    FOREIGN KEY (
    user_id -- 회원번호
    )
    REFERENCES user ( -- 회원
    user_id -- 회원번호
    );

-- 좋아요
ALTER TABLE story_like
    ADD CONSTRAINT FK_story_TO_story_like -- 스토리 -> 좋아요
    FOREIGN KEY (
    story_id -- 스토리번호
    )
    REFERENCES story ( -- 스토리
    story_id -- 스토리번호
    );
