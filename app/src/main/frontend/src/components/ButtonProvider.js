import { createContext, useContext } from 'react';

const Button = createContext();

export const useButtonContext = () => useContext(Button);

export const ButtonProvider = ({ children, type, width, className }) => {
    return (
        <div className={`button__item ${width != null ? `button__item__${width}` : ``} ${className != null ? className : ``}`}>
            <Button.Provider value={type}>
                {children}
            </Button.Provider>
        </div>
    );
}