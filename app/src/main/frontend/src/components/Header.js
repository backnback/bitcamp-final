import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import styles from '../assets/styles/css/Header.module.css';
import iconStyles from '../assets/styles/css/Icon.module.css';

function Header() {
    return (
        <header className={styles.header}>
            <div className={styles.header__wrapper}>
                <div className={styles.header__top}>
                    <Link to="/story/map" className={`${styles.logo__link} ${styles.header__logo}`}>
                        <img src='/images/logo.svg' alt='기억하길 로고' className={`${styles.logo__img} ${styles.header__logo__img}`} />
                    </Link>
                </div>

                <div className={styles.header__middle}>
                    <nav className={styles.nav__gnb}>
                        <ul className={styles.nav__gnb__list}>
                            <li className={styles.nav__gnb__item}>
                                <Link to="/story/map" className={styles.nav__gnb__link}>지도</Link>
                            </li>
                            <li className={styles.nav__gnb__item}>
                                <Link to="/my-story/list" className={styles.nav__gnb__link}>내 스토리</Link>
                            </li>
                            <li className={styles.nav__gnb__item}>
                                <Link to="/share-story/list" className={styles.nav__gnb__link}>공개 스토리</Link>
                            </li>
                            <li className={styles.nav__gnb__item}>
                                <Link to="/viewuser/" className={styles.nav__gnb__link}>마이페이지</Link>
                            </li>
                        </ul>
                    </nav>
                </div>

                <div className={styles.header__bottom}>
                    <nav className={styles.nav__aside}>
                        <div className={styles.nav__profile}>
                            <Link to="/viewuser/" className={styles.nav__profile__link}>
                                <i className={`${iconStyles.icon} ${iconStyles.profile} ${styles.nav__profile__img}`}></i>
                                <strong className={styles.nav__profile__name}>제펫토</strong>
                            </Link>
                        </div>
                        <ul className={styles.nav__aside__list}>
                            <li className={styles.nav__aside__item}>
                                <Link to="/faq/list" className={styles.nav__aside__link}>고객문의</Link>
                            </li>
                            <li className={styles.nav__aside__item}>
                                <Link to="/login" className={styles.nav__aside__link}>로그아웃</Link>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </header>

    );
}

export default Header;
