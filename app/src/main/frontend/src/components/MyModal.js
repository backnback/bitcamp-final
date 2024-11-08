import ReactModal from 'react-modal';

const MyModal = ( { onSubmit, onClose } ) => {
    const handleClickSubmit = () => {
        console.log("submit")
    };

    const handleClickCancel = () => {
        onClose();
    };

    return (
        <ReactModal
            isOpen>
            <div>모달 입니다.</div>
            <div>
                <button onClick={handleClickSubmit}>확인</button>
                <button type='button' onClick={handleClickCancel}>취소</button>
            </div>
        </ReactModal>
    );
};

export default MyModal;
