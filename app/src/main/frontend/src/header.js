import React from "react";
import { Link } from "react-router-dom";

function Header() {
  return (
    <header>
      <nav>
        <ul>
          <li><Link to="/">홈</Link></li>
          <li><Link to="">스토리</Link></li>
        </ul>
      </nav>
    </header>
  );
}

export default Header;
