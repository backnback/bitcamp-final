import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom'; // URL 파라미터를 사용하기 위해 import
import axios from 'axios'; // axios를 사용하여 API 요청
import './StoryView.css';

const StoryView = () => {
    const { id } = useParams(); // URL에서 ID 파라미터를 가져옴
    const [story, setStory] = useState(null); // 스토리 상태

    const fetchStory = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/story/view/${id}`); // 스토리 API 요청
            setStory(response.data); // 스토리 상태 업데이트
        } catch (error) {
            console.error("스토리를 가져오는 중 오류가 발생했습니다!", error);
        }
    };

    useEffect(() => {
        fetchStory(); // 컴포넌트가 마운트될 때 스토리 데이터 가져오기
    }, [id]);

    if (!story) {
        return <div>로딩 중...</div>; // 스토리 데이터가 로딩 중일 때 표시
    }

    return (
        <div className="story-view">
            <h2>{story.title}</h2>
            <p><strong>공유 여부:</strong> {story.share ? "예" : "아니오"}</p>
            <p>{story.travelDate}</p>
            <p>{story.createDate}</p>
            <p><strong>위치:</strong> {story.locationDetail}</p>
            <p><strong>내용:</strong> {story.content}</p>

        </div>
    );
};

export default StoryView;
