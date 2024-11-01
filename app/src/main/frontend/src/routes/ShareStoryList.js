import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // useNavigate import 추가
// import './ShareStoryList.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용
import { useUser } from '../UserContext';

const ShareStoryList = () => {
    const [storyList, setStoryList] = useState([]); // 변수 이름을 stories로 수정
    const navigate = useNavigate(); // navigate 함수를 사용하여 페이지 이동
    const { user } = useUser();

    const fetchList = async () => {
        const token = localStorage.getItem('accessToken');
        console.log("Fetched token from localStorage:", token);

        try {
            const response = await axios.get(`http://localhost:8080/share-story/list/${user.userId}`, {
                headers: {
                    'Authorization': `Bearer ${token}` // user에서 토큰 가져오기
                }
            });
            setStoryList(response.data);
        } catch (error) {
            console.error("There was an error", error);
        }
    };

    useEffect(() => {
        console.log(user);
        if (user && user.userId) {
            fetchList();
        }
    }, [user]);

    return (
        <div className="story-list">
            <h2>공유 스토리</h2>
            <ul>
                {Array.isArray(storyList) && storyList.map((storyListDTO) => (
                    <li key={storyListDTO.storyId} className="story-card">
                        <div className="story-header">
                            <p className="nickname">{storyListDTO.userNickname}</p>
                            <button className="share-button">{storyListDTO.share ? '공유됨' : '공유하기'}</button>
                        </div>
                        {storyListDTO.mainPhoto && (
                            <Link to={`/share-story/view/${storyListDTO.storyId}`}>
                                <div className="image-container">
                                    <img src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/story/${storyListDTO.mainPhoto.path ? storyListDTO.mainPhoto.path : 'default.png'}`} />
                                </div>
                            </Link>
                        )}
                        <div className="story-info">
                            <div className="like-info">
                                <button className="like-button">좋아요</button>
                                <span>{storyListDTO.likeCount}</span>
                            </div>
                            <Link to={`/share-story/view/${storyListDTO.storyId}`}>{storyListDTO.title}</Link>
                            <Link to={`/share-story/view/${storyListDTO.storyId}`}>
                                <p>{storyListDTO.content}</p>
                            </Link>
                            <div className="location-date">
                                <p>{storyListDTO.locationFirstName} {storyListDTO.locationDetail}</p>
                                <p>{storyListDTO.travelDate}</p>
                            </div>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ShareStoryList;
