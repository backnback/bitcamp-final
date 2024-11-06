import React from 'react';
import styles from "../assets/styles/css/Profile.module.css";

const Profile = () => {
  return (
    <div className={styles.card}>
      <svg xmlSpace="preserve" style={{ enableBackground: 'new 0 0 16 16' }} viewBox="0 0 16 16" y="0px" x="0px" id="Layer_1_1_" version="1.1" className={styles.img}>
        <path d="M12,9H8H4c-2.209,0-4,1.791-4,4v3h16v-3C16,10.791,14.209,9,12,9z" />
        <path d="M12,5V4c0-2.209-1.791-4-4-4S4,1.791,4,4v1c0,2.209,1.791,4,4,4S12,7.209,12,5z" />
      </svg>
      <div className={styles.text}>
        <span className={styles.heading}>Marco</span>
        <p className={styles.info}>CEO</p>
        <div className={styles.country}>
          <svg strokeLinejoin="round" strokeLinecap="round" strokeWidth={2} stroke="#f2e5d7" fill="none" viewBox="0 0 24 24" className={styles.svgCountry}>
            <path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z" />
            <line y2={15} x2={4} y1={22} x1={4} />
          </svg>
          <span>Italy</span>
        </div>
      </div>
    </div>
  );
};

export default Profile;
