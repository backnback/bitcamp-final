import { useMemo, useState } from 'react';
import { ModalsDispatchContext, ModalsStateContext } from './ModalContext';
import Modals from './Modals';

const ModalsProvider = ({ children }) => {
    const [openedModals, setOpenedModals] = useState([]);

    const open = (Component, props) => {
        setOpenedModals((modals) => {
            return [...modals, { Component, props }];
        });
    };

    const close = (Component) => {
        setOpenedModals((modals) => {
            return modals.filter((modal) => {
                return modal.Component !== Component;
            });
        });
    };

    const dispatch = useMemo(() => ({ open, close }), []);
    return (
        <ModalsStateContext.Provider value={openedModals}>
            <ModalsDispatchContext.Provider value={dispatch}>
                {children}
                <Modals />
            </ModalsDispatchContext.Provider>
        </ModalsStateContext.Provider>
    );
};

export default ModalsProvider;