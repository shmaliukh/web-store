let incItemLink = "/shopping-cart/add-one/literature/newspaper/";
let decItemLink = "/shopping-cart/remove-one/literature/newspaper/";
let removeOneItemLink = "/shopping-cart/remove-all/";
let removeAllItemsLink = "/shopping-cart/remove-all-items";
let newCartItem;
const PUT_METHOD = "PUT";
const DELETE_METHOD = "DELETE";
function incCartItem(itemId){
    sendJSON(incItemLink+itemId, PUT_METHOD).then(res=>{
        changeCartItemPriceInPage(itemId,newCartItem.item.price*newCartItem.quantity);
        changeCartItemQuantityInPage(itemId,newCartItem.quantity);
        incCartItemsAllQuantityInPage();
        changeCartItemsTotalPriceOnDif(newCartItem.item.price,true);
    });
}

function decCartItem(itemId){
    sendJSON(decItemLink+itemId, PUT_METHOD).then(res=>{
        changeCartItemPriceInPage(itemId,newCartItem.item.price*newCartItem.quantity);
        changeCartItemQuantityInPage(itemId,newCartItem.quantity);
        minusCartItemsAllQuantityInPage(1);
        changeCartItemsTotalPriceOnDif(newCartItem.item.price, Boolean(false));
    });
}

function removeItem(itemId){
    sendJSON(removeOneItemLink+itemId, PUT_METHOD).then(res => {
        removeElement("allCartItems", "cartItem"+itemId);
        changeCartItemsTotalPriceOnDif(newCartItem.item.price*newCartItem.quantity,Boolean(false));
        minusCartItemsAllQuantityInPage(newCartItem.quantity);
    });
}

function removeAllItems(){
    sendJSON(removeAllItemsLink,DELETE_METHOD).then(res=>{
        removeElement("cart","allCartItems");
        setCartItemsAllQuantityInPage(0);
        setCartItemsTotalPriceInPage(0);
    });
}
async function sendJSON(link, method) {
    const requestOptions = {
        method: method,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ title:'' })
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
function changeCartItemPriceInPage(itemId, newPrice){
    document.getElementById('cartItemPrice' + itemId).innerHTML = newPrice;
}
function changeCartItemQuantityInPage(itemId, newQuantity){
    document.getElementById('cartItemQuantity' + itemId).innerHTML = newQuantity;
}
function incCartItemsAllQuantityInPage(){
    document.getElementById("totalItems").innerHTML = (parseInt(document.getElementById("totalItems").textContent)+1).toString();
}
function minusCartItemsAllQuantityInPage(dif){
    document.getElementById("totalItems").innerHTML = (parseInt(document.getElementById("totalItems").textContent,10)-dif).toString();
}
function setCartItemsAllQuantityInPage(quantity){
    document.getElementById("totalItems").innerHTML = quantity;
}
function setCartItemsTotalPriceInPage(price){
    document.getElementById("totalPrice").innerHTML = price;
}
function changeCartItemsTotalPriceOnDif(dif,add){
    let newPrice;
    let oldPrice = document.getElementById("totalPrice").textContent;
    if(Boolean(add)){
        newPrice = parseInt(oldPrice,10)+dif;
    } else {
        newPrice = parseInt(oldPrice,10)-dif;
    }
    document.getElementById("totalPrice").innerHTML = newPrice;
}

function removeElement(parentDiv,divToRemove) {
    var div = document.getElementById(parentDiv);
    var oldDiv = document.getElementById(divToRemove);
    div.removeChild(oldDiv);
}