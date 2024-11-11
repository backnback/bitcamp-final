import { createContext, useContext } from 'react';
import { InputProvider } from "./InputProvider";

const ReauthenticateContext = createContext();

export const useStoryAddEditContext = () => useContext(ReauthenticateContext);

const Reauthenticate = ({ password, setPassword }) => {
    return (
        <div>
            <label>비밀번호를 입력해 주세요</label>
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
