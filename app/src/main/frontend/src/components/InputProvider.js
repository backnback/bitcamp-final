import { createContext, useContext, useEffect, useState } from 'react';

const Input = createContext();

export const useFormContext = () => useContext(Input);

export const InputProvider = ({ children, type, className }) => {
    return (
        <div className={`form__item form__item__input ${className ? className : ``}`}>
            <div className={`form__input__wrap`}>
                <Input.Provider value={type}>
                    {children}
                </Input.Provider>
            </div>
        </div>
    );
}