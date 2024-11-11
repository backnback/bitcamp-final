import loadable from '@loadable/component';
import { useContext } from "react"
import { ModalsStateContext, ModalsDispatchContext } from "./ModalContext"

export const modals = {
    myModal: loadable(() => import('./MyModal')),
    modalSidebarRight: loadable(() => import('./ModalSidebarRight')),
    storyEditModal: loadable(() => import('./StoryEditModal')),
    reauthenticateModal: loadable(() => import('./ReauthenticateModal')),
};

function Modals() {
    const openedModals = useContext(ModalsStateContext);
    const { close } = useContext(ModalsDispatchContext);

    // 모달에 받아오는 props 정보 찍어보기
    // console.log("Opened Modals:", openedModals);

    return openedModals.map((modal, index) => {
        const { Component, props } = modal;
        const { onSubmit, onClose, ...restProps } = props;

        const handleSubmit = async () => {
            if (typeof onSubmit === 'function') {
                await onSubmit();
            }
            console.log("modals에서 지운다!");
            close(Component);
        };

        const handleClose = async () => {
          if (typeof onClose === 'function') {
            await onClose();
          }
          console.log("modals에서 지운다!");
          close(Component);
        };

        // return <Component {...restProps} isOpen={true} key={index} onRequestClose={onClose} />;
        return <Component
            {...restProps}
            {...props}
            key={index}
            onSubmit={handleSubmit}
            onClose={handleClose}
            isOpen={restProps.isOpen}
            shouldCloseOnOverlayClick={restProps.shouldCloseOnOverlayClick} />
    });
}

export default Modals;
