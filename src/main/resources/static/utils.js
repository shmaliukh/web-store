//return "fetchFormWithJsonBody(" + formElemId + ", " + pageToSend + ", " + method + ", " + pageToRedirect + ")";

function getJsonObj(formElemId) {
    let form = document.getElementById(formElemId);
    let formData = new FormData(form);
    return Object.fromEntries(formData);
}

function generateJsonBody(formElemId) {
    let formValue = getJsonObj(formElemId);
    return JSON.stringify(formValue);
}

function generateJsonFetch(pageToSend, method, formElemId) {
    return fetch(pageToSend, {
        method: method,
        body: generateJsonBody(formElemId),
        headers: {
            "Content-Type": "application/json"
        }
    });
}

function informAboutError(e) {
    alert('Error: ' + e.name + ":" + e.message + "\n" + e.stack);
}

function fetchFormWithJsonBody(formElemId, pageToSend, method, pageToRedirect) {
    generateJsonFetch(pageToSend, method, formElemId)
        .then((res) => {
            try {
                if (res.ok) {
                    window.location.href = pageToRedirect;
                } else {
                    alert('Problem status: ' + res.status);
                }
            } catch (e) {
                informAboutError(e);
            }
        });
}

function fetchAddingItemFormWithJsonBody(formElemId, pageToSend, method, pageToRedirect) {
    generateJsonFetch(pageToSend, method, formElemId)
        .then((res) => {
            try {
                if (res.ok) {
                    let prettyItemJsonStr = 'Magazine ' +  JSON.stringify(getJsonObj(formElemId), null, 2);
                    alert('Item to add: \n' + prettyItemJsonStr)
                    window.location.href = pageToRedirect;
                } else {
                    alert('Item NOT added');
                    alert('Problem status: ' + res.status);
                }
            } catch (e) {
                informAboutError(e);
            }
        });
}

