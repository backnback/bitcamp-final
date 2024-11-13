import { createContext, useContext, useEffect, useState } from 'react';

const StoryTitle = createContext();

export const useStoryTitleContext = () => useContext(StoryTitle);

export const StoryTitleProvider = ({ title, selectChildren }) => {
    return (
        <div className='title__story__wrap'>
            <h2 className={`title__story`}>{title}</h2>

            {selectChildren && selectChildren}
        </div>
    );
}