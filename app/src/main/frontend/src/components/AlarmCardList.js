import { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import styles from "../assets/styles/css/AlarmCardList.module.css";
import AlarmCard from '../components/AlarmCard';

function AlarmCardList({alarmListDTOs, confirmView}) {

    const handleConfirmClick = (storyId, userId) => {
        confirmView(storyId, userId);
    };

    return (
        <div className={styles.list}>
            <ul className={styles.list__ul}>
                {/* 알람 목록 */}
                {Array.isArray(alarmListDTOs) && alarmListDTOs.map((alarmListDTO) => (
                       <li key={`${alarmListDTO.userId}-${alarmListDTO.storyId}`} className={styles.list__item}>
                           <AlarmCard
                                userImg={alarmListDTO.userPath || 'default.png'} // 프로필 이미지
                                userName={alarmListDTO.userNickname} // 프로필 이름
                                content={"회원님이 스토리를 좋아합니다"} // 스토리 내용
                                toggleBin={() => handleConfirmClick(alarmListDTO.storyId, alarmListDTO.userId)}
                           />
                       </li>
                   ))}
            </ul>
        </div>
    );
}

export default AlarmCardList
