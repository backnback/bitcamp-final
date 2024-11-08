import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useNavigate } from 'react-router-dom';
import styles from '../assets/styles/css/Header.module.css';
import {jwtDecode} from 'jwt-decode';
import { useEffect, useState } from "react";

function Header() {
    const [accessToken, setAccessToken] = useState(null);
    const [user, setUser] = useState(null);
    const [userPath, setUserPath] = useState(null);  // path 저장할 변수
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        navigate('/');
        window.location.reload();
    };

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setAccessToken(token);
            const decodedUser = jwtDecode(token);
            setUser(decodedUser);
            setUserPath(decodedUser.path); // path 값을 미리 저장
        } else {
            console.log("토큰이 없습니다");
        }
    }, []);

    return (
        <header className={styles.header}>
            <div className={styles.header__wrapper}>
                <div className={styles.header__top}>
                    <Link to="/" className={`${styles.logo__link} ${styles.header__logo}`}>
                        <img src='/images/logo.svg' alt='기억하길 로고' className={`${styles.logo__img} ${styles.header__logo__img}`} />
                    </Link>
                </div>

                <div className={styles.header__middle}>
                    <nav className={styles.nav__gnb}>
                        <ul className={styles.nav__gnb__list}>
                            <li className={styles.nav__gnb__item}>
                                <Link to="/" className={styles.nav__gnb__link}>지도</Link>
                            </li>
                            <li className={styles.nav__gnb__item}>
                                <Link to="/my-story/list" className={styles.nav__gnb__link}>내 스토리</Link>
                            </li>
                            <li className={styles.nav__gnb__item}>
                                <Link to="/share-story/list" className={styles.nav__gnb__link}>공유 스토리</Link>
                            </li>
                            <li className={styles.nav__gnb__item}>
                                <Link to="/my-page" className={styles.nav__gnb__link}>마이페이지</Link>
                            </li>
                        </ul>
                    </nav>
                </div>

                <div className={styles.header__bottom}>
                    <nav className={styles.nav__aside}>
                        <div className={styles.nav__profile}>
                            <Link to="/my-page" className={styles.nav__profile__link}>
                                <span className={styles.nav__profile__img__wrap}>
                                    <img className={styles.nav__profile__img}
                                        src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/user/${userPath}`}
                                    />
                                </span>
                                <strong className={`${styles.nav__profile__name} line1`}>{user ? user.nickname : "Guest"}</strong>
                            </Link>
                        </div>
                        <ul className={styles.nav__aside__list}>
                            <li className={styles.nav__aside__item}>
                                <Link to="/faqs" className={styles.nav__aside__link}>고객문의</Link>
                            </li>
                            <li className={styles.nav__aside__item}>
                                <Link to="/" onClick={handleLogout} className={styles.nav__aside__link}>로그아웃</Link>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </header>
    );
}

export default Header;

