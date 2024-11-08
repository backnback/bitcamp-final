import ReactModal from 'react-modal';

const ModalSidebarRight = ({ onSubmit, onClose }) => {
    const handleClickSubmit = () => {
        onSubmit();
    };

    const handleClickCancel = () => {
        onClose();
    };

    return (
        <ReactModal
            isOpen
            onRequestClose={onClose}
            className="modal modal-right"
            overlayClassName="modal-overlay">
            <div>모달 입니다.</div>
            <div>
                <button onClick={handleClickSubmit}>확인</button>
                <button type='button' onClick={handleClickCancel}>취소</button>
            </div>
        </ReactModal>
    );
};

export default ModalSidebarRight;