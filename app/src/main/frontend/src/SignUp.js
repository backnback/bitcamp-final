import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // useNavigate 추가

function SignUp() {
  const navigate = useNavigate(); // navigate 함수 선언

  // 입력 필드 상태 관리
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nickname, setNickname] = useState('');
  const [profileImage, setProfileImage] = useState(null);
  const [agree, setAgree] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault(); // 기본 폼 제출 동작 방지

    if (!agree) {
      alert("개인정보 제공 동의가 필요합니다.");
      return;
    }

    const formData = new FormData();
    formData.append('email', email);
    formData.append('password', password);
    formData.append('nickname', nickname);
    if (profileImage) {
      formData.append('profileImage', profileImage);
    }

    try {
      const response = await axios.post('http://localhost:8080/sign/up', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      // 회원가입 성공 시 App.js로 리다이렉트
      if (response.status === 200) {
        window.location.replace('/'); // 홈으로 이동
      }
    } catch (error) {
      console.error("회원가입 중 오류 발생:", error);
    }
  };

  return (
    <div>
      <section>
        <h2>회원가입</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="user-email">이메일: </label>
            <input
              type="text"
              id="email"
              placeholder="이메일을 입력해주세요."
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div>
            <label htmlFor="user-password">비밀번호: </label>
            <input
              type="password"
              id="password"
              placeholder="비밀번호를 입력해주세요."
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div>
            <label htmlFor="user-nickname">닉네임: </label>
            <input
              type="text"
              id="nickname"
              placeholder="닉네임을 입력해주세요."
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              required
            />
          </div>
          <div>
            <label htmlFor="user-profile">프로필 이미지: </label>
            <input
              type="file"
              id="profile-image"
              onChange={(e) => setProfileImage(e.target.files[0])}
            />
          </div>
          <div>
            <input
              type="checkbox"
              id="agree"
              checked={agree}
              onChange={(e) => setAgree(e.target.checked)}
              required
            />
            <label htmlFor="agree">개인정보 제공 동의</label>
          </div>
          <button type="submit">가입하기</button>
        </form>
      </section>
    </div>
  );
}

export default SignUp;
