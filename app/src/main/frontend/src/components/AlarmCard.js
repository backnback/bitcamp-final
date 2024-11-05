// AlarmCard.js
import { useState } from "react";
import { Link } from "react-router-dom";
import styles from "../assets/styles/css/AlarmCard.module.css";

const AlarmCard = ({ userImg, userName, content }) => {
    const [liked, setLiked] = useState(false);

    const toggleLike = () => {
        setLiked(!liked);
    };

    return (
        <div className={styles.card}>
            <div className={styles.img}>
                <img 
                    src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/user/${userImg}`}
                    alt={`${userName}`}
                    style={{ width: '100%', height: '100%', borderRadius: '10px' }}
                />
            </div>
            <div className={styles.textBox}>
                <div className={styles.textContent}>
                    <h1 className={styles.h1}>{userName}</h1>
                </div>
                <p className={styles.p}>{content}</p>
            </div>
            <button type="button" className="button button__icon" onClick={toggleLike}>
                <span className="blind">좋아요</span>
                <i className={`icon ${liked ? `icon__heart__full` : `icon__heart`}`}></i>
            </button>
        </div>
    );
};

export default AlarmCard;
