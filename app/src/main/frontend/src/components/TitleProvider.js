import { createContext, useContext } from 'react';
import styles from "../assets/styles/css/Commons.module.css";

const Title = createContext();
const Text = createContext();

export const TitleProvider = ({ children, type }) => {
    return (
        <Title.Provider value={type}>
            {children}
        </Title.Provider>
    )
}

export const TextProvider = ({ children, type }) => {
    return (
        <Text.Provider value={type}>
            {children}
        </Text.Provider>
    )
}

export const useTitleContext = () => useContext(Title);
export const useTextContext = () => useContext(Text);