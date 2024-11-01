import React, { createContext, useContext, useState, useEffect } from 'react';
import Header from './components/Header';
import { jwtDecode } from 'jwt-decode';


const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  // 페이지 로드 시 로컬 스토리지에서 토큰을 확인하고 유저 정보를 설정
  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      const userInfo = jwtDecode(token);
      console.log(userInfo);
      setUser(userInfo);
    }
  }, []);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => useContext(UserContext);
