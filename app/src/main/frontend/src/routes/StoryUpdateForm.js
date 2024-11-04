import React, { useEffect, useState } from 'react';
import axios from 'axios';
// import './StoryUpdateForm.css';
import { useParams, useNavigate } from 'react-router-dom';


const MyStoryUpdateForm = () => {
    const { storyId } = useParams();
    const navigate = useNavigate();
    const [accessToken, setAccessToken] = useState(null);

    const [title, setTitle] = useState('');
    const [travelDate, setTravelDate] = useState('');
    const [content, setContent] = useState('');
    const [locationDetail, setLocationDetail] = useState('');
    const [files, setFiles] = useState([]);
    const [firstNames, setFirstNames] = useState([]);
    const [secondNames, setSecondNames] = useState([]);
    const [selectedFirstName, setSelectedFirstName] = useState('');
    const [selectedSecondName, setSelectedSecondName] = useState('');
    const [loading, setLoading] = useState(true);


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
                    const response = await axios.get(`http://localhost:8080/my-story/view/${storyId}`, {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    });
                    const story = response.data;
                    setTitle(story.title);
                    setTravelDate(story.travelDate);
                    setContent(story.content);
                    setLocationDetail(story.locationDetail);
                    setSelectedFirstName(story.locationFirstName);
                    setSelectedSecondName(story.locationSecondName);
                    setFiles(story.photos || []);
                    setLoading(false);  // 데이터를 불러온 후 로딩 상태를 false로 설정
                } catch (error) {
                    console.error("스토리를 가져오는 중 오류가 발생했습니다!", error);
                }
            };
            fetchStoryViewDTO();
        }
    }, [accessToken, storyId]);


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
        const newFiles = Array.from(event.target.files);
        const previewFiles = newFiles.map(file => ({
            file,
            preview: URL.createObjectURL(file),  // 미리보기 URL 생성
            mainPhoto: false,  // 기본적으로 mainPhoto는 false로 설정
        }));
        setFiles([...files, ...previewFiles]);  // 기존 파일과 새로운 파일을 결합
    };


    const handleSubmit = async (event) => {
        event.preventDefault();

        const formData = new FormData();
        formData.append('title', title);
        formData.append('travelDate', travelDate);
        formData.append('locationDetail', locationDetail);
        formData.append('content', content);
        formData.append('firstName', selectedFirstName);
        formData.append('secondName', selectedSecondName);
        formData.append('oldStoryId', storyId);

        files.forEach(file => {
            if (file instanceof File) {
                formData.append('files', file);
            }
        });

        try {
            const response = await axios.post('http://localhost:8080/my-story/update', formData, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'multipart/form-data',
                },
            });
            alert('스토리가 업데이트되었습니다!');
            navigate(`/my-story/view/${storyId}`);
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
                        {file.preview ? (
                            <img src={file.preview} alt={`New Photo ${index + 1}`} />
                        ) : (
                            <img src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/story/${file.path}`} alt={`Existing Photo ${index + 1}`} />
                        )}
                        <span>{file.mainPhoto ? 'main' : ''}</span>
                    </div>
                ))}
            </div>
            <button type="submit">수정 완료</button>
        </form>
    );
};

export default MyStoryUpdateForm;
