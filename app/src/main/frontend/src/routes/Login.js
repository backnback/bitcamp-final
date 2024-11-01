import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import { useUser } from '../UserContext';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [saveId, setSaveId] = useState(false);
  const navigate = useNavigate();
  const { setUser } = useUser();

  // 페이지가 로드될 때 토큰을 확인하고 유저 정보를 설정
  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      const userInfo = jwtDecode(token);
      setUser(userInfo); // UserContext에 유저 정보 설정
    }
  }, [setUser]);

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/sign/in', {
        email: email,
        password: password
      }, {
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (response.data) {
        const { accessToken } = response.data;

        // 토큰을 로컬 스토리지에 저장
        localStorage.setItem('accessToken', accessToken);

        // 토큰 디코딩하여 유저 정보 추출
        const userInfo = jwtDecode(accessToken);
        setUser(userInfo); // UserContext에 유저 정보 설정
        navigate('/'); // 홈 페이지로 이동
      } else {
        alert("로그인 실패: 이메일 또는 비밀번호를 확인해주세요.");
      }
    } catch (error) {
      console.error("로그인 요청 중 오류 발생:", error);
      alert("로그인 요청 중 오류가 발생했습니다. 나중에 다시 시도해주세요.");
    }
  };

  return (
    <div>
      <section>
        <h2>로그인</h2>
        <form onSubmit={handleLogin}>
          <div>
            <label htmlFor="email">이메일: </label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="이메일을 입력해주세요."
              required
            />
          </div>
          <div>
            <label htmlFor="user-password">비밀번호: </label>
            <input
              type="password"
              id="user-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="비밀번호를 입력해주세요."
              required
            />
          </div>
          <div className="area_label">
            <input
              type="checkbox"
              id="save-id"
              checked={saveId}
              onChange={(e) => setSaveId(e.target.checked)}
            />
            <label htmlFor="save-id">아이디 저장</label>
          </div>
          <button type="submit">로그인</button>
            <Link to="/signup"  >회원가입</Link>
        </form>
      </section>
    </div>
  );
}

export default Login;
