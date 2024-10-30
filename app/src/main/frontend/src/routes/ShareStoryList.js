import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // useNavigate import 추가
import './ShareStoryList.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용

const ShareStoryList = () => {
    const [responseList, setResponseList] = useState([]); // 변수 이름을 stories로 수정
    const navigate = useNavigate(); // navigate 함수를 사용하여 페이지 이동

    const fetchList = async () => {
        try {
            const response = await axios.get('http://localhost:8080/share-story/list'); // API 요청
            setResponseList(response.data);
        } catch (error) {
            console.error("There was an error", error);
        }
    };

    useEffect(() => {
        fetchList();
    }, []);

    return (
        <div className="story-list">
            <h2>공유 스토리</h2>
            <ul>
                {Array.isArray(responseList) && responseList.map((map) => (
                    <li key={map.story.id} className="story-card">
                        <div className="story-header">
                            <p className="nickname">{map.story.user ? map.story.user.nickname : '익명'}</p>
                            <button className="share-button">{map.story.share ? '공유됨' : '공유하기'}</button>
                        </div>
                        {map.mainPhoto && (
                            <Link to={`/share-story/view/${map.story.id}`}>
                                <div className="image-container">
                                <img src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/story/${map.mainPhoto.path ? map.mainPhoto.path : 'default.png'}`}/>
                                </div>
                            </Link>
                        )}
                        <div className="story-info">
                            <div className="like-info">
                                <button className="like-button">좋아요</button>
                                <span>{map.likeCount}</span>
                            </div>
                            <Link to={`/share-story/view/${map.story.id}`}>{map.story.title}</Link>
                            <Link to={`/share-story/view/${map.story.id}`}>
                                <p>{map.story.content}</p>
                            </Link>
                            <div className="location-date">
                                <p>{map.story.location.firstName} {map.story.locationDetail}</p>
                                <p>{map.story.travelDate}</p>
                            </div>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ShareStoryList;
