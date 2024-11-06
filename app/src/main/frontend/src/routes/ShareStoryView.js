import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom'; // URL 파라미터와 페이지 이동을 위해 import
import axios from 'axios';
// import './ShareStoryView.css';

const ShareStoryView = () => {
    const { storyId } = useParams(); // URL에서 ID 파라미터를 가져옴
    const navigate = useNavigate(); // 페이지 이동을 위한 네비게이션 훅
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
            <div className="photos">
                {photos.length > 0 ? (
                    <div className="photo-gallery">
                        {photos.map(photo => (
                            <div className="photo" key={photo.id}>
                                <img
                                    src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/story/${photo.path ? photo.path : 'default.png'}`}
                                    //                                    src={`/images${photo.path}`}
                                    alt={`Photo ${photo.id}`}
                                    className={`story-photo ${photo.mainPhoto ? 'main-photo' : ''}`} // Apply main-photo class if it's the main photo
                                />
                                {photo.mainPhoto && <span className="main-label">main</span>}
                            </div>
                        ))}
                    </div>
                ) : (
                    <p>사진이 없습니다.</p>
                )}
            </div>
            <p><strong>내용:</strong> {storyViewDTO.content}</p>
            <p><strong>공유 여부 :</strong> {storyViewDTO.share ? "예" : "아니오"}</p>

        </div>
    );
};

export default ShareStoryView;
