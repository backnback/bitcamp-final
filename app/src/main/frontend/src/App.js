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
import { UserProvider } from './UserContext'; // UserContext import

function App() {
  const [users, setUsers] = useState([]);
  const [currentUser, setCurrentUser] = useState(null); // 현재 사용자 상태 추가

  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8080/user/list');
      setUsers(response.data);
    } catch (error) {
      console.error("There was an error fetching the user!", error);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <UserProvider value={{ currentUser, setCurrentUser }}> {/* UserProvider로 사용자 정보 전달 */}
      <Router>
        <Header />
        <Routes>
          <Route path="/" element={
            <div>
              <h1>User List</h1>
              <ul>
                {Array.isArray(users) && users.map(user => (
                  <li key={user.id}>
                    <Link to={`/viewuser/${user.id}`}>{user.nickname}</Link> - {user.email}
                  </li>
                ))}
              </ul>

              <img src="http://localhost:8080/images/test.jpg" alt="Test" />

              <Link to="/signup">회원가입</Link>
              <Link to="/login">로그인</Link>
              <Link to="/story/list">스토리 보기</Link> {/* StoryList로 이동하는 링크 추가 */}
            </div>
          } />

        <Route path="/signup" element={<SignUp />} />
        <Route path="/viewuser/:id" element={<ViewUser />} /> {/* ViewUser 경로 추가 */}
        <Route path="/login" element={<Login />} /> {/* 로그인 경로 추가 */}
        <Route path="/story/list" element={<StoryList />} /> {/* StoryList 컴포넌트를 위한 경로 추가 */}
        <Route path="/story/view/:id" element={<StoryView />} />
        <Route path="/faqs/list" element={<FaqList />} />

      </Routes>
    </Router>
  );
}

export default App;
