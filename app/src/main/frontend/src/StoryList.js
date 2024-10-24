import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import './StoryList.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용

const StoryList = () => {
    const [stories, setStories] = useState([]); // 변수 이름을 stories로 수정

    const fetchStories = async () => {
        try {
            const response = await axios.get('http://localhost:8080/story/list'); // API 요청
            setStories(response.data);
        } catch (error) {
            console.error("There was an error fetching the stories!", error);
        }
    };

    useEffect(() => {
        fetchStories();
    }, []);

    return (
        <div className="story-list">
            <h2>Stories</h2>
            <ul>
                {Array.isArray(stories) && stories.map((story) => ( // stories 사용
                    <li key={story.id}>
                        <Link to={`/story/view/${story.id}`}>{story.title}</Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default StoryList;
