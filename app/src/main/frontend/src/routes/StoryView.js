import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom'; // URL 파라미터와 페이지 이동을 위해 import
import axios from 'axios';
// import './StoryView.css';

const MyStoryView = () => {
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


    // accessToken이 설정된 경우에만 fetchList 호출
    useEffect(() => {
        if (accessToken) {
            const fetchStoryViewDTO = async () => {
                try {
                    const response = await axios.get(`http://localhost:8080/my-story/view/${storyId}`, {
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


    // 삭제 버튼 처리
    const handleDelete = async () => {
        const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
        if (confirmDelete) {
            try {
                await axios.delete(`http://localhost:8080/my-story/delete/${storyId}`, {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`
                    }
                });
                alert("스토리가 삭제되었습니다.");
                navigate('/my-story/list'); // 삭제 후 목록 페이지로 이동
            } catch (error) {
                console.error("스토리 삭제 중 오류가 발생했습니다!", error);
                alert("스토리 삭제에 실패했습니다.");
            }
        }
    };


    // 업데이트 버튼 처리
    const handleEdit = () => {
        navigate(`/my-story/form/update/${storyId}`); // MyStoryUpdateForm으로 이동
    };

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

            <div className="button-group">
                <button onClick={handleEdit}>수정</button> {/* 수정 버튼 클릭 시 handleEdit 호출 */}
                <button onClick={handleDelete}>삭제</button>
            </div>
        </div>
    );
};

export default MyStoryView;
