import React, { useState } from 'react';
import styles from '../assets/styles/css/FindEmail.module.css';
import { InputProvider } from '../components/InputProvider';
import { ButtonProvider } from '../components/ButtonProvider';
import axios from 'axios';

const FindEmail = () => {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState(''); // 가입 여부 메시지를 저장할 상태 추가

    const getUserEmail = async (e) => {
        e.preventDefault();
      
        if (!email) {
            alert("이메일을 입력해주세요");
            return;
        }
      
        try {
            const response = await axios.post('http://localhost:8080/sign/findemail', { email }, {
                headers: {
                    'Content-Type': 'application/json',
                },
                withCredentials: true,
            });
            if (response.data) {
                setMessage("해당 아이디는 가입되어있는 아이디 입니다");
            } else {
                setMessage("해당 아이디는 가입되어있지 않은 아이디 입니다");
            }
        } catch (error) {
            console.error("이메일 찾는중 오류 발생", error);
            setMessage("오류가 발생했습니다. 다시 시도해주세요.");
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.box}>
                <div>
                    <h2>아이디 찾기</h2>
                    <div>
                        <InputProvider>
                            <input
                                type="email"
                                className="form__input"
                                id="email01"
                                name="이메일"
                                placeholder="이메일 입력"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)} // 이메일 상태 업데이트
                                required
                            />
                        </InputProvider>
                        <ButtonProvider>
                            <button type="button" className="button button__primary" onClick={getUserEmail}>
                                <span className="button__text">아이디 찾기</span>
                            </button>
                        </ButtonProvider>
                    </div>
                    {message && <div className={styles.message}>{message}</div>} {/* 메시지를 화면에 표시 */}
                </div>
            </div>
        </div>
    );
};

export default FindEmail;
