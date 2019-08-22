document.addEventListener("DOMContentLoaded", function() {
    DeleteAccount.init();
});

export const DeleteAccount = (function () {
    let accountDeleted = false;

    const addConfirmDeleteListener = function () {
        const confirmDeleteButton = getElementById('confirmDeleteButton');
        confirmDeleteButton.onclick = function () {
            if (accountDeleted) {
                return;
            }

            deleteAccount();
        };
    };

    const addCloseModalListeners = function () {
        const closeModalButton = document.getElementsByClassName("close")[0];
        const cancelDeleteButton = getElementById('cancelDeleteButton');

        cancelDeleteButton.onclick = function() {
            closeModal(modal);
        };

        closeModalButton.onclick = function() {
            closeModal(modal);
        };

        window.onclick = function(event) {
            if (event.target === modal) {
                closeModal(modal);
            }
        };

        window.onkeydown = function(event) {
            if (event.key === 'Escape') {
                closeModal(modal);
            }
        };
    };

    const addDisplayModalListener = function () {
        const deleteAccountButton = getElementById('deleteAccountButton');
        const modal = getElementById('deleteAccountModal');

        deleteAccountButton.onclick = function() {
            modal.style.display = 'block';
        };
    };

    const deleteAccount = function () {
        const successCallback = function() {
            accountDeleted = true;
            hideElement('preAccountDeleted');
            showElement('postAccountDeleted');
        };

        const failureCallback = function (failure) {
            getElementById('deleteAccountError').innerText = failure;
            showElement('deleteAccountError');
        };

        callApi('/api/user', HTTP_DELETE, null, false, successCallback, failureCallback);
    };

    const closeModal = function (modal) {
        if (accountDeleted) {
            window.location.href = '/'
        }

        hideElement('deleteAccountError');
        modal.style.display = "none";
    };

    return {
        init: function () {
            addConfirmDeleteListener();
            addCloseModalListeners();
            addDisplayModalListener();
        }
    }
})();
