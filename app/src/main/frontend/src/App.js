import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import SignUp from "./SignUp";
import ViewUser from "./ViewUser"; // ViewUser 컴포넌트 import
import Login from "./Login"; // Login 컴포넌트 import
import Header from "./header";
import StoryList from "./StoryList"; // StoryList 컴포넌트 import
import StoryView from "./StoryView";
import FaqList from "./FaqList";
import StoryAddForm from "./StoryAddForm";
import StoryUpdateForm from "./StoryUpdateForm";
import { UserProvider } from './UserContext'; // UserContext import

function App() {
  const [users, setUsers] = useState([]); // 사용자 목록 상태
  const [currentUser, setCurrentUser] = useState(null); // 현재 사용자 상태

  // 사용자 목록 가져오기 함수
  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8080/user/list');
      setUsers(response.data); // 사용자 목록 상태 업데이트
    } catch (error) {
      console.error("There was an error fetching the user!", error);
    }
  };

  useEffect(() => {
    fetchUsers(); // 컴포넌트가 처음 로드될 때 사용자 목록을 가져옴
  }, []);

  return (
    // UserProvider로 currentUser와 setCurrentUser 전달
    <UserProvider value={{ currentUser, setCurrentUser }}>
      <Router>
        <Header /> {/* 헤더 컴포넌트 */}
        <Routes>
          <Route path="/" element={
            <div>
              <h1>User List</h1>
              {/* 사용자 목록 렌더링 */}
              <ul>
                {Array.isArray(users) && users.map(user => (
                  <li key={user.id}>
                    <Link to={`/viewuser/${user.id}`}>{user.nickname}</Link> - {user.email}
                  </li>
                ))}
              </ul>


              {/* 링크 */}
              <Link to="/signup">회원가입</Link> {/* 회원가입 페이지로 이동 */}
              <Link to="/login">로그인</Link> {/* 로그인 페이지로 이동 */}
              <Link to="/story/list">스토리 보기</Link> {/* StoryList로 이동 */}
            </div>
          } />

          {/* 라우터 경로 설정 */}
          <Route path="/signup" element={<SignUp />} /> {/* 회원가입 페이지 */}
          <Route path="/viewuser/:id" element={<ViewUser />} /> {/* 특정 사용자 보기 */}
          <Route path="/login" element={<Login />} /> {/* 로그인 페이지 */}
          <Route path="/story/list" element={<StoryList />} /> {/* 스토리 목록 페이지 */}
          <Route path="/story/view/:id" element={<StoryView />} /> {/* 특정 스토리 보기 */}
          <Route path="/story/form/add" element={<StoryAddForm />} /> {/* 스토리 추가 */}
          <Route path="/story/form/update/:id" element={<StoryUpdateForm />} /> {/* 스토리 수정 */}
          <Route path="/faqs/list" element={<FaqList />} /> {/* FAQ 목록 페이지 */}


        </Routes>
      </Router>
    </UserProvider>
  );
}

export default App;
