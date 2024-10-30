import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
// import './FaqList.css'; // 스타일 파일 임포트
import axios from 'axios'; // axios를 import하여 API 요청 사용

const FaqList = () => {
    const [faqs, setFaqs] = useState([]);

    const fetchFaqs = async () => {
        try {
            const response = await axios.get('http://localhost:8080/faqs/list'); // API 요청
            setFaqs(response.data);
        } catch (error) {
            console.error("There was an error fetching the FAQs!", error);
        }
    };

    useEffect(() => {
        fetchFaqs();
    }, []);

    return (
        <div className="faq-list">
            <h1>자주 묻는 질문</h1>
            <ul>
                {faqs.map((faq) => (
                    <li key={faq.id}>
                        <h3>{faq.title}</h3>
                        <p>{faq.content}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default FaqList;

