let incItemLink = "/shopping-cart/add-one/literature/newspaper/";
let decItemLink = "/shopping-cart/remove-one/literature/newspaper/";
let removeOneItemLink = "/shopping-cart/remove-all/";
let removeAllItemsLink = "/shopping-cart/remove-all-items";

const PUT_METHOD = "PUT";
const DELETE_METHOD = "DELETE";

const ID_CART_ITEM_QUANTITY = "cartItemQuantity";
const ID_CART_ITEM_PRICE = "cartItemPrice";
const ID_ALL_CART_ITEMS = "allCartItems";
const ID_CART_ITEM = "cartItem";
const ID_CART = "cart";
const ID_TOTAL_ITEMS = "totalItems";
const ID_TOTAL_PRICE = "totalPrice";


let newCartItem;


function incCartItem(itemId){
    sendJSON(incItemLink+itemId, PUT_METHOD).then(res=>{
        setNewValueInPage(ID_CART_ITEM_PRICE+itemId,newCartItem.item.price*newCartItem.quantity);
        setNewValueInPage(ID_CART_ITEM_QUANTITY+itemId,newCartItem.quantity);
        incCartItemsTotalQuantityInPage();
        changeCartItemsTotalPriceOnDif(newCartItem.item.price,true);
    });
}

function decCartItem(itemId){
    sendJSON(decItemLink+itemId, PUT_METHOD).then(res=>{
        setNewValueInPage(ID_CART_ITEM_PRICE+itemId,newCartItem.item.price*newCartItem.quantity);
        setNewValueInPage(ID_CART_ITEM_PRICE+itemId,newCartItem.quantity);
        minusCartItemsTotalQuantityInPage(1);
        changeCartItemsTotalPriceOnDif(newCartItem.item.price, Boolean(false));
    });
}

function removeItem(itemId){
    sendJSON(removeOneItemLink+itemId, PUT_METHOD).then(res => {
        removeElement(ID_ALL_CART_ITEMS, ID_CART_ITEM+itemId);
        changeCartItemsTotalPriceOnDif(newCartItem.item.price*newCartItem.quantity,Boolean(false));
        minusCartItemsTotalQuantityInPage(newCartItem.quantity);
    });
}

function removeAllItems(){
    sendJSON(removeAllItemsLink,DELETE_METHOD).then(res=>{
        removeElement(ID_CART,ID_ALL_CART_ITEMS);
        setNewValueInPage(ID_TOTAL_ITEMS,0);
        setNewValueInPage(ID_TOTAL_PRICE,0);
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