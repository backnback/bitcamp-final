import { createContext, useContext, useEffect, useState } from 'react';
import { InputProvider } from "./InputProvider";
import axios from 'axios';
import FormFileIcon from "../components/FormFileIcon";

const UserEditContext = createContext();

export const useStoryAddEditContext = () => useContext(UserEditContext);

const UserEdit = ({ password, setPassword, nickname, setNickname, profileImage, setProfileImage, accessToken }) => {
    const [filename, setFilename] = useState('');

    const handleImageChange = (e) => {
        setProfileImage(e.target.files[0]);
      };

    useEffect(() => {
        const authenticateUser = async () => {
            try {
                const response = await axios.post('http://localhost:8080/user/viewuser', {}, {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`,
                        'Content-Type': 'application/json'
                    },
                });

                if (response.data) {
                    setNickname(response.data.nickname);

                    // 파일이 있을 경우에만 설정
                    if (response.data.file) {
                        setFilename(response.data.filename);
                        setProfileImage(null); 
                    }
                } else {
                    alert("해당 정보를 가져오지 못했습니다.");
                }
            } catch (error) {
                console.error("회원 정보를 가져오던 중 오류 발생:", error);
            }
        };

        if (accessToken) {
            authenticateUser();
        }
    }, [accessToken, setNickname, setProfileImage]);

    return (
        <div>
             <label>프로필 사진</label>
                <img
                src={profileImage ? URL.createObjectURL(profileImage) : `https://kr.object.ncloudstorage.com/bitcamp-bucket-final/user/${filename}`}
                style={{ width: '200px', height: '200px', borderRadius: '50%', objectFit: 'cover' }}
                />
            <InputProvider>
                <label htmlFor="file01" className="form__label form__label__file">
                    <input type="file" className="blind" id="file01" onChange={handleImageChange} />
                    <FormFileIcon />
                </label>
            </InputProvider>

            <label>닉네임</label>
            <InputProvider>
                <input 
                    value={nickname} 
                    className={`form__input`} 
                    onChange={(e) => setNickname(e.target.value)} 
                />
            </InputProvider>
            <label>비밀번호</label>
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

export default UserEdit;
