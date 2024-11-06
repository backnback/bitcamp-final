import React, { useState, useEffect} from 'react';
import styles from '../assets/styles/css/FindEmail.module.css';
import { InputProvider } from '../components/InputProvider';
import { ButtonProvider } from '../components/ButtonProvider';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const FindPassword = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [authCode, setAuthCode] = useState(''); // 가입 여부 메시지를 저장할 상태 추가

    const getUserAuthCode = async (e) => {
        e.preventDefault();
      
        if (!email) {
            alert("이메일을 입력해주세요");
            return;
        }
      
        try {
            const response = await axios.post('http://localhost:8080/sign/emailverification', { email }, {
                headers: {
                    'Content-Type': 'application/json',
                },
                withCredentials: true,
            });
            if (response.data) {
                alert("인증번호가 이메일로 발송되었습니다.");
            } else {
                alert("인증번호 발송에 실패했습니다.");
            }
        } catch (error) {
            console.error("이메일 찾는중 오류 발생", error);

        }
    };

    const setUserAuthCode = async (e) => {
        e.preventDefault();
      
        if (!authCode) {
          alert("인증번호를 입력해주세요");
          return;
        }
      
        try {
          const response = await axios.post('http://localhost:8080/sign/verificationcode', { authCode }, {
            headers: {
              'Content-Type': 'application/json',
            },
            withCredentials: true, // 쿠키 사용 시 설정
          });
          if(response.data){
            alert("정상적으로 처리 되었습니다");
            sessionStorage.setItem("email", email);
            navigate('/newPassword');
          }else{
            alert("인증코드가 알맞지 않습니다 다시입력해 주세요");
          }

        } catch (error) {
          console.error("인증번호 요청 중 오류 발생:", error);
        }
      };

    return (
        <div className={styles.container}>
            <div className={styles.box}>
                <div>
                    <h2>비밀번호 찾기</h2>
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
                            <button type="button" className="button button__primary" onClick={getUserAuthCode}>
                                <span className="button__text">인증번호 받기</span>
                            </button>
                        </ButtonProvider>
                    </div>
                    <div >
            <label>인증번호</label>
            <div>
            <InputProvider>
              <input 
                type="text" 
                placeholder="인증번호" 
                value={authCode} 
                className="form__input" 
                onChange={(e) => setAuthCode(e.target.value)}
                required
              />
            </InputProvider>
            <ButtonProvider>
                <button type="button" className="button button__primary" onClick={setUserAuthCode}>
                  <span className="button__text">인증확인</span>
                </button>
              </ButtonProvider>
              </div>
          </div>
                </div>
            </div>
        </div>
    );
};

export default FindPassword;
