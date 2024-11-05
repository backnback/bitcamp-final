import { useEffect, useState } from 'react';

function FormFileIcon() {
    return (
        <>
            <i className={`icon icon__plus__gray form__input__file__icon`}></i>
            <strong className={`form__file__text`}>사진 등록</strong>
        </>
    );
}

export default FormFileIcon;