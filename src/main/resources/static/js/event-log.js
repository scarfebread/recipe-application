EventLog = (function () {
    let getTime = function () {
        let now = new Date();
        return '[' + now.getHours() + ':' + now.getMinutes() + ']';
    };

    return {
        add: function (event) {
            let eventLog = getElementById('eventLog');
            eventLog.innerText = getTime() + ' ' + event;
        }
    }
})();