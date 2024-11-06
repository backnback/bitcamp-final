import React, { useState } from 'react';
import styles from '../assets/styles/css/FindEmail.module.css';
import { InputProvider } from '../components/InputProvider';
import { ButtonProvider } from '../components/ButtonProvider';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const NewPassword = () => {
    const navigate = useNavigate();
    const [password, setPassword] = useState('');
    const [checkPassword, setCheckPassword] = useState('');
    const email = sessionStorage.getItem("email");

    const updatePassword = async (e) => {
        e.preventDefault();

        if (password !== checkPassword) {
            alert("비밀번호가 같지 않습니다");
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/sign/newpassword', { email, password }, {
                headers: {
                    'Content-Type': 'application/json',
                },
                withCredentials: true,
            });
            if (response.data) {
                alert("성공적으로 변경되었습니다");
                sessionStorage.removeItem("email");
                navigate('/');
            } else {
                alert("비밀번호 변경에 실패했습니다.");
            }
        } catch (error) {
            console.error("비밀번호 변경 중 오류 발생:", error);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.box}>
                <div>
                    <h2>새 비밀번호 입력</h2>
                    <div>
                        <InputProvider>
                            <input
                                type="password"
                                className="form__input"
                                id="pwd01"
                                name="newPassword"
                                value={password}
                                placeholder="새 비밀번호"
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </InputProvider>
                        <InputProvider>
                            <input
                                type="password"
                                className="form__input"
                                id="pwd02"
                                name="confirmPassword"
                                value={checkPassword}
                                placeholder="비밀번호 확인"
                                onChange={(e) => setCheckPassword(e.target.value)}
                                required
                            />
                        </InputProvider>
                        <ButtonProvider>
                            <button type="button" className="button button__primary" onClick={updatePassword}>
                                <span className="button__text">완료</span>
                            </button>
                        </ButtonProvider>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default NewPassword;
