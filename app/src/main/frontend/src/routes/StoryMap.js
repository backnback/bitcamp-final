import Map from '../components/Map';
import styles from '../assets/styles/css/Map.css';
import { useEffect } from 'react';

function StoryMap() {
    useEffect(() => {
        document.body.className = 'body body__story body__map'
    })
    return (
        <div className={`map__container`}>
            <Map />
        </div>
    )
}

export default StoryMap;