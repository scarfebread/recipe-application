let accountDeleted = false;

document.addEventListener("DOMContentLoaded", function() {
    DeleteAccount.init();
});

DeleteAccount = {
    init: function () {
        this.bindUserActions();
    },

    bindUserActions: function () {
        let deleteAccountButton = getElementById('deleteAccountButton');
        let confirmDeleteButton = getElementById('confirmDeleteButton');
        let cancelDeleteButton = getElementById('cancelDeleteButton');
        let modal = getElementById('deleteAccountModal');
        let closeModalButton = document.getElementsByClassName("close")[0];

        deleteAccountButton.onclick = function() {
            modal.style.display = 'block';
        };

        cancelDeleteButton.onclick = function() {
            DeleteAccount.closeModal(modal);
        };

        confirmDeleteButton.onclick = function () {
            if (accountDeleted) {
                return;
            }

            DeleteAccount.deleteAccount();
        };

        closeModalButton.onclick = function() {
            DeleteAccount.closeModal(modal);
        };

        window.onclick = function(event) {
            if (event.target === modal) {
                DeleteAccount.closeModal(modal);
            }
        };

        window.onkeydown = function(event) {
            if (event.key === 'Escape') {
                DeleteAccount.closeModal(modal);
            }
        };
    },

    deleteAccount: function () {
        let successCallback = function(success) {
            hideElement('preAccountDeleted');
            showElement('postAccountDeleted');
        };

        let failureCallback = function (failure) {
            getElementById('deleteAccountError').innerText = failure;
            showElement('deleteAccountError');
        };

        callApi('/api/user', HTTP_DELETE, null, false, successCallback, failureCallback);
    },

    closeModal: function (modal) {
        hideElement('deleteAccountError');
        modal.style.display = "none";
    },
};
