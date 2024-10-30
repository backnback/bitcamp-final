import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom'; // URL 파라미터와 페이지 이동을 위해 import
import axios from 'axios';
import './MyStoryView.css';
import { useUser } from './UserContext';

const MyStoryView = () => {
    const { id } = useParams(); // URL에서 ID 파라미터를 가져옴
    const navigate = useNavigate(); // 페이지 이동을 위한 네비게이션 훅
    const [responseMap, setResponseMap] = useState(null);
    const { user } = useUser();

    const fetchResponseMap = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/my-story/view/${id}?userId=${user.id}`);
            setResponseMap(response.data);
        } catch (error) {
            console.error("스토리를 가져오는 중 오류가 발생했습니다!", error);
        }
    };

    useEffect(() => {
        fetchResponseMap();
    }, [id]);

    const handleDelete = async () => {
        const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
        if (confirmDelete) {
            try {
                await axios.delete(`http://localhost:8080/my-story/delete/${id}?userId=${user.id}`);
                alert("스토리가 삭제되었습니다.");
                navigate('/my-story/list'); // 삭제 후 목록 페이지로 이동
            } catch (error) {
                console.error("스토리 삭제 중 오류가 발생했습니다!", error);
                alert("스토리 삭제에 실패했습니다.");
            }
        }
    };

    // Add a function to navigate to the update form
    const handleEdit = () => {
        navigate(`/my-story/form/update/${id}?userId=${user.id}`); // MyStoryUpdateForm으로 이동
    };

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

            <div className="button-group">
                <button onClick={handleEdit}>수정</button> {/* 수정 버튼 클릭 시 handleEdit 호출 */}
                <button onClick={handleDelete}>삭제</button>
            </div>
        </div>
    );
};

export default MyStoryView;
