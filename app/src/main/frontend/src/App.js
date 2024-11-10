import { BrowserRouter as Router, Route, Routes, Link, useLocation } from "react-router-dom";
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
import MyPage from "./routes/MyPage";
import ShareStoryView from "./routes/ShareStoryView";
import FaqBoard from "./routes/FaqBoard";
import StoryAddForm from "./routes/StoryAddForm";
import StoryUpdateForm from "./routes/StoryUpdateForm";
import FormStyles from "./routes/FormStyles";
import FindEmail from "./routes/FindEmail";
import FindPassword from "./routes/FindPassword";
import NewPassword from "./routes/NewPassword";

import MapSeoul from "./components/map/MapSeoul";
import MapBusan from "./components/map/MapBusan";
import MapDaegu from "./components/map/MapDaegu";
import MapDaejeon from "./components/map/MapDaejeon";
import MapGwangju from "./components/map/MapGwangju";
import MapGwangwon from "./components/map/MapGwangwon";
import MapGyeonggi from "./components/map/MapGyeonggi";
import MapIncheon from "./components/map/MapIncheon";
import MapJeju from "./components/map/MapJeju";
import MapNorthChungcheoung from "./components/map/MapNorthChungcheoungTest";
import MapNorthGyeongsang from "./components/map/MapNorthGyeongsang";
import MapNorthJeolla from "./components/map/MapNorthJeolla";
import MapSejong from "./components/map/MapSejong";
import MapSouthChungcheong from "./components/map/MapSouthChungcheong";
import MapSouthGyeongsan from "./components/map/MapSouthGyeongsan";
import MapSouthJeolla from "./components/map/MapSouthJeolla";
import MapUlsan from "./components/map/MapUlsan";
import Map from "./components/Map";
import { jwtDecode } from "jwt-decode";
import MapLocation from "./routes/MapLocation";

function App() {
  // UserProvider 내부에서 useUser 훅을 호출하여 사용자 정보 가져오기
  const [users, setUsers] = useState([]); // 사용자 목록 상태
  const [accessToken, setAccessToken] = useState(null);
  const [user, setUser] = useState(null);
  const currentLocation = useLocation();
  const [currentTime, setCurrentTime] = useState(Date.now());


  useEffect(() => {
    let token = null;
    const checkTokenExpiration = () => {
      token = localStorage.getItem('accessToken');
      if (token) {
        const decodedToken = jwtDecode(token);
        const expirationTime = decodedToken.exp * 1000; // 초 단위의 만료 시간을 밀리초로 변환

        if (Date.now() >= expirationTime) {
          alert("로그인이 만료되었습니다. 다시 로그인해주세요.");
          localStorage.removeItem('accessToken');
          setAccessToken(null);
          setUser(null);
          window.location.reload();
        } else {
          setAccessToken(token);
          setUser(decodedToken);
        }
      }
    };

    checkTokenExpiration();

    if (token != null) {
      // 1초마다 currentTime 업데이트
      const interval = setInterval(() => setCurrentTime(Date.now()), 1000);
      // console.log(interval);
      return () => clearInterval(interval);
    }
  }, [currentTime]);


  useEffect(() => {
    // console.log('page changed to:', currentLocation.pathname)

    // body class
    const locationNames = currentLocation.pathname.split('/');
    const [firstName, secondName] = [locationNames[1] && `body__${locationNames[1]}`, locationNames[2] != null & locationNames[2] != '' && `body__${locationNames[1]}__${locationNames[2]}`];
    document.body.className = `body ${firstName} ${secondName || ''}`;

    //fetchUsers(); // 컴포넌트가 처음 로드될 때 사용자 목록을 가져옴
  }, [currentLocation]);

  return (
    <div className={`layout__wrapper layout__wrapper__header`}>
      {user == null ? null : <Header />}

      <div className={`layout__content__wrapper`}>
        <div className={`layout__contents`}>
          <Routes>
            <Route path={user == null ? "/" : "/map"} element={user == null ? <Login /> : <StoryMap />} />


            <Route path="/form/test" element={<FormStyles />} />
            {/* 라우터 경로 설정 */}
            <Route path="map/story/:locationId" element={<MapLocation />} />
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
            <Route path="/my-page" element={<MyPage />} /> {/* 마이 페이지 */}
            <Route path="/share-story/view/:storyId" element={<ShareStoryView />} /> {/* 특정 스토리 보기 */}
            <Route path="/my-story/view/:storyId" element={<StoryView />} /> {/* 특정 스토리 보기 */}
            <Route path="/my-story/form/add" element={<StoryAddForm />} /> {/* 스토리 추가 */}
            <Route path="/my-story/form/update/:storyId" element={<StoryUpdateForm />} /> {/* 스토리 수정 */}
            <Route path="/faqs" element={<FaqBoard />} /> {/* FAQ 목록 페이지 */}
            <Route path="/find-email" element={<FindEmail />} /> {/* 이메일 찾기 페이지 */}
            <Route path="/find-password" element={<FindPassword />} /> {/* 비번 찾기 페이지 */}
            <Route path="/newPassword" element={<NewPassword />} /> {/* 비번 재생성 페이지 */}
          </Routes>
        </div>
      </div>
    </div>
  );
}

// UserProvider로 App 컴포넌트 감싸기
function UserWrapper() {
  return (
    <Router>
      <App />
    </Router>
  );
}

export default UserWrapper;
