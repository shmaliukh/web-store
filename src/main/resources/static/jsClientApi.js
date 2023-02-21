let incItemLink = "/cart-item/add-one/literature/newspaper/";
let decItemLink = "/cart-item/remove-one/literature/newspaper/";
let removeOneItemLink = "/cart-item/remove-all/";
let removeAllItemsLink = "/cart-item/remove-all-items";
let editUsernameLink = "/user/edit-username-user-page";
let editUserEmailLink = "/user/edit-email-user-page";
let editUserAvatarLink = "/user/edit-avatar-user-page";

const POST_METHOD = "POST";
const PUT_METHOD = "PUT";
const DELETE_METHOD = "DELETE";

const ID_CART_ITEM_QUANTITY = "cartItemQuantity";
const ID_CART_ITEM_PRICE = "cartItemPrice";
const ID_ALL_CART_ITEMS = "allCartItems";
const ID_CART_ITEM = "cartItem";
const ID_CART = "cart";
const ID_TOTAL_ITEMS = "totalItems";
const ID_TOTAL_PRICE = "totalPrice";
const ID_EMAIL = "email";
const ID_USERNAME = "username";


let newCartItem;

const emptyBody = {title:''} ;

function incCartItem(itemId){
    sendJSON(incItemLink+itemId, PUT_METHOD,emptyBody).then(res=>{
        setNewValueInPage(ID_CART_ITEM_PRICE+itemId,newCartItem.item.price*newCartItem.quantity);
        setNewValueInPage(ID_CART_ITEM_QUANTITY+itemId,newCartItem.quantity);
        incCartItemsTotalQuantityInPage();
        changeCartItemsTotalPriceOnDif(newCartItem.item.price,true);
    });
}

function decCartItem(itemId){
    sendJSON(decItemLink+itemId, PUT_METHOD,emptyBody).then(res=>{
        setNewValueInPage(ID_CART_ITEM_PRICE+itemId,newCartItem.item.price*newCartItem.quantity);
        setNewValueInPage(ID_CART_ITEM_PRICE+itemId,newCartItem.quantity);
        minusCartItemsTotalQuantityInPage(1);
        changeCartItemsTotalPriceOnDif(newCartItem.item.price, Boolean(false));
    });
}

function removeItem(itemId){
    sendJSON(removeOneItemLink+itemId, PUT_METHOD,emptyBody).then(res => {
        removeElement(ID_ALL_CART_ITEMS, ID_CART_ITEM+itemId);
        changeCartItemsTotalPriceOnDif(newCartItem.item.price*newCartItem.quantity,Boolean(false));
        minusCartItemsTotalQuantityInPage(newCartItem.quantity);
    });
}

function removeAllItems(){
    sendJSON(removeAllItemsLink,DELETE_METHOD,emptyBody).then(res=>{
        removeElement(ID_CART,ID_ALL_CART_ITEMS);
        setNewValueInPage(ID_TOTAL_ITEMS,0);
        setNewValueInPage(ID_TOTAL_PRICE,0);
    });
}

async function sendJSON(link, method,body) {
    const requestOptions = {
        method: method,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: body
    };
    await fetch(link,requestOptions)
        .then((res) => {
            if (res.ok) {
                return res.json();
            }
        }).then(function (data) {
            console.log(data);
            newCartItem = data;
        });
}

function setNewValueInPage(tagId,newValue){
    document.getElementById(tagId).innerHTML = newValue;
}

function incCartItemsTotalQuantityInPage(){
    setNewValueInPage(ID_TOTAL_ITEMS,(parseInt(document.getElementById(ID_TOTAL_ITEMS).textContent)+1).toString());
}

function minusCartItemsTotalQuantityInPage(dif){
    setNewValueInPage(ID_TOTAL_ITEMS,(parseInt(document.getElementById(ID_TOTAL_ITEMS).textContent,10)-dif).toString());
}

function changeCartItemsTotalPriceOnDif(dif,add){
    let newPrice;
    let oldPrice = document.getElementById(ID_TOTAL_PRICE).textContent;
    if(Boolean(add)){
        newPrice = parseInt(oldPrice,10)+dif;
    } else {
        newPrice = parseInt(oldPrice,10)-dif;
    }
    document.getElementById(ID_TOTAL_PRICE).innerHTML = newPrice;
}

function removeElement(parentDiv,divToRemove) {
    var div = document.getElementById(parentDiv);
    var oldDiv = document.getElementById(divToRemove);
    div.removeChild(oldDiv);
}

function clearField(fieldId){
    document.getElementById(fieldId).value = '';
}

function validateEmail(input, tagId) {
    var validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    if (!input.match(validRegex)) {
        alert("Invalid email address!");
        document.document.getElementById(tagId).value.focus();
        return false;
    }
    return true;
}

function validateUsername(username,tagId){
    const validRegex = /^[a-z0-9_.]+$/;
    if (!username.match(validRegex)) {
        alert("Invalid username!");
        document.document.getElementById(tagId).value.focus();
        return false;
    }
    return true;
}

function saveUserChanges(link,tagId){

    var userChanges = document.getElementById(tagId).value;
    alert(userChanges);
    var isValid;
    if(tagId===ID_EMAIL){
        isValid = validateEmail(userChanges,tagId);
    } else if (tagId===ID_USERNAME){
        isValid=validateUsername(userChanges);
    }
    if(Boolean(isValid)){
        sendJSON(link, PUT_METHOD, userChanges).then(res=>{
            clearField(tagId);
        });
    }

}

function addItemToCart(itemId){
    let link = document.getElementById("cartButton" + itemId).getAttribute("href");
    alert(link);
    sendJSON(link,POST_METHOD,JSON.stringify({itemId:itemId})).then(res=>{
        alert(res);
    });
}