import { createContext, useContext, useEffect, useState } from 'react';

const Input = createContext();

export const useFormContext = () => useContext(Input);

export const InputProvider = ({ children, type }) => {
    return (
        <div className={`form__item`}>
            <div className={`form__input__wrap`}>
                <Input.Provider value={type}>
                    {children}
                </Input.Provider>
            </div>
        </div>
    );
}