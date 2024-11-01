import { createContext, useContext } from 'react';
import styles from '../assets/styles/css/Button.module.css';

const Button = createContext();

export const useButtonContext = () => useContext(Button);

export const ButtonProvider = ({ children, type, width }) => {
    return (
        <div className={`${styles.item} ${width == '130' ? styles.item__130 : ''}`}>
            <Button.Provider value={type}>
                {children}
            </Button.Provider>
        </div>
    );
}