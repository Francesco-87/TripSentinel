import {useRef, useEffect, useId} from "react";
import "../../styles/Modal.css";


function Modal({ children, title, onClose }) {

    const modalRef = useRef(null);
    const titleId = useId();

    useEffect(() => {
        const dialog = modalRef.current;
        dialog.showModal();

        return () => dialog.close();
    }, []);

    return (
        <dialog ref={modalRef} onCancel={onClose} aria-labelledby={titleId} className="modal">
            <header className="modal__header">
                <button className="modal__close" type="button" aria-label="Close" onClick={onClose}>
                    ×
                </button>
                <h2 id={titleId}>{title}</h2>
              
            </header>
            <div className="modal__body">
                {children}
            </div>
        </dialog>
    );
}

export default Modal
