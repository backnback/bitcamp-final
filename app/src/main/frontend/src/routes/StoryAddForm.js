import React, { useEffect, useState } from 'react';
import axios from 'axios';
// import './StoryAddForm.css'; // CSS 파일을 가져옵니다
import { useUser } from '../UserContext';

const MyStoryAddForm = () => {
    const [title, setTitle] = useState('');
    const [travelDate, setTravelDate] = useState('');
    const [content, setContent] = useState('');
    const [locationDetail, setLocationDetail] = useState(''); // 사용자가 입력할 위치 상세 필드
    const [files, setFiles] = useState([]);
    const [firstNames, setFirstNames] = useState([]);
    const [secondNames, setSecondNames] = useState([]);
    const [selectedFirstName, setSelectedFirstName] = useState('');
    const [selectedSecondName, setSelectedSecondName] = useState('');
    const { user } = useUser(); // UserContext에서 user를 가져옵니다.

    useEffect(() => {
        const fetchFirstNames = async () => {
            try {
                const response = await axios.get('http://localhost:8080/location/list');
                setFirstNames(response.data);
            } catch (error) {
                console.error("로케이션 가져오는 중 오류가 발생했습니다!", error);
            }
        };
        fetchFirstNames();
    }, []);

    useEffect(() => {
        const fetchSecondNames = async () => {
            if (selectedFirstName) {
                try {
                    const response = await axios.get(`http://localhost:8080/location/list/${selectedFirstName}`);
                    setSecondNames(response.data);
                } catch (error) {
                    console.error("두 번째 이름 가져오는 중 오류가 발생했습니다!", error);
                }
            } else {
                setSecondNames([]);
            }
        };
        fetchSecondNames();
    }, [selectedFirstName]);

    const handleFileChange = (event) => {
        setFiles(event.target.files);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const formData = new FormData();
        formData.append('title', title);
        formData.append('travelDate', travelDate);
        formData.append('locationDetail', locationDetail); // 사용자가 입력한 위치 상세
        formData.append('content', content);

        if (user && user.id) {
            formData.append('userId', user.id);  // 로그인 사용자 정보 전달
        }

        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }

        try {
            await axios.post(`http://localhost:8080/my-story/add/${selectedFirstName}/${selectedSecondName}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            alert('스토리가 추가되었습니다!');
            window.location.href = '/my-story/list';  // My 스토리의 list 페이지로 이동
        } catch (error) {
            console.error("스토리 추가 중 오류가 발생했습니다!", error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="story-add-form">
            <h2>스토리 추가</h2>
            <input
                type="text"
                placeholder="제목"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
            />
            <input
                type="date"
                value={travelDate}
                onChange={(e) => setTravelDate(e.target.value)}
                required
            />
            <div className="location-select-group">
                <select onChange={(e) => setSelectedFirstName(e.target.value)} value={selectedFirstName}>
                    <option value="">지역 선택</option>
                    {firstNames.map((firstName) => (
                        <option key={firstName} value={firstName}>
                            {firstName}
                        </option>
                    ))}
                </select>
                <select onChange={(e) => setSelectedSecondName(e.target.value)} value={selectedSecondName} disabled={!selectedFirstName}>
                    <option value="">세부 지역 선택</option>
                    {secondNames.map((location) => (
                        <option key={location.id} value={location.secondName}>
                            {location.secondName}
                        </option>
                    ))}
                </select>
            </div>
            <input
                type="text"
                placeholder="지역 상세정보 입력"
                value={locationDetail}
                onChange={(e) => setLocationDetail(e.target.value)}
                required
            />
            <textarea
                placeholder="내용"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                required
            />
            <input
                type="file"
                multiple
                onChange={handleFileChange}
            />
            <button type="submit">추가</button>
        </form>
    );
};

export default MyStoryAddForm;
