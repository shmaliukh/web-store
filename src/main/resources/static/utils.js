function getJsonObj(formElemId) {
    let form = document.getElementById(formElemId);
    let formData = new FormData(form);
    return Object.fromEntries(formData);
}

function generateJsonBodyWithType(formElemId, itemClassType) {
    let formValue = getJsonObj(formElemId);
    let jsonType = "\"@type\":\"".concat(itemClassType.toLowerCase(), "\",");
    let jsonBodyStr = JSON.stringify(formValue);
    return [jsonBodyStr.slice(0, 1), jsonType, jsonBodyStr.slice(1)].join('');
}

function fetchJson(pageToSend, method, formElemId) {
    return fetch(pageToSend, {
        method: method,
        body: JSON.stringify(getJsonObj(formElemId)),
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

function askToDeleteItem(itemId, pageToRedirect) {
    if (window.confirm('Do you really want to delete item with ' + itemId + ' id')) {
        fetch('/admin/item/' + itemId + '/delete', {
            method: "DELETE",
        }).then(informIfNotDeleted(pageToRedirect));
    }
}

function askToDelete(pageToDoDelete, pageToRedirect) {
    if (window.confirm('Do you really want to delete')) {
        fetch(pageToDoDelete, {
            method: "DELETE",
        }).then(informIfNotDeleted(pageToRedirect));
    }
}

function fetchAddingItemFormWithJsonBodyWithItemClassType(formElemId, pageToSend, method, pageToRedirect, itemClassType) {
    generateJsonWithTypeFetch(pageToSend, method, formElemId, itemClassType)
        .then(informAboutResult(formElemId, pageToRedirect, itemClassType));
}

async function getJsonFromApiCall(url) {
    return (await fetch(url)).json();
}

async function generateOptionUsingDataFromApi(elementId, url) {
    let select = document.getElementById(elementId);
    let jsonFromApiCall = await (getJsonFromApiCall(url));
    for (let i = 0; i < jsonFromApiCall.length; i++) {
        let opt = jsonFromApiCall[i];
        let el = document.createElement("option");
        el.textContent = opt;
        el.value = opt;
        select.appendChild(el);
    }
}
