import React from "react";
import { Link } from "react-router-dom";
import './header.css'; // 스타일 파일 임포트

function Header() {
  return (
    <header className="sidebar">
      <div className="logo">MyApp</div>
      <nav className="nav">
        <ul>
          <li><Link to="/">홈</Link></li>
          <li><Link to="/story/list">스토리</Link></li>
        </ul>
      </nav>
    </header>
  );
}

export default Header;
