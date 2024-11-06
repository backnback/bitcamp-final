import FormFileIcon from "../components/FormFileIcon";
import { InputProvider } from '../components/InputProvider';
import { ButtonProvider } from '../components/ButtonProvider';
import { SelectProvider } from '../components/SelectProvider';
import StoryItem, { StoryAddContext } from "../components/StoryItem";
import useModals from '../useModals';
import { modals } from '../components/Modals';

function FormStyles() {
    const { openModal } = useModals();

    const handleClick = () => {
        openModal(modals.storyEditModal, {
            onSubmit: () => {
                console.log('비지니스 로직 처리...2');
            },
        });
    };

    const handleClick2 = () => {
        openModal(modals.myModal, {
            onSubmit: () => {
                console.log('비지니스 로직 처리...2');
            },
        });
    };

    return (
        <>

            <InputProvider>
                <input placeholder="입력입력" className={`form__input`} />
            </InputProvider>

            <InputProvider>
                <label htmlFor="checkbox01" className={`form__label form__label__checkbox`}>
                    <input
                        type='checkbox'
                        className={`form__input`}
                        defaultChecked='checked'
                        id='checkbox01'
                        name='체크체크' />
                    <span className={`.input__text`}>체크해라</span>
                </label>
            </InputProvider>

            <InputProvider>
                <label htmlFor="checkbox02" className={`form__label form__label__checkbox`}>
                    <input
                        type='checkbox'
                        className={`form__input`}
                        id='checkbox02'
                        name='체크체크' />
                    <span className={`.input__text`}>체크하지마러라</span>
                </label>
            </InputProvider>

            <InputProvider>
                <label htmlFor="radio01" className={`form__label form__label__radio`}>

                    <input
                        type='radio'
                        className={`form__input`}
                        defaultChecked='checked'
                        id='radio01'
                        name='라디오' />
                    <span className={`.input__text`}>라디오버튼이여</span>

                </label>
            </InputProvider>

            <InputProvider>
                <label htmlFor="radio02" className={`form__label form__label__radio`}>
                    <input
                        type='radio'
                        className={`form__input`}
                        id='radio02'
                        name='라디오' />
                    <span className={`.input__text`}>라디오버튼이여</span>
                </label>
            </InputProvider>

            <InputProvider>
                <input
                    type='text'
                    className={`form__input`}
                    defaultValue='텍스트를 작성하는건? value'
                    id='text01'
                    name='텍수투'
                    placeholder='title' />
            </InputProvider>

            <InputProvider>
                <input
                    type='password'
                    className={`form__input`}
                    id='pwd01'
                    name='패수워드'
                    placeholder='비밀번호' />
            </InputProvider>

            <InputProvider>
                <input
                    type='email'
                    className={`form__input`}
                    id='email01'
                    name='이메일'
                    defaultValue='test@test.com'
                    placeholder='이메일입력' />
            </InputProvider>

            <InputProvider>
                <label htmlFor="file01" className={`form__label form__label__file`}>
                    <input type='file' className={`blind`} id="file01" />
                    <FormFileIcon />
                </label>
            </InputProvider>

            <InputProvider>
                <textarea
                    id='textarea01'
                    placeholder='내용 입력'
                    className={`form__textarea`}
                ></textarea>
            </InputProvider>

            <ButtonProvider>
                <button type="button" className={`button button__primary`}>
                    <span className={`button__text`}>등록</span>
                </button>
            </ButtonProvider>

            <ButtonProvider>
                <button type="button" className={`button button__whiteRed`}>
                    <span className={`button__text`}>삭제</span>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'130'}>
                <button type="button" className={`button button__whitePrimary`}>
                    <span className={`button__text`}>삭제</span>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'130'}>
                <button type="button" className={`button button__whitePrimary`} onClick={handleClick}>
                    <span className={`button__text`}>모달열기</span>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'130'}>
                <button type="button" className={`button button__whitePrimary`} onClick={handleClick2}>
                    <span className={`button__text`}>모달열기2</span>
                </button>
            </ButtonProvider>

            <SelectProvider>
                <select id="select01" name="selectName" className={`form__select`}>
                    <option value={'0'}>선택해라</option>
                    <option value={'1'}>2021</option>
                    <option value={'2'}>2022</option>
                    <option value={'3'}>2023</option>
                </select>
            </SelectProvider>

            <ButtonProvider width={'icon'}>
                <button type="button" className={`button button__icon`}>
                    <i data-button="icon" className={`icon icon__arrow__right__black`}></i>
                    <span className={`blind`}>닫기</span>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'icon'}>
                <button type="button" className={`button button__icon`}>
                    <i data-button="icon" className={`icon icon__edit__black`}></i>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'icon'}>
                <button type="button" className={`button button__icon`}>
                    <i data-button="icon" className={`icon icon__share__black`}></i>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'icon'}>
                <button type="button" className={`button button__icon`}>
                    <i data-button="icon" className={`icon icon__trash__red`}></i>
                </button>
            </ButtonProvider>

            <i className={`icon icon__lock__black`}></i>

            <StoryItem profileImg='data' profileName='data' currentLock='data' storyThum='data' currentLike='data' currentLikeCount='data' storyTitle='data' storyContent='data' storyLocation='data' storyDate />

            <StoryAddContext>
                <button type="button" className={`button button__story__add`} onClick={handleClick}>
                    <span className={`blind`}>스토리 등록</span>
                    <i className={`icon icon__plus__white`}></i>
                </button>
            </StoryAddContext>
        </>
    )
}

export default FormStyles;