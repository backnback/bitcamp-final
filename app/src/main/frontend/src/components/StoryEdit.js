
import styles from "../assets/styles/css/Sidebar.module.css";
import { createContext, useContext } from 'react';
import { InputProvider } from "./InputProvider";

const StoryAddEdit = createContext();

export const useStoryAddEditContext = () => useContext(StoryAddEdit);

export const StoryAddEditContext = () => {

    return (
        <div>
            <InputProvider>
                <input
                    type='text'
                    className={`form__input`}
                    defaultValue='텍스트를 작성하는건? value'
                    id='text01'
                    name='텍수투'
                    placeholder='title' />
            </InputProvider>
            <InputProvider>
                <input
                    type='text'
                    className={`form__input`}
                    defaultValue='텍스트를 작성하는건? value'
                    id='text02'
                    name='텍수투'
                    placeholder='title' />
            </InputProvider>
        </div>
    );
}

const StoryUpdateEdit = createContext();

export const useStoryUpdateEditContext = () => useContext(StoryUpdateEdit);

export const StoryUpdateEditContext = () => {
    return (
        <div className={`${styles.item} ${styles.add}`}>
            <button type="button" className={`button ${styles.add__button}`} data-event={`modal`}>
                <span className={`blind`}>스토리 업데이트</span>
                <i className={`icon icon__plus__white`}></i>
            </button>
        </div>
    );
}