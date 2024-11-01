import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Header from "./components/Header";
import SignUp from "./routes/SignUp";
import ViewUser from "./routes/ViewUser"; // ViewUser 컴포넌트 import
import Login from "./routes/Login"; // Login 컴포넌트 import
import StoryMap from "./routes/StoryMap";
import StoryList from "./routes/StoryList"; // StoryList 컴포넌트 import
import ShareStoryList from "./routes/ShareStoryList"; // ShareStoryList 컴포넌트 import
import StoryView from "./routes/StoryView";
import Test from "./routes/Test";
import ShareStoryView from "./routes/ShareStoryView";
import FaqBoard from "./routes/FaqBoard";
import StoryAddForm from "./routes/StoryAddForm";
import StoryUpdateForm from "./routes/StoryUpdateForm";
import { UserProvider, useUser } from './UserContext'; // UserContext import
import layoutStyles from "./assets/styles/css/Layout.module.css";

import MapSeoul from "./components/map/MapSeoul";
import MapBusan from "./components/map/MapBusan";
import MapDaegu from "./components/map/MapDaegu";
import MapDaejeon from "./components/map/MapDaejeon";
import MapGwangju from "./components/map/MapGwangju";
import MapGwangwon from "./components/map/MapGwangwon";
import MapGyeonggi from "./components/map/MapGyeonggi";
import MapIncheon from "./components/map/MapIncheon";
import MapJeju from "./components/map/MapJeju";
import MapNorthChungcheoung from "./components/map/MapNorthChungcheoung";
import MapNorthGyeongsang from "./components/map/MapNorthGyeongsang";
import MapNorthJeolla from "./components/map/MapNorthJeolla";
import MapSejong from "./components/map/MapSejong";
import MapSouthChungcheong from "./components/map/MapSouthChungcheong";
import MapSouthGyeongsan from "./components/map/MapSouthGyeongsan";
import MapSouthJeolla from "./components/map/MapSouthJeolla";
import MapUlsan from "./components/map/MapUlsan";

function App() {
  // UserProvider 내부에서 useUser 훅을 호출하여 사용자 정보 가져오기
  const { user, logout, setUser } = useUser();
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
    <div className={`${layoutStyles.wrapper} ${layoutStyles.wrapper__header}`}>
      {user == null ? <Login /> : <Header />}


      <div className={`${layoutStyles.content__wrapper}`}>
        <div className={`${layoutStyles.contents}`}>
          <Routes>
            <Route path="/story/map" element={<StoryMap />} />

            <Route path="/form/test" element={<FormStyles />} />
            {/* 라우터 경로 설정 */}
            <Route path="/story/map/seoul" element={<MapSeoul />} />
            <Route path="/story/map/busan" element={<MapBusan />} />
            <Route path="/story/map/daegu" element={<MapDaegu />} />
            <Route path="/story/map/daejeon" element={<MapDaejeon />} />
            <Route path="/story/map/gwangju" element={<MapGwangju />} />
            <Route path="/story/map/gwangwon" element={<MapGwangwon />} />
            <Route path="/story/map/gyeonggi" element={<MapGyeonggi />} />
            <Route path="/story/map/incheon" element={<MapIncheon />} />
            <Route path="/story/map/jeju" element={<MapJeju />} />
            <Route path="/story/map/northChungcheoung" element={<MapNorthChungcheoung />} />
            <Route path="/story/map/northGyeongsang" element={<MapNorthGyeongsang />} />
            <Route path="/story/map/northJeolla" element={<MapNorthJeolla />} />
            <Route path="/story/map/sejong" element={<MapSejong />} />
            <Route path="/story/map/southChungcheong" element={<MapSouthChungcheong />} />
            <Route path="/story/map/southGyeongsan" element={<MapSouthGyeongsan />} />
            <Route path="/story/map/southJeolla" element={<MapSouthJeolla />} />
            <Route path="/story/map/ulsan" element={<MapUlsan />} />

          <Route path="/signup" element={<SignUp />} /> {/* 회원가입 페이지 */}
          <Route path="/viewuser/:id" element={<ViewUser />} /> {/* 특정 사용자 보기 */}
          <Route path="/login" element={<Login />} /> {/* 로그인 페이지 */}
          <Route path="/share-story/list" element={<ShareStoryList />} /> {/* 스토리 목록 페이지 */}
          <Route path="/my-story/list" element={<StoryList />} /> {/* 스토리 목록 페이지 */}
          <Route path="/test/list" element={<Test />} /> {/* 테스트 */}
          <Route path="/share-story/view/:id" element={<ShareStoryView />} /> {/* 특정 스토리 보기 */}
          <Route path="/my-story/view/:id" element={<StoryView />} /> {/* 특정 스토리 보기 */}
          <Route path="/my-story/form/add" element={<StoryAddForm />} /> {/* 스토리 추가 */}
          <Route path="/story/form/update/:id" element={<StoryUpdateForm />} /> {/* 스토리 수정 */}
          <Route path="/faqs" element={<FaqBoard />} /> {/* FAQ 목록 페이지 */}
        </Routes>
        </div>
      </div>
    </div>
  );
}

// UserProvider로 App 컴포넌트 감싸기
function UserWrapper() {
  return (
    <UserProvider>
      <Router>
        <App />
      </Router>
    </UserProvider>
  );
}

export default UserWrapper;
