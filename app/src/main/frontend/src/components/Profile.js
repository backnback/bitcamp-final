import React from 'react';
import styles from "../assets/styles/css/Profile.module.css";
import EditBtn from "../components/EditBtn";

const Profile = ({ loginUser }) => {
  return (
    <div className={styles.card}>
      <div className={styles.img}>
        <img
          src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/user/${loginUser.path}`}
          style={{ width: '100%', height: '100%', borderRadius: '50%' }}
        />
      </div>
      <div className={styles.text}>
        <span className={styles.heading}>닉네임 : {loginUser.nickname}</span>
        <p className={styles.info}>이메일 : {loginUser.email}</p>
      </div>
          <EditBtn />
    </div>
  );
};

export default Profile;

