import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // useNavigate import 추가
import axios from 'axios'; // axios를 import하여 API 요청 사용
import { StoryAddContext } from '../components/StoryItem';
import StoryItemList from '../components/StoryItemList';


const MyStoryList = () => {
    const [storyList, setStoryList] = useState([]);
    const [accessToken, setAccessToken] = useState(null);
    const navigate = useNavigate(); // navigate 함수를 사용하여 페이지 이동
    const [batchedLikes, setBatchedLikes] = useState([]);


    // 로컬 스토리지에서 accessToken을 가져오는 함수
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setAccessToken(token);
        } else {
            console.warn("Access token이 없습니다.");
        }
    }, []);


    // accessToken이 설정된 경우에만 fetchList 호출
    useEffect(() => {
        if (accessToken) {
            const fetchStoryList = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/my-story/list', {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    }); // API 요청
                    setStoryList(response.data);
                } catch (error) {
                    console.error("There was an error", error);
                }
            };
            fetchStoryList();
        }
    }, [accessToken]);


    // 스토리 추가 버튼 클릭 시 MyStoryAddForm으로 이동하는 함수
    const handleAddStory = () => {
        navigate('/my-story/form/add'); // MyStoryAddForm 페이지로 이동
    };


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
            <h2>My 스토리</h2>
            <StoryItemList
                storyList={storyList}
                onAddStory={handleAddStory}
                onBatchedLikesChange={handleBatchedLikesChange}
            />
        </div>
    );
};

export default MyStoryList;
