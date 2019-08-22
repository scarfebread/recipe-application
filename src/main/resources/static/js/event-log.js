export const EventLog = (function () {
    const getTime = function () {
        const now = new Date();
        return '[' + now.getHours() + ':' + now.getMinutes() + ']';
    };

    return {
        add: function (event) {
            const eventLog = getElementById('eventLog');
            eventLog.innerText = getTime() + ' ' + event;
        }
    }
})();