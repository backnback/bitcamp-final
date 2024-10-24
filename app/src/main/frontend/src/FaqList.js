import React, { useEffect, useState } from 'react';
import './FaqList.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용

const FaqList = () => {
    const [faqs, setFaqs] = useState([]);
    const [openIndex, setOpenIndex] = useState(null); // Track which FAQ is open

    // API에서 FAQ 목록 가져오기
    const fetchFaqs = async () => {
        try {
            const response = await axios.get('http://localhost:8080/faqs/list'); // API 요청
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

    return (
        <div className="faq-list-container">
            <h1 className="faq-title">자주 묻는 질문</h1>
            <ul className="faq-list">
                {faqs.map((faq, index) => (
                    <li key={faq.id} className={`faq-item ${openIndex === index ? 'open' : ''}`}>
                        <div className="faq-header" onClick={() => toggleFaq(index)}>
                            <span className="faq-number">{index + 1}. </span>
                            <span className="faq-question">{faq.title}</span>
                            <span className="faq-toggle">{openIndex === index ? '▲' : '▼'}</span>
                        </div>
                        {openIndex === index && (
                            <div className="faq-content">
                                <p>{faq.content}</p>
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default FaqList;
