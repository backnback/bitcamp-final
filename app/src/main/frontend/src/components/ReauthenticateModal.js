import ReactModal from 'react-modal';
import axios from 'axios';
import Reauthenticate from './Reauthenticate';
import { ButtonProvider } from './ButtonProvider';
import { useState, useEffect} from 'react';


const ReauthenticateModal = ({ onSubmit, onClose, shouldCloseOnOverlayClick = true }) => {

    const [password, setPassword] = useState('');
    const [accessToken, setAccessToken] = useState(null);


    const handleClickSubmit = async (e) => {
        e.preventDefault();
       
        try {
            const response = await axios.post('http://localhost:8080/user/userauthentication', {
                password: password
            }, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'application/json'
                },
            });

            if (response.data) {
               alert("인증되었습니다");
            } else {
                alert("인증 실패: 비밀번호를 확인해주세요.");
            }
        } catch (error) {
            console.error("마이페이지 회원인증 요청 중 오류 발생:", error);
            alert("마이페이지 회원인증 오류가 발생했습니다. 나중에 다시 시도해주세요.");
        }
        
    };

    const handleClickCancel = () => {
        onClose();
    };

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setAccessToken(token);
        } else {
            console.warn("Access token이 없습니다.");
        }
    }, []);

    return (
        <ReactModal
            isOpen
            onRequestClose={onClose}
            shouldCloseOnOverlayClick={shouldCloseOnOverlayClick}
            className="modal modal-right"
            overlayClassName="modal-overlay">

            <div className='modal__hedader'>

                <ButtonProvider width={'icon'}>
                    <button type="button" className={`button button__icon`} onClick={handleClickCancel}>
                        <i data-button="icon" className={`icon icon__arrow__right__black`}></i>
                        <span className={`blind`}>닫기</span>
                    </button>
                </ButtonProvider>
            </div>

            <div className='modal__body'>
                <Reauthenticate password={password} setPassword={setPassword}/>
            </div>

            <div className='modal__footer'>
                <ButtonProvider>
                    <button type="button" className={`button button__primary`} onClick={handleClickSubmit}>
                        <span className={`button__text`}>확인</span>
                    </button>
                </ButtonProvider>
            </div>

        </ReactModal>
    );
};

export default ReauthenticateModal;