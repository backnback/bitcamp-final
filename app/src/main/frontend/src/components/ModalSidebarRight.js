import ReactModal from 'react-modal';
import StoryView from '../routes/StoryView.js';

const ModalSidebarRight = ({ onSubmit, onClose, storyId, modalContent }) => {
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
            shouldCloseOnOverlayClick={shouldCloseOnOverlayClick}
            className="modal modal-right"
            overlayClassName="modal-overlay">
            <div>
                {modalContent}
            </div>
            <div>
                <button onClick={handleClickSubmit}>확인</button>
                <button type='button' onClick={handleClickCancel}>취소</button>
            </div>
        </ReactModal>
    );
};

export default ModalSidebarRight;
