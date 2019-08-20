document.addEventListener("DOMContentLoaded", function() {
    DeleteAccount.init();
});

DeleteAccount = (function () {
    let accountDeleted = false;

    let addConfirmDeleteListener = function () {
        let confirmDeleteButton = getElementById('confirmDeleteButton');
        confirmDeleteButton.onclick = function () {
            if (accountDeleted) {
                return;
            }

            deleteAccount();
        };
    };

    let addCloseModalListeners = function () {
        let closeModalButton = document.getElementsByClassName("close")[0];
        let cancelDeleteButton = getElementById('cancelDeleteButton');

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

    let addDisplayModalListener = function () {
        let deleteAccountButton = getElementById('deleteAccountButton');
        let modal = getElementById('deleteAccountModal');

        deleteAccountButton.onclick = function() {
            modal.style.display = 'block';
        };
    };

    let deleteAccount = function () {
        let successCallback = function() {
            accountDeleted = true;
            hideElement('preAccountDeleted');
            showElement('postAccountDeleted');
        };

        let failureCallback = function (failure) {
            getElementById('deleteAccountError').innerText = failure;
            showElement('deleteAccountError');
        };

        callApi('/api/user', HTTP_DELETE, null, false, successCallback, failureCallback);
    };

    let closeModal = function (modal) {
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
