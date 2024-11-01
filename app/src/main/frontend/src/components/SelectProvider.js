import { createContext, useContext } from 'react';
import styles from '../assets/styles/css/Form.module.css';

const Select = createContext();

export const useSelectContext = () => useContext(Select);

export const SelectProvider = ({ children, type }) => {
    return (
        <div className={styles.item}>
            <Select.Provider value={type}>
                {children}
            </Select.Provider>
        </div>
    );
}