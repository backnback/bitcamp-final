import { useEffect, useState } from 'react';
import iconStyles from '../assets/styles/css/Icon.module.css';
import styles from '../assets/styles/css/Form.module.css';

function FormFileIcon() {
    return (
        <>
            <i className={`${iconStyles.icon} ${iconStyles.plus__gray} ${styles.input__file__icon}`}></i>
            <strong className={`${styles.file__text}`}>사진 등록</strong>
        </>
    );
}

export default FormFileIcon;