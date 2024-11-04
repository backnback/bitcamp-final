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

function StoryItem({ profileImg, profileName, currentLock, storyThum, currentLike, currentLikeCount, storyTitle, storyContent, storyLocation, storyDate }) {
    const [like, setLike] = useState(currentLike);
    const likeClick = () => {
        setLike((current) => current = !current);
    }

    useEffect(() => {
    }, []);
    return (
        <div className={styles.item}>
            <div className={styles.top}>
                <div className={styles.profile}>
                    <Link to="/" className={styles.profile__link}>
                        <span className={styles.profile__img__wrap}>
                            {/* <img src={'/images/profile-default.png'} alt="" className={styles.profile__img} /> */}
                            <img src={`/images/${profileImg}`} alt={`${profileName}의 프로필 이미지`} className={styles.profile__img} />
                        </span>
                        {/* <span className={`${styles.profile__name} line1`}>제펫토제펫토제펫토제펫토제펫토제펫토제펫토제펫토제펫토제펫토제펫토</span> */}
                        <span className={`${styles.profile__name} line1`}>{profileName}</span>
                    </Link>
                </div>
                <div className={`${styles.right}`}>
                    <span className="blind">{currentLock ? `비공개` : `공개`}</span>
                    <i className={`icon ${currentLock ? `icon__lock__black` : `icon__unlock`}`}></i>
                </div>
            </div>
            <div className={styles.middle}>
                {/* <img src={"/images/sample.jpg"} alt={'story image'} className={`${styles.thumnail}`} /> */}
                <img src={`/images/${storyThum}`} alt={'story image'} className={`${styles.thumnail}`} />
            </div>
            <div className={styles.bottom}>
                <div className={styles.bottom__header}>
                    <ButtonProvider width={'icon'} className={`${styles.button__item}`}>
                        <button type="button" className={`button button__icon`} onClick={likeClick}>
                            <span className={`blind`}>좋아요</span>
                            <i className={`icon ${like ? `icon__heart__full` : `icon__heart`}`}></i>
                        </button>
                    </ButtonProvider>
                    {/* <span className={`${styles.like__count}`}>999</span> */}
                    <span className={`${styles.like__count}`}>{like ? currentLikeCount + 1 : currentLikeCount}</span>
                </div>
                <button type="button" className={`${styles.view__button}`}>
                    <div className={`${styles.bottom__body}`}>
                        {/* <strong className={`${styles.title} line1`}>카드 UI 짜는 중카드 UI 짜는 중카드 UI 짜는 중카드 UI 짜는 중</strong> */}
                        <strong className={`${styles.title} line1`}>{storyTitle}</strong>
                        {/* <p className={`${styles.content}`}>국채를 모집하거나 예산외에 국가의 부담이 될 계약을 체결하려 할 때에는 정부는 미리 국회의 의결을 얻어야 한다.국채를 모집하거나 예산외에 국가의 부담이 될 계약을 체결하려 할 때에는 정부는 미리 국회의 의결을 얻어야 한다.국채를 모집하거나 예산외에 국가의 부담이 될 계약을 체결하려 할 때에는 정부는 미리 국회의 의결을 얻어야 한다.국채를 모집하거나 예산외에 국가의 부담이 될 계약을 체결하려 할 때에는 정부는 미리 국회의 의결을 얻어야 한다. </p> */}
                        <p className={`${styles.content}`}>{storyContent} </p>
                    </div>
                    <div className={`${styles.bottom__footer}`}>
                        {/* <span className={`${styles.location}`}>부산광역시 흰여울마을</span> */}
                        <span className={`${styles.location}`}>{storyLocation}</span>
                        {/* <span className={`${styles.date}`}>2024.09.13</span> */}
                        <span className={`${styles.date}`}>{storyDate}</span>
                    </div>
                </button>
            </div>
        </div>
    )
}

export default StoryItem;