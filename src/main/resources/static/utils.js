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

function generateJsonBodyWithType(formElemId, itemClassType) {
    let formValue = getJsonObj(formElemId);
    let jsonType =  "\"@type\":\"".concat(itemClassType, "\",");
    let jsonBodyStr = JSON.stringify(formValue);
    let jsonWithType = [jsonBodyStr.slice(0, 1), jsonType, jsonBodyStr.slice(1)].join('');
    return jsonWithType;
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

function generateJsonWithTypeFetch(pageToSend, method, formElemId, itemClassType) {
    return fetch(pageToSend, {
        method: method,
        body: generateJsonBodyWithType(formElemId, itemClassType),
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

function informAboutResult(formElemId, pageToRedirect) {
    return (res) => {
        try {
            if (res.ok) {
                let prettyItemJsonStr = JSON.stringify(getJsonObj(formElemId), null, 2);
                alert('Item to add: \n' + prettyItemJsonStr)
                window.location.href = pageToRedirect;
            } else {
                alert('Item NOT added');
                alert('Problem status: ' + res.status);
            }
        } catch (e) {
            informAboutError(e);
        }
    };
}

function fetchAddingItemFormWithJsonBody(formElemId, pageToSend, method, pageToRedirect) {
    generateJsonFetch(pageToSend, method, formElemId)
        .then(informAboutResult(formElemId, pageToRedirect));
}

function fetchAddingItemFormWithJsonBodyWithItemClassType(formElemId, pageToSend, method, pageToRedirect, itemClassType) {
    generateJsonWithTypeFetch(pageToSend, method, formElemId, itemClassType)
        .then(informAboutResult(formElemId, pageToRedirect));
}

