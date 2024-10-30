import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import SignUp from "./SignUp";
import ViewUser from "./ViewUser"; // ViewUser 컴포넌트 import
import Login from "./Login"; // Login 컴포넌트 import
import Header from "./header";
import ShareStoryList from "./ShareStoryList"; // ShareStoryList 컴포넌트 import
import MyStoryList from "./MyStoryList"; // MyStoryList 컴포넌트 import
import MyStoryView from "./MyStoryView";
import ShareStoryView from "./ShareStoryView";
import FaqList from "./FaqList";
import MyStoryAddForm from "./MyStoryAddForm";
import MyStoryUpdateForm from "./MyStoryUpdateForm";
import { UserProvider, useUser } from './UserContext'; // UserContext import

function App() {
  // UserProvider 내부에서 useUser 훅을 호출하여 사용자 정보 가져오기
  const { user, logout } = useUser();
  const [users, setUsers] = useState([]); // 사용자 목록 상태

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
    <div>
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

            <div>
              {user ? (
                <Link to="/" onClick={logout} >로그아웃</Link>
              ) : (
                <>
                  <Link to="/login">로그인</Link>
                  <Link to="/signup">회원가입</Link>
                </>
              )}
            </div>
          </div>
        } />

        {/* 라우터 경로 설정 */}
        <Route path="/signup" element={<SignUp />} /> {/* 회원가입 페이지 */}
        <Route path="/viewuser/:id" element={<ViewUser />} /> {/* 특정 사용자 보기 */}
        <Route path="/login" element={<Login />} /> {/* 로그인 페이지 */}
        <Route path="/share-story/list" element={<ShareStoryList />} /> {/* 공유 스토리 목록 페이지 */}
        <Route path="/my-story/list" element={<MyStoryList />} /> {/* My 스토리 목록 페이지 */}
        <Route path="/my-story/view/:id" element={<MyStoryView />} /> {/* 특정 My 스토리 보기 */}
        <Route path="/share-story/view/:id" element={<ShareStoryView />} /> {/* 특정 공유 스토리 보기 */}
        <Route path="/my-story/form/add" element={<MyStoryAddForm />} /> {/* My 스토리 추가 */}
        <Route path="/my-story/form/update/:id" element={<MyStoryUpdateForm />} /> {/* My 스토리 수정 */}
        <Route path="/faqs/list" element={<FaqList />} /> {/* FAQ 목록 페이지 */}
      </Routes>
    </div>
  );
}

// UserProvider로 App 컴포넌트 감싸기
function Main() {
  return (
    <UserProvider>
      <Router>
        <App />
      </Router>
    </UserProvider>
  );
}

export default Main;
