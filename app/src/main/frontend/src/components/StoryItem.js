import { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { ButtonProvider } from "./ButtonProvider";
import styles from "../assets/styles/css/StoryItem.module.css";

import { createContext, useContext } from 'react';

const StoryAdd = createContext();

export const useStoryAddContext = () => useContext(StoryAdd);

export const StoryAddContext = () => {
    return (
        <div className={`${styles.item} ${styles.add}`}>
            <button type="button" className={`button ${styles.add__button}`}>
                <span className={`blind`}>스토리 등록</span>
                <i className={`icon icon__plus__white`}></i>
            </button>
        </div>
    );
}

function StoryItem() {
    const [like, setLike] = useState(false);

    useEffect(() => {

    }, [])
    return (
        <div className={styles.item}>
            <div className={styles.top}>
                <div className={styles.profile}>
                    <Link to="/" className={styles.profile__link}>
                        <span className={styles.profile__img__wrap}>
                            <img src={'/images/profile-default.png'} alt="" className={styles.profile__img} />
                        </span>
                        <span className={`${styles.profile__name} line1`}>제펫토제펫토제펫토제펫토제펫토제펫토제펫토제펫토제펫토제펫토제펫토</span>
                    </Link>
                </div>
                <div className={`${styles.right}`}>
                    <span className="blind">비공개</span>
                    <i className={`icon icon__lock__black`}></i>
                </div>
            </div>
            <div className={styles.middle}>
                <img src={"/images/sample.jpg"} alt={'story image'} className={`${styles.thumnail}`} />
            </div>
            <div className={styles.bottom}>
                <div className={styles.bottom__header}>
                    <ButtonProvider width={'icon'}>
                        <button type="button" className={`button button__icon`}>
                            <span className={`blind`}>좋아요</span>
                            <i className={`icon icon__heart`}></i>
                        </button>
                    </ButtonProvider>
                    <span className={`${styles.like__count}`}>999</span>
                </div>
                <button type="button" className={`${styles.view__button}`}>
                    <div className={`${styles.bottom__body}`}>
                        <strong className={`${styles.title}`}>카드 UI 짜는 중</strong>
                        <p className={`${styles.content}`}>국채를 모집하거나 예산외에 국가의 부담이 될 계약을 체결하려 할 때에는 정부는 미리 국회의 의결을 얻어야 한다. </p>
                    </div>
                    <div className={`${styles.bottom__footer}`}>
                        <span className={`${styles.location}`}>부산광역시 흰여울마을</span>
                        <span className={`${styles.date}`}>2024.09.13</span>
                    </div>
                </button>
            </div>
        </div>
    )
}

export default StoryItem;