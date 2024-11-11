import { createContext, useContext } from 'react';


export const PhotosProvider = ({ photos, viewMode, className, itemClassName, layout }) => (
    <div className={`photo__photos ${viewMode ? `` : `photo__photos__noMain`} ${className != null ? className : ``}`}>
        {photos.length > 0 ? (
            <div className={`photo__layout ${layout ? `photo__layout__custom` : ``}`}>
                {photos.map(photo => (
                    <div className={`photo__photoItem ${itemClassName ? `photo__photoItem__custom` : ``}`} key={photo.id}>
                        <img
                            src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/story/${photo.path ? photo.path : 'default.png'}`}
                            alt={`Photo ${photo.id}`}
                            className={`story-photo ${photo.mainPhoto ? 'main-photo' : ''}`}
                        />
                        {photo.mainPhoto && <span className="main-label">대표</span>}
                    </div>
                ))}
            </div>
        ) : (
            <p>사진이 없습니다.</p>
        )}
    </div>
);
