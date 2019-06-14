let accountDeleted = false;

document.addEventListener("DOMContentLoaded", function(event)
{
    let deleteAccountButton = getElementById('deleteAccountButton');
    let confirmDeleteButton = getElementById('confirmDeleteButton');
    let cancelDeleteButton = getElementById('cancelDeleteButton');
    let modal = getElementById('deleteAccountModal');
    let closeModalButton = document.getElementsByClassName("close")[0];

    deleteAccountButton.onclick = function() {
        modal.style.display = 'block';
    };

    cancelDeleteButton.onclick = function() {
        closeModal(modal);
    };

    confirmDeleteButton.onclick = function () {
        if (accountDeleted) {
            return;
        }

        deleteAccount();
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
});

function deleteAccount()
{
    let successCallback = function() {
        hideElement('preAccountDeleted');
        showElement('postAccountDeleted');
    };

    let failureCallback = function (failure) {
        getElementById('deleteAccountError').innerText = failure;
        showElement('deleteAccountError');
    };

    callApi('/api/user', HTTP_DELETE, null, successCallback, failureCallback)
}

function closeModal(modal)
{
    hideElement('deleteAccountError');
    modal.style.display = "none";
}