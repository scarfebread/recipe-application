import {useEffect, useState, createRef} from 'react';

const useModal = (onClose) => {
    const [shouldShowModal, setShowModal] = useState(false);
    const modal = createRef();

    useEffect(() => {
        const handleClickOffModal = (event) => {
            if (shouldShowModal && event.target === modal.current) {
                setShowModal(false);
                onClose();
            }
        }

        window.onclick = function(event) {
            handleClickOffModal(event);
        };

        return () => {
            window.removeEventListener('onclick', handleClickOffModal)
        }
    });

    useEffect(() => {
        const handleEscapeKey = (event) => {
            if (event.key === 'Escape' && shouldShowModal) {
                setShowModal(false);
                onClose();
            }
        }

        window.onkeydown = function(event) {
            handleEscapeKey(event);
        };

        return () => {
            window.removeEventListener('onkeydown', handleEscapeKey)
        }
    });

    const showModal = () => {
        setShowModal(true);
    }

    const hideModal = () => {
        setShowModal(false);
        onClose();
    }

    return [modal, shouldShowModal, showModal, hideModal]
};

export default useModal;