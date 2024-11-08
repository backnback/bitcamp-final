import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import styles from '../assets/styles/css/Faq.module.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용

const FaqBoard = () => {
    const [faqs, setFaqs] = useState([]); // FAQ 목록 상태
    const [openIndex, setOpenIndex] = useState(null); // 어떤 FAQ가 열려 있는지 추적
    const [accessToken, setAccessToken] = useState(null); // accessToken 상태 추가
    const navigate = useNavigate(); // 페이지 이동을 위한 훅

    // 로컬 스토리지에서 accessToken을 가져오는 함수
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setAccessToken(token);
        } else {
            console.warn("Access token이 없습니다.");
            // 필요하다면 여기에서 사용자를 로그인 페이지로 리디렉션 할 수 있습니다.
        }
    }, []);

    // accessToken이 설정된 경우에만 fetchList 호출
    useEffect(() => {
        const fetchFaqs = async () => {
            if (accessToken) {
                try {
                    const response = await axios.get('http://localhost:8080/faqs', {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    }); // API 요청
                    setFaqs(response.data); // 상태를 faqs로 업데이트
                } catch (error) {
                    console.error("FAQ를 불러오는 중 오류 발생!", error);
                    // 오류 발생 시 사용자에게 메시지 표시 또는 다른 처리
                }
            }
        };
        fetchFaqs(); // 함수를 호출
    }, [accessToken]);

    // FAQ 아이템 열기/닫기 토글
    const toggleFaq = (index) => {
        setOpenIndex(openIndex === index ? null : index); // 현재 열려 있으면 닫고, 아니면 연다
    };

    // FaqManage 페이지로 이동하는 함수
    const goToEditFaq = () => {
        navigate('/faqs/edit'); // /faqs/edit 경로로 이동
    };

    return (
        <div className={styles.faq__container}>
            <div className={styles.faq__header__container}>
                <p className={styles.faq__header__title}>자주 묻는 질문</p>

                <div className={styles.faq__header__text}>
                    <p>자주 묻는 질문들을 간단하게 공지합니다. </p>
                    <p>고객분들이 빠르게 해결하실 수 있습니다. </p>
                    <p>언제나 편안하고 최선을 다하는 기억하길이 되도록 정성을 다하겠습니다.</p>
                </div>


                <p className={styles.faq__header__tel}>1588-8282</p>
                <p className={styles.faq__header__time}>MON-FRI AM 10:00-PM:5:00 / LUNCH PH 1:00-2:00 / SAT, SUN,
                    HOLIDAY CLOSE</p>
            </div>
            {/*<button className={styles.edit__faq__button} onClick={goToEditFaq}>💾</button>*/}
            {/* 펜슬 버튼 */}
            <div>
                <div className={styles.faq__list__header}>
                    <p className={styles.faq__list__header__no}>NO</p>
                    <p className={styles.faq__list__header__title}>TITLE</p>
                </div>
                <ul className={styles.faq__list}>
                    {faqs.map((faq, index) => (
                        <li key={faq.id}
                            className={`${styles.faq__list__item} ${openIndex === index ? 'open' : ''}`}>
                            <div className={styles.faq__list__title} onClick={() => toggleFaq(index)}>
                                <span className={styles.faq__list__number}>{index + 1} </span>
                                <span className={styles.faq__list__question}>Q. {faq.title}</span>
                                {openIndex === index ?
                                    <i className={`icon icon__drop__up`}> </i> :
                                    <i className={`icon icon__drop__down`}></i>}
                            </div>
                            {openIndex === index && (
                                <div className={styles.faq__list__contentBox}>
                                    <p className={styles.faq__list__contentText}>A. {faq.content}</p>
                                </div>
                            )}
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default FaqBoard;
