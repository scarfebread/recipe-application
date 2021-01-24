const ApiClient = (() => {
    const method = {
        POST: 'POST'
    }

    const callApi = (url, method, body, success, failure) => {
        const request = {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "same-origin"
        };

        if (body) {
            request.body = JSON.stringify(body);
        }

        fetch (url, request).then(
            function (response) {
                if (!response.ok) {
                    response.text().then((data) => {
                        failure(data);
                    });

                    return;
                }

                const contentType = response.headers.get("content-type");
                if (contentType && contentType.indexOf("application/json") !== -1) {
                    return response.json().then(data => {
                        success(data);
                    });
                } else {
                    success();
                }
            }
        ).catch(
            function (error) {
                failure(error);
            }
        );
    }

    return {
        post : (url, body, success, failure) => {
            callApi(url, method.POST, body, success, failure);
        }
    }
})();

export default ApiClient;
