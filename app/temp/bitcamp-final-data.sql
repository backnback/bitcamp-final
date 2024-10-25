INSERT INTO user (user_id, email, password, nickname, path)
VALUES 
(1, 'hong@example.com', 'pass1234', '홍길동', '/images/hong.jpg'),
(2, 'kim@example.com', 'kim4321', '김철수', '/images/kim.jpg'),
(3, 'park@example.com', 'park5678', '박영희', '/images/park.jpg'),
(4, 'lee@example.com', 'lee8765', '이민수', '/images/lee.jpg'),
(5, 'choi@example.com', 'choi9876', '최은정', '/images/choi.jpg');

INSERT INTO location (location_id, first_name, second_name)
VALUES
(1, '서울', '관악구'),
(2, '부산', '해운대구'),
(3, '제주', '서귀포시'),
(4, '대구', '달성군'),
(5, '인천', '연수구');


INSERT INTO story (story_id, user_id, location_id, title, travel_date, create_date, location_detail, content, share)
VALUES 
(1, 1, 1, '서울 여행', '2024-09-01', '2024-09-10 12:00:00', '광화문', '경복궁에 다녀왔습니다.', 1),
(2, 2, 2, '부산 여행', '2024-08-15', '2024-08-20 14:00:00', '해운대', '해운대 해수욕장에서 놀았어요.', 0),
(3, 3, 3, '제주도 여행', '2024-07-10', '2024-07-15 10:00:00', '성산 일출봉', '성산 일출봉에 올랐습니다.', 1),
(4, 4, 4, '대구 여행', '2024-06-05', '2024-06-10 16:00:00', '동화사', '동화사에 다녀왔어요.', 1),
(5, 5, 5, '인천 여행', '2024-05-01', '2024-05-05 18:00:00', '송도', '송도 센트럴파크에서 보트 탔습니다.', 0);

INSERT INTO story_photo (photo_id, story_id, main_photo, path)
VALUES 
(1, 1, 1, '/photos/seoul1.jpg'),
(2, 1, 0, '/photos/seoul2.jpg'),
(3, 2, 1, '/photos/busan1.jpg'),
(4, 3, 1, '/photos/jeju1.jpg'),
(5, 4, 1, '/photos/daegu1.jpg'),
(6, 5, 1, '/photos/incheon1.jpg');

INSERT INTO story_like (user_id, story_id, like_date, view)
VALUES 
(1, 1, '2024-10-10 10:00:00', 1),
(2, 2, '2024-10-09 09:00:00', 1),
(3, 3, '2024-10-08 08:00:00', 0),
(4, 4, '2024-10-07 07:00:00', 1),
(5, 5, '2024-10-06 06:00:00', 0);


INSERT INTO faq (faq_id, title, content)
VALUES 
(1, '사진을 어떻게 올리나요?', '스토리 페이지에서 업로드 버튼을 클릭하세요.'),
(2, '스토리를 공유할 수 있나요?', '네, 공유 버튼을 눌러서 공유할 수 있습니다.'),
(3, '프로필 사진을 어떻게 바꾸나요?', '프로필 설정에서 새 사진을 업로드하세요.'),
(4, '어떤 종류의 스토리를 올릴 수 있나요?', '여행과 관련된 모든 이야기를 올릴 수 있습니다.'),
(5, '여러 개의 스토리에 좋아요를 누를 수 있나요?', '네, 여러 개의 스토리에 좋아요를 누를 수 있습니다.');



