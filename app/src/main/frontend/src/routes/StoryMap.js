import Map from '../components/Map';
import {useEffect, useState} from "react";
import axios from "axios";

function StoryMap() {
    const [storyList, setStoryList] = useState([]);
    const [accessToken, setAccessToken] = useState(null);

    useEffect(() => {
        if (accessToken) {
            const fetchStoryList = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/map', {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`
                        }
                    }); // API 요청
                    // setStoryList(response.data);
                    console.log(response.data)
                } catch (error) {
                    console.error("There was an error", error);
                }
            };
            fetchStoryList();
        }
    }, [accessToken]);

    // 로컬 스토리지에서 accessToken을 가져오는 함수
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            setAccessToken(token);
        } else {
            console.warn("Access token이 없습니다.");
        }
    }, []);

    return (
        <div>
            <Map />
        </div>
    )
}

export default StoryMap;