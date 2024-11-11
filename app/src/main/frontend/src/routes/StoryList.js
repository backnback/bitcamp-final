import React, { useEffect, useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // useNavigate import 추가
import axios from 'axios'; // axios를 import하여 API 요청 사용
import { StoryAddContext } from '../components/StoryItem';
import StoryItemList from '../components/StoryItemList';
import StoryAddForm from './StoryAddForm';
import StoryEditModal from '../components/StoryEditModal';
import StoryView from './StoryView.js';
import useModals from '../useModals';
import { modals } from '../components/Modals';


const MyStoryList = () => {
    const [storyList, setStoryList] = useState([]);
    const [accessToken, setAccessToken] = useState(null);
    const navigate = useNavigate(); // navigate 함수를 사용하여 페이지 이동
    const [batchedLikes, setBatchedLikes] = useState([]);
    const [batchedLocks, setBatchedLocks] = useState([]);
    const { openModal } = useModals();


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
            const fetchStoryList = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/my-story/list', {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    }); // API 요청
                    setStoryList(response.data);
                } catch (error) {
                    console.error("There was an error", error);
                }
            };
            fetchStoryList();
        }
    }, [accessToken]);


     // StoryItemList에서 모아둔 like 변경 사항을 저장하는 함수
    const handleBatchedLikesChange = (newBatchedLikes) => {
        setBatchedLikes(newBatchedLikes);
    };

    // 페이지 이동이나 새로고침 시, 서버에 좋아요 변경 사항 전송
    const handleSubmitLikes = async () => {
        if (batchedLikes.length === 0) return;

        try {
            console.log(batchedLikes);
            await axios.post('http://localhost:8080/like/batch-update', batchedLikes, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
            });
            setBatchedLikes([]); // 전송 후 초기화
        } catch (error) {
            console.error("좋아요 변경 사항 전송 중 에러 발생", error);
        }
    };

    useEffect(() => {
        // 페이지 새로고침 시 전송
        window.addEventListener('beforeunload', handleSubmitLikes);

        // 페이지 이동 시 전송
        const unlisten = navigate((location) => {
            handleSubmitLikes();
        });
        return () => {
            window.removeEventListener('beforeunload', handleSubmitLikes);
            handleSubmitLikes(); // 컴포넌트 언마운트 시에도 전송
        };
    }, [batchedLikes]);



    // StoryItemList에서 모아둔 Lock 변경 사항을 저장하는 함수
    const handleBatchedLocksChange = (newBatchedLocks) => {
        setBatchedLocks(newBatchedLocks);
    };

    // 페이지 이동이나 새로고침 시, 서버에 공유 변경 사항 전송
    const handleSubmitLocks = async () => {
        if (batchedLocks.length === 0) return;

        try {
            console.log(batchedLocks);
            await axios.post('http://localhost:8080/story/batch-update', batchedLocks, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                }
            });
            setBatchedLocks([]); // 전송 후 초기화
        } catch (error) {
            console.error("공유 변경 사항 전송 중 에러 발생", error);
        }
    };

    useEffect(() => {
        // 페이지 새로고침 시 전송
        window.addEventListener('beforeunload', handleSubmitLocks);

        // 페이지 이동 시 전송
        const unlisten = navigate((location) => {
            handleSubmitLocks();
        });
        return () => {
            window.removeEventListener('beforeunload', handleSubmitLocks);
            handleSubmitLocks(); // 컴포넌트 언마운트 시에도 전송
        };
    }, [batchedLocks]);


    // 스토리 조회 모달
    const openStoryModal = (storyId) => {
        const content = <StoryView storyId={storyId} />
        openModal(modals.modalSidebarRight, {
            onSubmit: () => {
                console.log('비지니스 로직 처리...2');
            },
            content
        });
    };


    // 스토리 추가 모달
    const openAddModal = () => {
        const content = <StoryAddForm />
        openModal(modals.storyEditModal, {
            onSubmit: () => {
                console.log('비지니스 로직 처리...2');
            },
            content
        });
        //navigate("/my-story/form/add");
    };



    return (
        <div className="story-list">
            <h2>My 스토리</h2>
            <StoryItemList
                storyList={storyList}
                onAddStory={openAddModal}
                onBatchedLikesChange={handleBatchedLikesChange}
                onBatchedLocksChange={handleBatchedLocksChange}
                handleModal={openStoryModal}
            />
        </div>

    );
};
export default MyStoryList;
