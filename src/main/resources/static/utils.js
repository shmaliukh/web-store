//return "fetchFormWithJsonBody(" + formElemId + ", " + pageToSend + ", " + method + ", " + pageToRedirect + ")";

function getJsonObj(formElemId) {
    let form = document.getElementById(formElemId);
    let formData = new FormData(form);
    return Object.fromEntries(formData);
}

function generateJsonBodyWithType(formElemId, itemClassType) {
    let formValue = getJsonObj(formElemId);
    let jsonType = "\"@type\":\"".concat(itemClassType.toLowerCase(), "\",");
    let jsonBodyStr = JSON.stringify(formValue);
    let jsonWithType = [jsonBodyStr.slice(0, 1), jsonType, jsonBodyStr.slice(1)].join('');
    return jsonWithType;
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

function informAboutResult(formElemId, pageToRedirect, itemClassType) {
    return (res) => {
        try {
            if (res.ok) {
                let prettyItemJsonStr = getPrettyItemJsonStr(itemClassType, formElemId);
                alert('Item to save: \n' + prettyItemJsonStr)
                window.location.href = pageToRedirect;
            } else {
                alert('Item NOT saved');
                alert('Problem status: ' + res.status);
            }
        } catch (e) {
            informAboutError(e);
        }
    };
}

function getPrettyItemJsonStr(itemClassType, formElemId) {
    return itemClassType.concat(': ', JSON.stringify(getJsonObj(formElemId), null, 2));
}

function informIfNotDeleted(pageToRedirect) {
    return (res) => {
        try {
            if (!res.ok) {
                alert('Item NOT deleted');
                alert('Problem status: ' + res.status);
            }
        } catch (e) {
            informAboutError(e);
        }
        window.location.href = pageToRedirect;
    };
}

function askToDelete(formElemId, pageToSend, method, pageToRedirect, itemClassType) {
    let prettyItemJsonStr = getPrettyItemJsonStr(itemClassType, formElemId);
    if (window.confirm('Do you really want to delete: \n' + prettyItemJsonStr)) {
        fetch(pageToSend, {
            method: method,
            body: generateJsonBodyWithType(formElemId, itemClassType),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(informIfNotDeleted(pageToRedirect));
    }
}

function fetchAddingItemFormWithJsonBodyWithItemClassType(formElemId, pageToSend, method, pageToRedirect, itemClassType) {
    generateJsonWithTypeFetch(pageToSend, method, formElemId, itemClassType)
        .then(informAboutResult(formElemId, pageToRedirect, itemClassType));
}

function fetchDeletingItemFormWithJsonBodyWithItemClassType(formElemId, pageToSend, method, pageToRedirect, itemClassType) {
    askToDelete(formElemId, pageToSend, method, pageToRedirect, itemClassType);
}

