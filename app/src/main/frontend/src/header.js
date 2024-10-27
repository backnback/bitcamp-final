import React from "react";
import { Link } from "react-router-dom";
import './header.css'; // 스타일 파일 임포트
import { useUser } from './UserContext'; // UserContext import

const Header = () => {
  const { user } = useUser(); // 사용자 정보 가져오기

  return (
    <header>
      <div className="logo">기억하길</div>
      <nav className="nav">
        <ul>
          <li><Link to="/">홈</Link></li>
          <li><Link to="/story/list">스토리</Link></li>
          <li className="faq"><Link to="/faqs/list">고객 문의</Link></li>
        </ul>
      </nav>
      <h1>My Application</h1>
      {user ? (
        <div>
          <p>안녕하세요, {user.nickname}님!</p>
        </div>
      ) : (
        <div>
          <p>로그인 해주세요.</p>
        </div>
      )}
    </header>
  );
};

export default Header;
