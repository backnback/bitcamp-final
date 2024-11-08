import axios from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import MapSeoul from "../components/map/MapSeoul";
import MapBusan from "../components/map/MapBusan";
import MapDaegu from "../components/map/MapDaegu";
import MapDaejeon from "../components/map/MapDaejeon";
import MapGwangwon from "../components/map/MapGwangwon";
import MapGwangju from "../components/map/MapGwangju";
import MapGyeonggi from "../components/map/MapGyeonggi";
import MapIncheon from "../components/map/MapIncheon";
import MapJeju from "../components/map/MapJeju";
import MapNorthGyeongsang from "../components/map/MapNorthGyeongsang";
import MapNorthJeolla from "../components/map/MapNorthJeolla";
import MapSejong from "../components/map/MapSejong";
import MapSouthChungcheong from "../components/map/MapSouthChungcheong";
import MapSouthGyeongsan from "../components/map/MapSouthGyeongsan";
import MapSouthJeolla from "../components/map/MapSouthJeolla";
import MapUlsan from "../components/map/MapUlsan";
import MapNorthChungcheoung from "../components/map/MapNorthChungcheoung";

function MapLocation() {
    const { locationId } = useParams(); // URL에서 ID 파라미터를 가져옴
    // const navigate = useNavigate(); // 페이지 이동을 위한 네비게이션 훅
    const [accessToken, setAccessToken] = useState(null);
    const [storyViewDTO, setStoryViewDTO] = useState(null);


    // 로컬 스토리지에서 accessToken을 가져오는 함수
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setAccessToken(token);
        } else {
            console.warn("Access token이 없습니다.");
        }
    }, []);


    // accessToken이 설정된 경우에만 fetchList 호출
    useEffect(() => {
        if (accessToken) {
            const fetchStoryViewDTO = async () => {
                try {
                    const response = await axios.get(`http://localhost:8080/my-story/list/${locationId}`, {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    });
                    setStoryViewDTO(response.data);
                } catch (error) {
                    console.error("스토리를 가져오는 중 오류가 발생했습니다!", error);
                }
            };
            fetchStoryViewDTO();
        }
    }, [accessToken, locationId]);


    // 삭제 버튼 처리
    // const handleDelete = async () => {
    //     const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
    //     if (confirmDelete) {
    //         try {
    //             await axios.delete(`http://localhost:8080/my-story/delete/${storyId}`, {
    //                 headers: {
    //                     'Authorization': `Bearer ${accessToken}`
    //                 }
    //             });
    //             alert("스토리가 삭제되었습니다.");
    //             navigate('/my-story/list'); // 삭제 후 목록 페이지로 이동
    //         } catch (error) {
    //             console.error("스토리 삭제 중 오류가 발생했습니다!", error);
    //             alert("스토리 삭제에 실패했습니다.");
    //         }
    //     }
    // };

    return (
        <>
            <MapSeoul />
            <MapBusan />
            <MapDaegu />
            <MapDaejeon />
            <MapGwangju />
            <MapGwangwon />
            <MapGyeonggi />
            <MapIncheon />
            <MapJeju />
            <MapNorthChungcheoung />
            <MapNorthGyeongsang />
            <MapNorthJeolla />
            <MapSejong />
            <MapSouthChungcheong />
            <MapSouthGyeongsan />
            <MapSouthJeolla />
            <MapUlsan />
        </>
    )
}

export default MapLocation;