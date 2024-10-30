import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

function ViewUser() {
  const { id } = useParams();
  const [user, setUser] = useState(null);
  const navigate = useNavigate(); // 페이지 이동을 위한 navigate 함수

  useEffect(() => {
    console.log("URL에서 가져온 ID:", id); // 여기서 ID 값을 출력하여 확인
    fetchUser();
  }, [id]);

  // 사용자 정보 가져오기
  const fetchUser = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/user/finduser?id=${id}`);
      setUser(response.data); // 사용자 정보를 가져오기 (배열이 아니라 객체일 경우)
    } catch (error) {
      console.error("사용자 정보를 가져오는 중 오류 발생:", error);
    }
  };

  // 사용자 정보가 없을 경우 로딩 상태 표시
  if (!user) {
    return <div>Loading...</div>; // 또는 다른 대체 UI
  }

const handleUpdate = async (e) => {
    e.preventDefault(); // 폼 기본 동작 방지

    // FormData 객체 생성
    const formData = new FormData();

    // 수정된 사용자 정보 추가
    formData.append("nickname", e.target.nickname.value);
    formData.append("password", e.target.password.value);
    formData.append("id", user.id); // ID 추가

    // 프로필 이미지 파일 추가
    const fileInput = e.target.profileImage; // 파일 input의 name 속성 확인
    if (fileInput && fileInput.files[0]) {
        formData.append("file", fileInput.files[0]);
    }

    try {
        // 사용자 정보 업데이트 요청
        await axios.post(`http://localhost:8080/user/update`, formData, {
            headers: { "Content-Type": "multipart/form-data" },
        });

        // 업데이트 후 app.js 페이지로 리다이렉트
        window.location.replace('/'); // app.js 페이지로 이동
    } catch (error) {
        console.error("사용자 정보를 업데이트하는 중 오류 발생:", error);
    }
};

  // 삭제하기 핸들러
  const handleDelete = async () => {
    try {
      // 사용자 삭제 요청 (DELETE 방식)
      await axios.delete(`http://localhost:8080/user/delete/${user.id}`);
      // 삭제 후 app.js 페이지로 리다이렉트
      window.location.replace('/'); // app.js 페이지로 이동
    } catch (error) {
      console.error("사용자 정보를 삭제하는 중 오류 발생:", error);
    }
  };

  return (
    <form onSubmit={handleUpdate}> {/* form 요소로 감싸서 onSubmit 처리 */}
      <h2>사용자 정보</h2>
      <img
        src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/user/${user.path ? user.path : 'default.png'}`}
        alt="프로필 이미지"
        style={{ width: '150px', height: '150px' }}
      />

      <dl>
        <dt>이메일: </dt>
        <dd>{user.email}</dd>
      </dl>
      <div>
        <label htmlFor="user-password">비밀번호: </label>
        <input type="password" id="user-password" name="password" defaultValue={user.password} placeholder="비밀번호를 입력해주세요." />
      </div>
      <div>
        <label htmlFor="user-nickname">닉네임: </label>
        <input type="text" id="user-nickname" name="nickname" defaultValue={user.nickname} placeholder="닉네임을 입력해주세요." />
      </div>
      <div>
        <label htmlFor="user-profile">프로필 이미지: </label>
        <input type="file" id="user-profile" name="profileImage" /> {/* name 속성 추가 */}
      </div>
      <button type="submit">수정하기</button> {/* 수정하기 버튼 */}
      <button type="button" onClick={handleDelete}>삭제하기</button> {/* 삭제하기 버튼 */}
    </form>
  );
}

export default ViewUser;