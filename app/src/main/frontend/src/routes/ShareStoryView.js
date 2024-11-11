import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { PhotosProvider } from '../components/PhotosProvider';


const ShareStoryView = ({ storyId }) => {
    const [accessToken, setAccessToken] = useState(null);
    const [storyViewDTO, setStoryViewDTO] = useState(null);


    // 로컬 스토리지에서 accessToken을 가져오는 함수
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
            const fetchStoryViewDTO = async () => {
                try {
                    const response = await axios.get(`http://localhost:8080/share-story/view/${storyId}`, {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    });
                    setStoryViewDTO(response.data);
                } catch (error) {
                    console.error("스토리를 가져오는 중 오류가 발생했습니다!", error);
                }
            };
            fetchStoryViewDTO();
        }
    }, [accessToken, storyId]);


    if (!storyViewDTO) {
        return <div>로딩 중...</div>;
    }

    const photos = storyViewDTO.photos || [];

    return (
        <div className="story-view">
            <h2>제목 : {storyViewDTO.title}</h2>
            <p><strong>여행 날짜:</strong> {storyViewDTO.travelDate}</p>
            <p><strong>위치:</strong> {storyViewDTO.locationDetail}</p>
            <PhotosProvider
              photos={storyViewDTO.photos}
              viewMode={true}
              className="custom-photo-container"
              itemClassName="custom-photo-item"
              layout="grid"
            />
            <p><strong>내용:</strong> {storyViewDTO.content}</p>
            <p><strong>공유 여부 :</strong> {storyViewDTO.share ? "예" : "아니오"}</p>

        </div>
    );
};

export default ShareStoryView;
