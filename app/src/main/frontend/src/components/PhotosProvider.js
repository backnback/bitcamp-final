import React from 'react';

export const PhotosProvider = ({
    photos,
    className = '',
    itemClassName = '',
    layout = 'gallery',
    emptyMessage = '사진이 없습니다.'
}) => (
    <div className={`photos ${className}`}>
        {photos.length > 0 ? (
            <div className={`photo-${layout}`}>
                {photos.map(photo => (
                    <div className={`photo ${itemClassName}`} key={photo.id}>
                        <img
                            src={`https://kr.object.ncloudstorage.com/bitcamp-bucket-final/story/${photo.path ? photo.path : 'default.png'}`}
                            alt={`Photo ${photo.id}`}
                            className={`story-photo ${photo.mainPhoto ? 'main-photo' : ''}`}
                        />
                        {photo.mainPhoto && <span className="main-label">main</span>}
                    </div>
                ))}
            </div>
        ) : (
            <p>{emptyMessage}</p>
        )}
    </div>
);
