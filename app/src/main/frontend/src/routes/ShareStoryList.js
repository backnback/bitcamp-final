import React, {useEffect, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom'; // useNavigate import 추가
// import './ShareStoryList.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용
import {useUser} from '../UserContext';
import StoryItem from "../components/StoryItem";

const ShareStoryList = () => {
    const [storyList, setStoryList] = useState([]);
    const [accessToken, setAccessToken] = useState(null); // accessToken 상태 추가
    const navigate = useNavigate(); // navigate 함수를 사용하여 페이지 이동
    const {user} = useUser();

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
            const fetchList = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/share-story/list', {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    });
                    response.data.map((story, index) => {
                        if(story.mainPhoto.path == null){
                            console.log("test")
                        }
                        console.log(`Content: ${story.mainPhoto.path}`);
                    })
                    console.log(response.data)
                    setStoryList(response.data);
                } catch (error) {
                    console.error("공유 스토리 목록 가져오기 실패 !", error);
                }
            };
            fetchList();
        }
    }, [accessToken]);


    return (
        <div className="story-list">
            <h2>공유 스토리</h2>
            <ul>
                {Array.isArray(storyList) && storyList.map((storyListDTO) => (
                        <StoryItem
                            key={storyListDTO.storyId}
                            currentLike={storyListDTO.likeStatus}
                            currentLock={!storyListDTO.share}
                            currentLikeCount={storyListDTO.likeCount}
                            profileName={storyListDTO.userNickname}
                            storyContent={storyListDTO.content}
                            storyDate={storyListDTO.travelDate}
                            storyTitle={storyListDTO.title}
                            storyLocation={storyListDTO.locationFirstName + storyListDTO.locationDetail}
                            storyThum={storyListDTO.mainPhoto.path || 'default.png'}
                            profileImg={storyListDTO.userPath || 'default.png'}
                        />
                ))}
            </ul>
        </div>
    )
        ;
};

export default ShareStoryList;
