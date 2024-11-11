import { createContext, useContext } from 'react';
import { InputProvider } from "./InputProvider";

const ReauthenticateContext = createContext();

export const useStoryAddEditContext = () => useContext(ReauthenticateContext);

const Reauthenticate = ({ password, setPassword }) => {
    return (
        <div>
            <InputProvider>
                <input
                    type='text'
                    className={`form__input`}
                    defaultValue='비밀번호를 입력해 주세요'
                    id='text01'
                    name='텍수투'
                    placeholder='title' />
            </InputProvider>
            <InputProvider>
                <input
                    type="password"
                    className="form__input"
                    id="pwd01"
                    name="비밀번호"
                    value={password}
                    placeholder="비밀번호"
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
            </InputProvider>
        </div>
    );
}

export default Reauthenticate;
