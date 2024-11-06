import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { InputProvider } from "../components/InputProvider";
import { ButtonProvider } from '../components/ButtonProvider';
import FormFileIcon from "../components/FormFileIcon";
import styles from '../assets/styles/css/SignUp.module.css';

function SignUp() {
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nickname, setNickname] = useState('');
  const [profileImage, setProfileImage] = useState(null);
  const [agree, setAgree] = useState(false);
  const [authCode, setAuthCode] = useState('');
  const [isVerified, setIsVerified] = useState(false); // 이메일 인증 상태 변수

  const handleFileChange = (e) => {
    setProfileImage(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (!isVerified) { // flag 대신 isVerified 사용
      alert("인증이 완료되지 않은 이메일입니다");
      return;
    }
  
    const formData = new FormData();
    formData.append('email', email);
    formData.append('password', password);
    formData.append('nickname', nickname);
  
    // 프로필 이미지가 있을 때만 추가
    if (profileImage) {
      formData.append('profileImage', profileImage);
    }
  
    try {
      const response = await axios.post('http://localhost:8080/sign/up', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
  
      if (response.status === 200) {
        navigate('/');
      }
    } catch (error) {
      console.error("회원가입 중 오류 발생:", error);
    }
  };
  

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
      console.error("인증번호 요청 중 오류 발생:", error);
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
        setIsVerified(true); 
      }else{
        alert("인증코드가 알맞지 않습니다 다시입력해 주세요");
      }
    } catch (error) {
      console.error("인증번호 요청 중 오류 발생:", error);
    }
  };
  

  return (
    <div className={styles.signupContainer}>
      <section className={styles.signupBox}>
        <h2>회원가입</h2>
        <form onSubmit={handleSubmit}>
          <div className={styles.inputGroup}>
            <label>이메일 <span className={styles.required}>*</span></label>
            <div className={styles.inputWrapper}>
              <InputProvider>
                <input
                  type="email"
                  className="form__input"
                  id="email01"
                  name="이메일"
                  value={email}
                  placeholder="이메일 입력" 
                  onChange={(e) => setEmail(e.target.value)} 
                  required
                />
              </InputProvider>
              <ButtonProvider>
                <button type="button" className="button button__primary" onClick={getUserAuthCode}>
                  <span className="button__text">인증번호 받기</span>
                </button>
              </ButtonProvider>
            </div>
          </div>
          <div className={styles.inputGroup}>
            <label>인증번호 <span className={styles.required}>*</span></label>
            <div className={styles.inputWrapper}>
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
          <div className={styles.inputGroup}>
            <label>비밀번호 <span className={styles.required}>*</span></label>
            <InputProvider>
              <input
                type="password"
                className="form__input"
                id="pwd01"
                name="비밀번호"
                value={password}
                placeholder="비밀번호" 
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </InputProvider>
          </div>
          <div className={styles.inputGroup}>
            <label>닉네임 <span className={styles.required}>*</span></label>
            <InputProvider>
              <input 
                type="text" 
                placeholder="닉네임" 
                value={nickname} 
                className="form__input" 
                onChange={(e) => setNickname(e.target.value)}
                required
              />
            </InputProvider>
          </div>
          <div className={styles.inputGroup}>
            <label>프로필 사진</label>
            {profileImage && <span>{ "(" + profileImage.name+ ")"}</span>}
            <div className={styles.inputWrapper}>
              <InputProvider>
                <label htmlFor="file01" className="form__label form__label__file">
                  <input type="file" className="blind" id="file01" onChange={handleFileChange} />
                  <FormFileIcon />
                </label>
              </InputProvider>
            </div>
          </div>
          <div className={styles.checkboxGroup}>
            <InputProvider>
              <label htmlFor="checkbox02" className="form__label form__label__checkbox">
                <input
                  type="checkbox"
                  checked={agree}
                  onChange={(e) => setAgree(e.target.checked)}
                  className="form__input"
                  id="checkbox02"
                  name="개인정보 동의"
                  required
                />
                <span className="input__text">개인 정보 이용 동의</span>
              </label>
            </InputProvider>
          </div>
          <ButtonProvider>
            <button type="submit" className="button button__primary">
              <span className="button__text">회원가입</span>
            </button>
          </ButtonProvider>
        </form>
      </section>
    </div>
  );
}

export default SignUp;
