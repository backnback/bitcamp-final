import loadable from '@loadable/component';
import { useContext } from "react"
import { ModalsStateContext, ModalsDispatchContext } from "./ModalContext"

export const modals = {
    myModal: loadable(() => import('./MyModal')),
    modalSidebarRight: loadable(() => import('./ModalSidebarRight')),
    storyEditModal: loadable(() => import('./StoryEditModal')),
};

function Modals() {
    const openedModals = useContext(ModalsStateContext);
    const { close } = useContext(ModalsDispatchContext);

    // 모달에 받아오는 props 정보 찍어보기
    // console.log("Opened Modals:", openedModals);

    return openedModals.map((modal, index) => {
        const { Component, props } = modal;
        const { onSubmit, ...restProps } = props;
        const onClose = () => {
            close(Component);
        };

        const handleSubmit = async () => {
            if (typeof onSubmit === 'function') {
                await onSubmit();
            }
            onClose();
        };


        // return <Component {...restProps} isOpen={true} key={index} onRequestClose={onClose} />;
        return <Component
            {...restProps}
            {...props}
            key={index}
            onClose={onClose}
            onSubmit={handleSubmit}
            isOpen={restProps.isOpen}
            shouldCloseOnOverlayClick={restProps.shouldCloseOnOverlayClick} />
    });
}

export default Modals;