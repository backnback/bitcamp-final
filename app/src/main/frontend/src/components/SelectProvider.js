import { createContext, useContext } from 'react';

const Select = createContext();

export const useSelectContext = () => useContext(Select);

export const SelectProvider = ({ children, type }) => {
    return (
        <div className={`form__item`}>
            <Select.Provider value={type}>
                {children}
            </Select.Provider>
        </div>
    );
}