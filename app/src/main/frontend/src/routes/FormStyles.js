import FormFileIcon from "../components/FormFileIcon";
import { InputProvider } from '../components/InputProvider';
import { ButtonProvider } from '../components/ButtonProvider';
import { SelectProvider } from '../components/SelectProvider';
import { TitleProvider, TextProvider } from "../components/TitleProvider";
import iconStyles from "../assets/styles/css/Icon.module.css";
import commonStyles from "../assets/styles/css/Commons.module.css";

function FormStyles() {
    return (
        <>
            <TitleProvider children={
                <h5 data-title='h5'>제목</h5>
            } />

            <TextProvider children={
                <div>
                    <p data-text='p'>친구들과 함께 놀이공원에서 즐겁게 놀고왔다.</p>
                    <br />
                    <p data-text='p'>#놀이공원 #민지피티</p>
                </div>
            } />

            <InputProvider>
                <input />
            </InputProvider>

            <InputProvider>
                <label htmlFor="checkbox01" data-label="checkbox">
                    <input
                        type='checkbox'
                        defaultChecked='checked'
                        id='checkbox01'
                        name='체크체크' />
                    <span data-input="text">체크해라</span>
                </label>
            </InputProvider>

            <InputProvider>
                <label htmlFor="checkbox02" data-label="checkbox">
                    <input
                        type='checkbox'
                        id='checkbox02'
                        name='체크체크' />
                    <span data-input="text">체크하지마러라</span>
                </label>
            </InputProvider>

            <InputProvider>
                <label htmlFor="radio01" data-label="radio">

                    <input
                        type='radio'
                        defaultChecked='checked'
                        id='radio01'
                        name='라디오' />
                    <span data-input="text">라디오버튼이여</span>

                </label>
            </InputProvider>

            <InputProvider>
                <label htmlFor="radio02" data-label="radio">
                    <input
                        type='radio'
                        id='radio02'
                        name='라디오' />
                    <span data-input="text">라디오버튼이여</span>
                </label>
            </InputProvider>

            <InputProvider>
                <input
                    type='text'
                    defaultValue='텍스트를 작성하는건? value'
                    id='text01'
                    name='텍수투'
                    placeholder='title' />
            </InputProvider>

            <InputProvider>
                <input
                    type='password'
                    id='pwd01'
                    name='패수워드'
                    placeholder='비밀번호' />
            </InputProvider>

            <InputProvider>
                <input
                    type='email'
                    id='email01'
                    name='이메일'
                    defaultValue='test@test.com'
                    placeholder='이메일입력' />
            </InputProvider>

            <InputProvider>
                <label htmlFor="file01" data-label="file">
                    <input type='file' id="file01" />
                    <FormFileIcon />
                </label>
            </InputProvider>

            <InputProvider>
                <textarea
                    id='textarea01'
                    placeholder='내용 입력'
                ></textarea>
            </InputProvider>

            <ButtonProvider>
                <button type="button" data-button='primary'>
                    <span data-button="text">등록</span>
                </button>
            </ButtonProvider>

            <ButtonProvider>
                <button type="button" data-button='whiteRed'>
                    <span data-button="text">삭제</span>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'130'}>
                <button type="button" data-button='whitePrimary'>
                    <span data-button="text">삭제</span>
                </button>
            </ButtonProvider>

            <SelectProvider>
                <select id="select01" name="selectName">
                    <option value={'0'}>선택해라</option>
                    <option value={'1'}>2021</option>
                    <option value={'2'}>2022</option>
                    <option value={'3'}>2023</option>
                </select>
            </SelectProvider>

            <ButtonProvider width={'icon'}>
                <button type="button" data-button='iconButton'>
                    <i data-button="icon" className={`${iconStyles.icon} ${iconStyles.arrow__right__black}`}></i>
                    <span className={commonStyles.blind}>닫기</span>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'icon'}>
                <button type="button" data-button='iconButton'>
                    <i data-button="icon" className={`${iconStyles.icon} ${iconStyles.edit__black}`}></i>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'icon'}>
                <button type="button" data-button='iconButton'>
                    <i data-button="icon" className={`${iconStyles.icon} ${iconStyles.share__black}`}></i>
                </button>
            </ButtonProvider>

            <ButtonProvider width={'icon'}>
                <button type="button" data-button='iconButton'>
                    <i data-button="icon" className={`${iconStyles.icon} ${iconStyles.trash__red}`}></i>
                </button>
            </ButtonProvider>

            <i className={`${iconStyles.icon} ${iconStyles.lock__black}`}></i>
        </>
    )
}

export default FormStyles;