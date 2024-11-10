import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { InputProvider } from '../components/InputProvider';
import { useNavigate } from 'react-router-dom';
import { Modals } from '../components/Modals';
import StoryUpdateForm from './StoryUpdateForm';
import StoryEditModal from '../components/StoryEditModal';
import { ModalsDispatchContext } from '../components/ModalContext';
import { ButtonProvider } from '../components/ButtonProvider';
import { PhotosProvider } from '../components/PhotosProvider';

const StoryView = ({ storyId }) => {
    const [accessToken, setAccessToken] = useState(null);
    const [storyViewDTO, setStoryViewDTO] = useState(null);
    const navigate = useNavigate();
    const { open } = useContext(ModalsDispatchContext);

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

    if (!storyViewDTO) {
        return <div>로딩 중...</div>;
    }



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
                window.location.reload();
            } catch (error) {
                console.error("스토리 삭제 중 오류가 발생했습니다!", error);
                alert("스토리 삭제에 실패했습니다.");
            }
        }
    };


    // 업데이트 버튼 처리
    const handleEdit = () => {
        {/*  navigate(`/my-story/form/update/${storyId}`); */} // MyStoryUpdateForm으로 이동
        const content = <StoryUpdateForm storyId={storyId} />
        open(StoryEditModal, {
            onSubmit: () => {
                console.log('확인 클릭');
            },
            onClose: () => {
               console.log('취소 클릭이야 클릭');
            },
            content
        });
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
          <PhotosProvider
              photos={storyViewDTO.photos}
              className="custom-photo-container"
              itemClassName="custom-photo-item"
              layout="grid"
              emptyMessage="No photos available."
          />
          <p><strong>내용:</strong> {storyViewDTO.content}</p>
          <p><strong>공유 여부 :</strong> {storyViewDTO.share ? "예" : "아니오"}</p>

          <div className="button-group">
                <ButtonProvider>
                    <button type="button" className={`button button__primary`} onClick={handleEdit}>
                        <span className={`button__text`}>수정</span>
                    </button>
                </ButtonProvider>
                <ButtonProvider>
                    <button type="button" className={`button button__whiteRed`} onClick={handleDelete}>
                        <span className={`button__text`}>삭제</span>
                    </button>
                </ButtonProvider>
          </div>
        </div>
    );
};

export default StoryView;
