import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import FaqStyles from '../assets/styles/css/Faq.module.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용

const FaqBoard = () => {
    const [faqs, setFaqs] = useState([]);
    const [openIndex, setOpenIndex] = useState(null); // Track which FAQ is open
    const navigate = useNavigate(); // 페이지 이동을 위한 훅


    // API에서 FAQ 목록 가져오기
    const fetchFaqs = async () => {
        try {
            const response = await axios.get('http://localhost:8080/faqs'); // API 요청
            setFaqs(response.data);
        } catch (error) {
            console.error("FAQ를 불러오는 중 오류 발생!", error);
        }
    };

    useEffect(() => {
        fetchFaqs(); // 컴포넌트가 마운트될 때 FAQ 불러오기
    }, []);

    // FAQ 아이템 열기/닫기 토글
    const toggleFaq = (index) => {
        setOpenIndex(openIndex === index ? null : index); // 현재 열려 있으면 닫고, 아니면 연다
    };

    // FaqManage 페이지로 이동하는 함수
    const goToEditFaq = () => {
        navigate('/faqs/edit'); // /faqs/add 경로로 이동
    };

    return (
            <div className={FaqStyles.faq__list__container}>
                <h1 className={FaqStyles.faq__title}>자주 묻는 질문</h1>
                <button className={FaqStyles.edit__faq__button} onClick={goToEditFaq}>💾</button> {/* 펜슬 버튼 */}
                <ul className={FaqStyles.faq__list}>
                    {faqs.map((faq, index) => (
                        <li key={faq.id} className={`${FaqStyles.faq__item} ${openIndex === index ? 'open' : ''}`}>
                            <div className={FaqStyles.faq__header} onClick={() => toggleFaq(index)}>
                                <span className={FaqStyles.faq__number}>{index + 1}. </span>
                                <span className={FaqStyles.faq__question}>{faq.title}</span>
                                <span className={FaqStyles.faq__toggle}>{openIndex === index ? '⋀' : '⋁'}</span>
                            </div>
                            {openIndex === index && (
                                <div className={FaqStyles.faq__content}>
                                    <p>{faq.content}</p>
                                </div>
                            )}
                        </li>
                    ))}
                </ul>
            </div>
        );
    };

export default FaqBoard;