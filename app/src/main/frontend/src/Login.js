import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // 로그인 후 페이지 이동을 위해 사용

function Login() {
  const [email, setEmail] = useState(''); // userId -> email로 변경
  const [password, setPassword] = useState('');
  const [saveId, setSaveId] = useState(false);
  const navigate = useNavigate(); // 페이지 이동을 위한 hook

  const handleLogin = async (e) => {
    e.preventDefault();

  const params = new URLSearchParams();
  params.append('email', email);
  params.append('password', password);

    try {
       const response = await axios.post('http://localhost:8080/sign/in', params);

      if (response.data != null) {
        // 로그인 성공 시 페이지 이동
        console.log("로그인 성공");
        window.location.replace('/'); // 홈으로 이동
      } else {
        // 로그인 실패 처리
        alert("로그인 실패: 이메일 또는 비밀번호를 확인해주세요.");
        window.location.reload(); // 페이지 새로고침
      }
    } catch (error) {
      console.error("로그인 요청 중 오류 발생:", error);
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
