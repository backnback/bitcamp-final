import { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import styles from "../assets/styles/css/AlarmCardList.module.css";
import AlarmCard from '../components/AlarmCard';

function AlarmCardList({userList}) {
    return (
        <div className={styles.list}>
            <ul className={styles.list__ul}>
                {/* 알람 목록 */}
                {Array.isArray(userList) && userList.map((user) => (
                       <li key={user.id} className={styles.list__item}>
                           <AlarmCard
                                userImg={user.path || 'default.png'} // 프로필 이미지
                                userName={user.nickname} // 프로필 이름
                                content={"회원님이 스토리를 좋아합니다"} // 스토리 내용
                           />
                       </li>
                   ))}
            </ul>
        </div>
    );
}

export default AlarmCardList
