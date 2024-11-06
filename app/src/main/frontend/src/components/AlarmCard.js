import { useState } from "react";
import { Link } from "react-router-dom";
import styles from "../assets/styles/css/AlarmCard.module.css";
import Bin from "../components/Bin";

const AlarmCard = ({ userImg, userName, content }) => {
    const [liked, setLiked] = useState(false);

    const toggleBin = () => {
    };

    return (
        <div className={styles.card}>
            <div className={styles.img}>
                <img 
                    src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/user/${userImg}`}
                    style={{ width: '100%', height: '100%', borderRadius: '10px' }}
                />
            </div>
            <div className={styles.textBox}>
                <div className={styles.textContent}>
                    <h1 className={styles.h1}>{userName}</h1>
                </div>
                <p className={styles.p}>{content}</p>
            </div>
            <Bin onClick={toggleBin} />
        </div>
    );
};

export default AlarmCard;
