import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../UserContext'; // UserContext를 가져옵니다.

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [saveId, setSaveId] = useState(false);
  const navigate = useNavigate();
  const { setUser } = useUser(); // UserContext에서 setUser를 가져옵니다.

  const handleLogin = async (e) => {
    e.preventDefault();

    const params = new URLSearchParams();
    params.append('email', email);
    params.append('password', password);

    try {
      const response = await axios.post('http://localhost:8080/sign/in', params);

      if (response.data) {
        // 로그인 성공 시 사용자 정보를 설정
        console.log("로그인 성공");
        setUser({ id: response.data.id, nickname: response.data.nickname }); // 사용자 정보를 UserContext에 설정
        navigate('/'); // useNavigate로 홈으로 이동
      } else {
        // 로그인 실패 처리
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
        </form>
      </section>
    </div>
  );
}

export default Login;
