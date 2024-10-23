import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import SignUp from "./SignUp";
import ViewUser from "./ViewUser"; // ViewUser 컴포넌트 import
import Login from "./Login"; // Login 컴포넌트 import
import Header from "./header";


function App() {
  const [users, setUsers] = useState([]);
  const [storys, setStorys] = useState([]);

  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8080/user/list');
      setUsers(response.data);
    } catch (error) {
      console.error("There was an error fetching the user!", error);
    }
  };

  const fetchStorys = async () => {
    try {
      const response = await axios.get('http://localhost:8080/story/list'); // /story/list API 호출
      setStorys(response.data);
    } catch (error) {
      console.error("There was an error fetching the storys!", error);
    }
  };

  useEffect(() => {
    fetchUsers();
    fetchStorys();
  }, []);

  return (
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

            <h2>Story List</h2>
            <ul>
               {Array.isArray(storys) && storys.map((story) => (
                 <li key={story.storyId}>{story.title}</li>
               ))}
            </ul>

            <Link to="/signup">회원가입</Link>
            <Link to="/login">로그인</Link>
          </div>
        } />

        <Route path="/signup" element={<SignUp />} />
        <Route path="/viewuser/:id" element={<ViewUser />} /> {/* ViewUser 경로 추가 */}
        <Route path="/login" element={<Login />} /> {/* 로그인 경로 추가 */}

      </Routes>
    </Router>
  );
}

export default App;
