import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import StoryItemList from "../components/StoryItemList";
import AlarmCardList from "../components/AlarmCardList";
import Profile from "../components/Profile";

const MyPage = () => {
    const [storyList, setStoryList] = useState([]);
    const [userList, setUserList] = useState([]);
    const navigate = useNavigate();
    const [batchedLikes, setBatchedLikes] = useState([]);
    const [accessToken, setAccessToken] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setAccessToken(token);
        } else {
            console.warn("Access token이 없습니다.");
        }
    }, []);

    useEffect(() => {
        if (accessToken) {
            const fetchStoryList = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/like/list/my-stories', {
                        headers: { 'Authorization': `Bearer ${accessToken}` }
                    });
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
                        headers: { 'Authorization': `Bearer ${accessToken}` }
                    });
                    setUserList(response2.data);
                } catch (error) {
                    console.error("오류가 발생했습니다!", error);
                }
            };
            fetchUserList();
        }
    }, [accessToken]);

    const handleBatchedLikesChange = (newBatchedLikes) => {
        setBatchedLikes(newBatchedLikes);
    };

    const handleSubmitLikes = async () => {
        if (batchedLikes.length === 0) return;

        try {
            console.log(batchedLikes);
            await axios.post('http://localhost:8080/like/batch-update', batchedLikes, {
                headers: { 'Authorization': `Bearer ${accessToken}` }
            });
            setBatchedLikes([]);
        } catch (error) {
            console.error("좋아요 변경 사항 전송 중 에러 발생", error);
        }
    };

    useEffect(() => {
        window.addEventListener('beforeunload', handleSubmitLikes);

        return () => {
            window.removeEventListener('beforeunload', handleSubmitLikes);
            handleSubmitLikes();
        };
    }, [batchedLikes]);

    return (
        <>
            <h1>프로필 테스트</h1>
            <Profile />
            <h2>좋아요한 스토리</h2>
            <StoryItemList
                storyList={storyList}
                onBatchedLikesChange={handleBatchedLikesChange}
            />
            <h3>알림</h3>
            <AlarmCardList userList={userList} />
        </>
    );
};

export default MyPage;
