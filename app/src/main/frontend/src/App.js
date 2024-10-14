import {useEffect, useState} from "react";
import axios from "axios";

function App() {
  const [users, setUsers] = useState([]);

  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/users');
      console.log(response.data); // 데이터 확인
      setUsers(response.data); // JSON 응답을 상태에 설정
    } catch (error) {
      console.error("There was an error fetching the users!", error);
    }
  };

  useEffect(() => {
    fetchUsers(); // users를 가져오기
  }, []);

  return (
      <div>
        <h1>User List</h1>
        <ul>
          {Array.isArray(users) && users.map(user => (
              <li key={user.id}>{user.name} - {user.email}</li>
          ))}
        </ul>
      </div>
  );
}

export default App;
