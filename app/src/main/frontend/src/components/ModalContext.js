import { createContext, useContext, useEffect, useState } from 'react';


export const ModalsDispatchContext = createContext({
    open: () => { },
    close: () => { },
});

export const ModalsStateContext = createContext([]);