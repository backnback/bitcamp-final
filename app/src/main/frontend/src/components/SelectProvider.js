import { createContext, useContext } from 'react';

const Select = createContext();

export const useSelectContext = () => useContext(Select);

export const SelectProvider = ({ children, type, className }) => {
    return (
        <div className={`form__item form__item__select ${className ? className : ``}`}>
            <Select.Provider value={type}>
                {children}
            </Select.Provider>
        </div>
    );
}