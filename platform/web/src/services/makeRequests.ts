const apiURL = "http://192.168.100.209:8080/api/v1";

export const postReq = async (url: string, body: any, headers?: Record<string, string>) => {
    try {
        const response = await fetch(`${apiURL}/${url}`, {
            method: "POST",
            headers: headers || {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
        });
        const data = await response.json();
        return Promise.resolve([data, null]);
    } catch (err) {
        return Promise.resolve([null, err]);
    }
};