import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // useNavigate import 추가
import axios from 'axios'; // axios를 import하여 API 요청 사용
import StoryItem from '../components/StoryItem';
import { StoryAddContext } from '../components/StoryItem';


const MyStoryList = () => {
    const [storyList, setStoryList] = useState([]);
    const [accessToken, setAccessToken] = useState(null);
    const navigate = useNavigate(); // navigate 함수를 사용하여 페이지 이동


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

    return (
        <div className="story-list">
            <h2>My 스토리</h2>
            <ul>
                <li onClick={handleAddStory}>
                    <StoryAddContext />
                </li>
                {Array.isArray(storyList) && storyList.map((storyListDTO) => (
                    <StoryItem
                        key={storyListDTO.storyId}
                        profileImg={storyListDTO.userPath || 'default.png'} // 프로필 이미지
                        profileName={storyListDTO.userNickname} // 프로필 이름
                        currentLock={!storyListDTO.share} // 공유 여부
                        storyThum={storyListDTO.mainPhoto.path || 'default.png'} // 썸네일 이미지
                        currentLike={storyListDTO.likeStatus} // 좋아요 상태
                        currentLikeCount={storyListDTO.likeCount} // 좋아요 개수
                        storyTitle={storyListDTO.title} // 스토리 제목
                        storyContent={storyListDTO.content} // 스토리 내용
                        storyLocation={`${storyListDTO.locationFirstName} ${storyListDTO.locationDetail}`} // 위치 정보
                        storyDate={storyListDTO.travelDate} // 여행 날짜
                    />
                ))}
            </ul>
        </div>
    );
};

export default MyStoryList;
