import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom'; // URL 파라미터와 페이지 이동을 위해 import
import axios from 'axios';
// import './ShareStoryView.css';
import { useUser } from '../UserContext';

const ShareStoryView = () => {
    const { id } = useParams(); // URL에서 ID 파라미터를 가져옴
    const navigate = useNavigate(); // 페이지 이동을 위한 네비게이션 훅
    const [responseMap, setResponseMap] = useState(null);
    const { user } = useUser();

    const fetchResponseMap = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/share-story/view/${user.userId}`, {
                headers: {
                    'Authorization': `Bearer ${user?.token}` // user에서 토큰 가져오기
                }
            }); // API 요청
            setResponseMap(response.data);
        } catch (error) {
            console.error("스토리를 가져오는 중 오류가 발생했습니다!", error);
        }
    };

    useEffect(() => {
        fetchResponseMap();
    }, [id]);



    if (!responseMap) {
        return <div>로딩 중...</div>;
    }

    const story = responseMap.story;
    const photos = responseMap.photos || [];

    return (
        <div className="story-view">
            <h2>제목 : {story.title}</h2>
            <p><strong>여행 날짜:</strong> {story.travelDate}</p>
            <p><strong>위치:</strong> {story.locationDetail}</p>
            <div className="photos">
                {photos.length > 0 ? (
                    <div className="photo-gallery">
                        {photos.map(photo => (
                            <div className="photo" key={photo.photoId}>
                                <img
                                    src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/story/${photo.path ? photo.path : 'default.png'}`}
                                    //                                    src={`/images${photo.path}`}
                                    alt={`Photo ${photo.photoId}`}
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
            <p><strong>내용:</strong> {story.content}</p>
            <p><strong>공유 여부 :</strong> {story.share ? "예" : "아니오"}</p>

        </div>
    );
};

export default ShareStoryView;
