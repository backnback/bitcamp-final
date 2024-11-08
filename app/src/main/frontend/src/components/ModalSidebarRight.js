import ReactModal from 'react-modal';
import React, { useState, useEffect } from 'react';


const ModalSidebarRight = ({ onSubmit, onClose, content }) => {
    const [isOpen, setIsOpen] = useState(false);

    useEffect(() => {
        setIsOpen(true);
    }, []);

    const handleClickSubmit = () => {
        onSubmit();
        setIsOpen(false);
    };

    const handleClickCancel = () => {
        onClose();
        setIsOpen(false);
    };

    return (
        <ReactModal
            isOpen={isOpen}
            onRequestClose={onClose}
            className="modal modal-right"
            overlayClassName="modal-overlay">
            <div>
               {content}
            </div>
            <div>
                 {/*
                <button onClick={handleClickSubmit}>확인</button>
                <button onClick={handleClickCancel}>취소</button>
                */}
            </div>
        </ReactModal>
    );
};

export default ModalSidebarRight;
