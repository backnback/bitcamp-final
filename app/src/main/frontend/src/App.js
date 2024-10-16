import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import SignUp from "./SignUp";
import ViewUser from "./ViewUser"; // ViewUser 컴포넌트 import

function App() {
  const [users, setUsers] = useState([]);

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
    <Router>
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
            <Link to="/signup">회원가입</Link>
          </div>
        } />

        <Route path="/signup" element={<SignUp />} />
        <Route path="/viewuser/:id" element={<ViewUser />} /> {/* ViewUser 경로 추가 */}
      </Routes>
    </Router>
  );
}

export default App;
