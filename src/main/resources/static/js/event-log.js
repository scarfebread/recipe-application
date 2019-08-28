export const EventLog = (function () {
    const getTime = function () {
        const now = new Date();
        return '[' + zeroPad(now.getHours()) + ':' + zeroPad(now.getMinutes()) + ']';
    };

    const zeroPad = function (number) {
        if (number < 10) {
            return `0${number}`;
        } {
            return number;
        }
    };

    return {
        add: function (event) {
            const eventLog = getElementById('eventLog');
            eventLog.innerText = getTime() + ' ' + event;
        }
    }
})();