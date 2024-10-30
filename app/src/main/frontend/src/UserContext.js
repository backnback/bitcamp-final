import React, { createContext, useContext, useState } from 'react';
import Header from './components/Header';

// UserContext 생성
const UserContext = createContext();

// UserProvider 컴포넌트
export const UserProvider = ({ children }) => {
    const [user, setUser] = useState(null); // 사용자 상태 관리

    // 로그아웃 함수
    const logout = () => {
        setUser(null);
    };

    return (
        <UserContext.Provider value={{ user, setUser, logout }}>
            {children}
        </UserContext.Provider>
    );
};

// UserContext 사용을 위한 커스텀 훅
export const useUser = () => {
    return useContext(UserContext);
};
