import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './StoryUpdateForm.css';
import { useParams, useNavigate } from 'react-router-dom';

const StoryUpdateForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const [title, setTitle] = useState('');
    const [travelDate, setTravelDate] = useState('');
    const [content, setContent] = useState('');
    const [locationDetail, setLocationDetail] = useState('');
    const [files, setFiles] = useState([]); // This will hold the existing photos
    const [firstNames, setFirstNames] = useState([]);
    const [secondNames, setSecondNames] = useState([]);
    const [selectedFirstName, setSelectedFirstName] = useState('');
    const [selectedSecondName, setSelectedSecondName] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStoryData = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/story/view/${id}`);
                const story = response.data.story; // Access the story object
                setTitle(story.title);
                setTravelDate(story.travelDate);
                setContent(story.content);
                setLocationDetail(story.locationDetail);
                setSelectedFirstName(story.location.firstName);
                setSelectedSecondName(story.location.secondName);
                setFiles(response.data.photos || []); // Access the photos from the response
            } catch (error) {
                console.error("스토리 데이터를 불러오는 중 오류가 발생했습니다!", error);
            } finally {
                setLoading(false);
            }
        };
        fetchStoryData();
    }, [id]);

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
        setFiles([...files, ...event.target.files]);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const formData = new FormData();
        formData.append('title', title);
        formData.append('travelDate', travelDate);
        formData.append('locationDetail', locationDetail);
        formData.append('content', content);

        files.forEach(file => {
            if (file instanceof File) {
                formData.append('files', file);
            }
        });

        try {
            const response = await axios.post(`http://localhost:8080/story/update/${id}/${selectedFirstName}/${selectedSecondName}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            alert('스토리가 업데이트되었습니다!');
            navigate(`/story/view/${id}`);
        } catch (error) {
            console.error("스토리 업데이트 중 오류가 발생했습니다!", error);
        }
    };

    if (loading) {
        return <div>로딩 중...</div>;
    }

    return (
        <form onSubmit={handleSubmit} className="story-update-form">
            <h2>스토리 수정</h2>
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
                    {firstNames.map((name) => (
                        <option key={name} value={name}>
                            {name}
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
            <h3>현재 사진들:</h3>
            <div className="existing-photos">
                {files.map((file, index) => (
                    <div key={index} className="photo">
                        {/* Assuming 'file' has a 'path' property for the image URL */}
                        <img src={`/images${file.path}`} alt={`Photo ${index + 1}`} />
                        <span>{file.mainPhoto ? 'main' : ''}</span>
                    </div>
                ))}
            </div>
            <button type="submit">수정 완료</button>
        </form>
    );
};

export default StoryUpdateForm;
