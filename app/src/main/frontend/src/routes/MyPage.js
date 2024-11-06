import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // useNavigate import 추가
// import './ShareStoryList.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용
import StoryItemList from "../components/StoryItemList";
import AlarmCardList from "../components/AlarmCardList";



const MyPage = () => {
    const [storyList, setStoryList] = useState([]); // 변수 이름을 stories로 수정
    const [userList, setUserList] = useState([]); // 테스트용
    const navigate = useNavigate(); // navigate 함수를 사용하여 페이지 이동
    const [batchedLikes, setBatchedLikes] = useState([]);
    const [accessToken, setAccessToken] = useState(null);



    // 로컬 스토리지에서 accessToken을 가져오는 함수
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setAccessToken(token);
        } else {
            console.warn("Access token이 없습니다.");
        }
    }, []);


    // accessToken이 설정된 경우에만 호출
    useEffect(() => {
        if (accessToken) {
            const fetchStoryList = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/like/list/my-stories', {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    }); // API 요청
                    setStoryList(response.data);
                } catch (error) {
                    console.error("좋아요한 스토리 불러오기 실패!", error);
                }
            };
            fetchStoryList();
        }
    }, [accessToken]);


    useEffect(() => {
        if (accessToken) {
            const fetchUserList = async () => {
                try {
                    const response2 = await axios.get('http://localhost:8080/like/list/users', {
                                headers: {
                                    'Authorization': `Bearer ${accessToken}`
                                }
                    });
                    setUserList(response2.data);
                } catch (error) {
                    console.error("오류가 발생했습니다!", error);
                }
            };
            fetchUserList();
        }
    }, [accessToken]);

    // StoryItemList에서 모아둔 like 변경 사항을 저장하는 함수
    const handleBatchedLikesChange = (newBatchedLikes) => {
        setBatchedLikes(newBatchedLikes);
    };

    // 페이지 이동이나 새로고침 시, 서버에 좋아요 변경 사항 전송
    const handleSubmitLikes = async () => {
        if (batchedLikes.length === 0) return;

        try {
            console.log(batchedLikes);
            await axios.post('http://localhost:8080/like/batch-update', batchedLikes, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
            });
            setBatchedLikes([]); // 전송 후 초기화
        } catch (error) {
            console.error("좋아요 변경 사항 전송 중 에러 발생", error);
        }
    };

    useEffect(() => {
        // 페이지 새로고침 시 전송
        window.addEventListener('beforeunload', handleSubmitLikes);

        // 페이지 이동 시 전송
        const unlisten = navigate((location) => {
            handleSubmitLikes();
        });
        return () => {
            window.removeEventListener('beforeunload', handleSubmitLikes);
            handleSubmitLikes(); // 컴포넌트 언마운트 시에도 전송
        };
    }, [batchedLikes]);

    return (
       <div className="story-list">
           <h1>좋아요한 스토리</h1>
           <StoryItemList
                storyList={storyList}
                onBatchedLikesChange={handleBatchedLikesChange}
           />

           <h2>알림</h2>
           <AlarmCardList userList={userList} />
       </div>
    );
};

export default MyPage;
