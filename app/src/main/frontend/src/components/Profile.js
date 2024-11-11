import React from 'react';
import styles from "../assets/styles/css/Profile.module.css";
import useModals from '../useModals';
import { modals } from '../components/Modals';
import { ButtonProvider } from '../components/ButtonProvider';

const Profile = ({ loginUser }) => {

  const { openModal } = useModals();

  const handleClick = () => {
      openModal(modals.reauthenticateModal, {
          onSubmit: () => {
              console.log('비지니스 로직 처리...2');
          },
      });
  };

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
      <ButtonProvider width={'icon'}>
        <button type="button" onClick={handleClick} className={`button button__icon`}>
            <i data-button="icon" className={`icon icon__edit__white`}></i>
        </button>
      </ButtonProvider>
    </div>
  );
};

export default Profile;