import React, { useEffect, useState } from 'react';
import axios from 'axios';
// import './StoryUpdateForm.css';
import { useParams, useNavigate } from 'react-router-dom';
import { InputProvider } from '../components/InputProvider';
import { SelectProvider } from '../components/SelectProvider';
import { ButtonProvider } from '../components/ButtonProvider';
import styles from "../assets/styles/css/StoryItem.module.css";
import Swal from 'sweetalert2';


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
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
    const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1); // 월은 0부터 시작
    const [selectedDay, setSelectedDay] = useState(new Date().getDate());
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


    useEffect(() => {
        // 연도, 월, 일이 변경될 때 travelDate 업데이트
        setTravelDate(`${selectedYear}-${String(selectedMonth).padStart(2, '0')}-${String(selectedDay).padStart(2, '0')}`);
    }, [selectedYear, selectedMonth, selectedDay]);


    // 파일 추가 로직을 변경하여 File 객체만 유지
    const handleFileChange = (event) => {
        const newFiles = Array.from(event.target.files);
        setFiles([...files, ...newFiles]); // File 객체만 추가
    };


    useEffect(() => {
        console.log("Current States:", {
            title,
            travelDate,
            content,
            locationDetail,
            selectedFirstName,
            selectedSecondName,
            selectedYear,
            selectedMonth,
            selectedDay,
            files
        });
    }, [title, travelDate, content, locationDetail, selectedFirstName, selectedSecondName, selectedYear, selectedMonth, selectedDay, files]);


    const handleSubmit = async (event) => {
        event.preventDefault();

        // 유효성 검사
        if (!title || !travelDate || !content || !locationDetail) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "모든 필수 항목을 입력해주세요!",
                footer: '<a href="#">왜 이 문제가 발생했나요?</a>'
            });
            return;
        }

        const formData = new FormData();
        formData.append('title', title);
        formData.append('travelDate', travelDate);
        formData.append('locationDetail', locationDetail);
        formData.append('content', content);
        formData.append('firstName', selectedFirstName);
        formData.append('secondName', selectedSecondName);
        formData.append('oldStoryId', storyId);
        formData.append('share', false);

        files.forEach(file => {
            if (file instanceof File) {
                formData.append('files', file);
            }
        });

        try {
            console.log(formData);
            const response = await axios.post('http://localhost:8080/my-story/update', formData, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'multipart/form-data',
                },
            });

            console.log(formData.getAll('files'));

            Swal.fire({
              position: "top",
              icon: "success",
              title: "스토리가 업데이트되었습니다!",
              showConfirmButton: false,
              timer: 1500
            }).then(() => {
              // 3초 후 페이지 이동
              navigate(`/my-story/view/${storyId}`);
            });
        } catch (error) {
            console.error("스토리 업데이트 중 오류가 발생했습니다!", error);
        }
    };


    const handleButtonClick = () => {
        handleSubmit(new Event('submit', { cancelable: true }));
    };


    if (loading) {
        return <div>로딩 중...</div>;
    }

    return (
        <form onSubmit={handleSubmit} className="story-update-form">
            <h2>스토리 수정</h2>

            <InputProvider>
                <input
                    type='text'
                    className={`form__input`}
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                    id='text01'
                    name='텍수투'
                    placeholder="제목" />
            </InputProvider>

            <SelectProvider>
                <select
                    value={selectedYear}
                    onChange={(e) => setSelectedYear(e.target.value)}
                    id="select01" name="selectYear" className={`form__select`}>
                    <option value={'0'}>년</option>
                    {[...Array(10)].map((_, index) => {
                        const year = new Date().getFullYear() - index;
                        return <option key={year} value={year}>{year}</option>;
                    })}
                </select>
            </SelectProvider>

            <SelectProvider>
                <select
                    value={selectedMonth}
                    onChange={(e) => setSelectedMonth(Number(e.target.value))}
                    id="select02" name="selectMonth" className={`form__select`}>
                    <option value={'0'}>달</option>
                    {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12].map(month => (
                        <option key={month} value={month}>{month}</option>
                    ))}
                </select>
            </SelectProvider>

            <SelectProvider>
                <select
                    value={selectedDay}
                    onChange={(e) => setSelectedDay(Number(e.target.value))}
                    id="select03" name="selectDay" className={`form__select`}>
                    <option value={'0'}>일</option>
                    {[...Array(31)].map((_, index) => {
                        const day = index + 1;
                        return <option key={day} value={day}>{day}</option>;
                    })}
                </select>
            </SelectProvider>

            <SelectProvider>
                <select id="select01" name="selectFirstName" className={`form__select`}
                    onChange={(e) => setSelectedFirstName(e.target.value)} value={selectedFirstName}>
                    <option value={'0'}>지역 선택</option>
                    {firstNames.map((firstName) => (
                        <option key={firstName} value={firstName}>
                            {firstName}
                        </option>
                    ))}
                </select>
            </SelectProvider>

            <SelectProvider>
                <select id="select02" name="selectSecondName" className={`form__select`}
                    onChange={(e) => setSelectedSecondName(e.target.value)} value={selectedSecondName} disabled={!selectedFirstName}>
                    <option value={'0'}>세부 지역 선택</option>
                    {secondNames.map((location) => (
                        <option key={location.id} value={location.secondName}>
                            {location.secondName}
                        </option>
                    ))}
                </select>
            </SelectProvider>

            <InputProvider>
                <input
                    type='text'
                    className={`form__input`}
                    value={locationDetail}
                    onChange={(e) => setLocationDetail(e.target.value)}
                    required
                    id='text02'
                    name='텍수투'
                    placeholder="지역 상세정보 입력" />
            </InputProvider>

            <InputProvider>
                <textarea
                    id='textarea01'
                    placeholder='내용 입력'
                    className={`form__textarea`}
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    required
                ></textarea>
            </InputProvider>

            <input
                type="file"
                multiple
                onChange={handleFileChange}
            />
            <h3>현재 사진들:</h3>
            <div className="existing-photos">
                {Array.isArray(files) && files.length > 0 ? (
                    files.map((file, index) => (
                        <div key={index} className={styles.middle}>
                            {file.preview ? (
                                <img src={file.preview} alt={`New Photo ${index + 1}`} className={styles.thumnail} />
                            ) : (
                                <img src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/story/${file.path}`} alt={`Existing Photo ${index + 1}`} />
                            )}
                            <span>{file.mainPhoto ? 'main' : ''}</span>
                        </div>
                    ))
                ) : (
                    <p>사진이 없습니다.</p> // 사진이 없을 경우 메시지
                )}
            </div>

            <ButtonProvider>
                <button type="button" id="submit-button" className={`button button__primary`} onClick={handleButtonClick}>
                    <span className={`button__text`}>수정</span>
                </button>
            </ButtonProvider>
        </form>
    );
};

export default MyStoryUpdateForm;
