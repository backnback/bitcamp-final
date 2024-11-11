import React from 'react';
import ReactDOM from 'react-dom/client';
import ReactModal from 'react-modal';
import './assets/styles/css/reset.css';
// import styles
import "@egjs/flicking/dist/flicking.css"; // Supports IE10+, using CSS flex
import './assets/styles/css/commons.css';
import App from './App';
import ModalsProvider from './components/ModalProvider';
// import reportWebVitals from './reportWebVitals';

ReactModal.setAppElement('#root');

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    <ModalsProvider>
      <App />
    </ModalsProvider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
// reportWebVitals();
