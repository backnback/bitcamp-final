import { createContext, useContext } from 'react';
import styles from "../assets/styles/css/Commons.module.css";

const Title = createContext();

export const TitleProvider = ({ children, type }) => {
    return (
        <Title.Provider value={type}>
            {children}
        </Title.Provider>
    )
}

export const useButtonContext = () => useContext(Title);