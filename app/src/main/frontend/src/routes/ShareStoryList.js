import React, { useEffect, useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // useNavigate import 추가
// import './ShareStoryList.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용
import StoryItemList from "../components/StoryItemList";
import ShareStoryView from './ShareStoryView.js';
import useModals from '../useModals';
import { modals } from '../components/Modals';
import { InputProvider } from '../components/InputProvider';
import { ButtonProvider } from '../components/ButtonProvider';
import { StoryTitleProvider } from '../components/TitleProvider.js';
import { SelectProvider } from '../components/SelectProvider.js';
import styles from '../assets/styles/css/StoryItemList.module.css';

const fetchStoryList = async (accessToken, searchQuery, setStoryList) => {
    try {
        const response = await axios.get('http://localhost:8080/share-story/list', {
            params: { title: searchQuery },
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });
        setStoryList(response.data);
    } catch (error) {
        console.error("There was an error", error);
    }
};

const ShareStoryList = () => {
    const [storyList, setStoryList] = useState([]);
    const [accessToken, setAccessToken] = useState(null); // accessToken 상태 추가
    const navigate = useNavigate(); // navigate 함수를 사용하여 페이지 이동
    const { token } = localStorage.getItem('accessToken');
    const [batchedLikes, setBatchedLikes] = useState([]);
    const [batchedLocks, setBatchedLocks] = useState([]);
    const { openModal } = useModals();
    const [searchQuery, setSearchQuery] = useState("");

    // 검색 값 변경
    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
        if (event.target.value == '') {
            setBatchedLocks([]);
        }
    };

    // 검색 제출 버튼
    const handleSearchSubmit = (event) => {
        event.preventDefault();
        if (accessToken) {
            fetchStoryList(accessToken, searchQuery, setStoryList);
        }
    };

    const handleSearchDelete = (event) => {
        setSearchQuery((value) => value = '');
        setBatchedLocks([]);
    };

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

    // 스토리 조회 모달
    const openStoryModal = (storyId) => {
        const content = <ShareStoryView storyId={storyId} />
        openModal(modals.modalSidebarRight, {
            onSubmit: () => {
                console.log('비지니스 로직 처리...2');
            },
            content
        });
    };

    // 로컬 스토리지에서 accessToken을 가져오는 함수
    // useEffect(() => {
    //     const token = localStorage.getItem('accessToken');
    //     if (token) {
    //         setAccessToken(token);
    //     } else {
    //         console.warn("Access token이 없습니다.");
    //     }
    // }, []);


    // accessToken이 설정된 경우에만 fetchList 호출
    // useEffect(() => {
    //     if (accessToken) {
    //         const fetchList = async () => {
    //             try {
    //                 const response = await axios.get('http://localhost:8080/share-story/list', {
    //                     headers: {
    //                         'Authorization': `Bearer ${accessToken}`
    //                     }
    //                 });
    //                 response.data.map((story, index) => {
    //                     if (story.mainPhoto.path == null) {
    //                         console.log("test")
    //                     }
    //                     console.log(`Content: ${story.mainPhoto.path}`);
    //                 })
    //                 console.log(response.data)
    //                 setStoryList(response.data);
    //             } catch (error) {
    //                 console.error("공유 스토리 목록 가져오기 실패 !", error);
    //             }
    //         };
    //         fetchList();
    //     }
    // }, [accessToken]);

    // useEffect(() => {
    //     // 페이지 새로고침 시 전송
    //     window.addEventListener('beforeunload', handleSubmitLikes);

    //     // 페이지 이동 시 전송
    //     const unlisten = navigate((location) => {
    //         handleSubmitLikes();
    //     });
    //     return () => {
    //         window.removeEventListener('beforeunload', handleSubmitLikes);
    //         handleSubmitLikes(); // 컴포넌트 언마운트 시에도 전송
    //     };
    // }, [batchedLikes]);

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setAccessToken(token);
        } else {
            console.warn("Access token이 없습니다.");
        }

        if (accessToken) {
            const fetchList = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/share-story/list', {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    });
                    response.data.map((story, index) => {
                        if (story.mainPhoto.path == null) {
                            console.log("test")
                        }
                        console.log(`Content: ${story.mainPhoto.path}`);
                    })
                    console.log(response.data)
                    setStoryList(response.data);
                } catch (error) {
                    console.error("공유 스토리 목록 가져오기 실패 !", error);
                }
            };
            fetchList();
        }

        // 페이지 새로고침 시 전송
        window.addEventListener('beforeunload', handleSubmitLocks, handleSubmitLikes);

        // 페이지 이동 시 전송
        const unlisten = navigate((location) => {
            handleSubmitLikes();
            handleSubmitLocks();
        });
        return () => {
            window.removeEventListener('beforeunload', handleSubmitLocks, handleSubmitLikes);
            handleSubmitLikes(); // 컴포넌트 언마운트 시에도 전송
            handleSubmitLocks(); // 컴포넌트 언마운트 시에도 전송
        };
    }, [accessToken, batchedLikes, batchedLocks]);

    return (
        <div className={styles.list__content__wrap}>
            <div className='search__wrap'>
                <form className="search__form" onSubmit={handleSearchSubmit}>
                    <div className='search__form__wrap'>
                        <div className={`search__form__item search__form__item__select`}>
                            <SelectProvider>
                                <select id="search-select" name="검색어" className={`form__select form__select__search`} title='검색'>
                                    <option value={'0'}>제목</option>
                                    <option value={'1'}>닉네임</option>
                                </select>
                            </SelectProvider>
                        </div>

                        <div className={`search__form__item search__form__item__input`}>
                            <InputProvider>
                                <input
                                    type='text'
                                    className={`form__input form__input__search`}
                                    value={searchQuery}
                                    onChange={handleSearchChange}
                                    required
                                    id='text01'
                                    name='검색'
                                    placeholder="검색" />
                            </InputProvider>

                            <div className={`search__input__item__button`}>
                                {searchQuery && <ButtonProvider width={'icon'} className={`button__item__x`}>
                                    <button type="button" className={`button button__icon button__icon__x`} onClick={handleSearchDelete}>
                                        <span className={`blind`}>삭제</span>
                                        <i className={`icon icon__x__black`}></i>
                                    </button>
                                </ButtonProvider>}

                                <ButtonProvider width={'icon'} className={`button__item__search`}>
                                    <button type="button" className={`button button__icon button__icon__search`} onClick={handleSearchSubmit}>
                                        <span className={`blind`}>검색</span>
                                        <i className={`icon__search`}></i>
                                    </button>
                                </ButtonProvider>
                            </div>

                        </div>
                    </div>
                </form>
            </div>

            <StoryTitleProvider
                title={'공유 스토리'}
                selectChildren={
                    <SelectProvider>
                        <select id="select01" name="스토리 정렬" className={`form__select`} title='스토리 정렬 방식 선택'>
                            <option value={'0'}>최신순</option>
                            <option value={'1'}>과거순</option>
                            <option value={'2'}>좋아요순</option>
                        </select>
                    </SelectProvider>}
            />
            <div className={styles.list__wrap}>
                <StoryItemList
                    storyPage={`share-story`}
                    storyList={storyList}
                    onBatchedLikesChange={handleBatchedLikesChange}
                    onBatchedLocksChange={handleBatchedLocksChange}
                    handleModal={openStoryModal}
                />
            </div>
        </div>
    );
};

export default ShareStoryList;
