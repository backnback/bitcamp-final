import { createContext, useContext, useEffect, useState } from 'react';
import styles from '../assets/styles/css/Form.module.css';

const Input = createContext();

export const useFormContext = () => useContext(Input);

export const InputProvider = ({ children, type }) => {
    return (
        <div className={styles.item}>
            <div className={styles.input__wrap}>
                <Input.Provider value={type}>
                    {children}
                </Input.Provider>
            </div>
        </div>
    );
}