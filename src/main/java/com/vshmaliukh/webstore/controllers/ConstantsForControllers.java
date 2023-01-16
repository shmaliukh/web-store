package com.vshmaliukh.webstore.controllers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ConstantsForControllers {

    public static final String DEFAULT_ITEM_QUANTITY_ON_PAGE = "5";

    private ConstantsForControllers() {
    }

    public static final String ORDER_PAGE = "order";
    public static final String PAGE_403 = "403";
    public static final String OAUTH_LOGIN_PAGE = "oauth-login";
    public static final String HOME_PAGE = "home";
    public static final String LOGIN_PAGE = "login";
    public static final String MAIN_PAGE = "main";
    public static final String CATALOG_PAGE = "catalog";
    public static final String CATEGORY_PAGE = "category";
    public static final String SHOPPING_CART = "shopping-cart";
    public static final String USER_HOME = "user-home";

    // TODO refactor
    public static String ORDER_STATUS_COMPLETED = "Completed";
    public static Map<String, String> orderStatusDescriptionMap;

    static {
        orderStatusDescriptionMap = new ConcurrentHashMap<>();

        orderStatusDescriptionMap.put("Pending", "Customer started the checkout process but did not complete it. Incomplete orders are assigned a \'Pending\' status and can be found under the More tab in the View Orders screen.");
        orderStatusDescriptionMap.put("Awaiting Payment", "Customer has completed the checkout process, but payment has yet to be confirmed. Authorize only transactions that are not yet captured have this status.");
        orderStatusDescriptionMap.put("Awaiting Fulfillment", "Customer has completed the checkout process and payment has been confirmed.");
        orderStatusDescriptionMap.put("Awaiting Shipment", "Order has been pulled and packaged and is awaiting collection from a shipping provider.");
        orderStatusDescriptionMap.put("Awaiting Pickup", "Order has been packaged and is awaiting customer pickup from a seller-specified location.");
        orderStatusDescriptionMap.put("Partially Shipped", "Only some items in the order have been shipped.");
        orderStatusDescriptionMap.put("Completed", "Order has been shipped/picked up, and receipt is confirmed; client has paid for their digital product, and their file(s) are available for download.");
        orderStatusDescriptionMap.put("Shipped", "Order has been shipped, but receipt has not been confirmed; seller has used the Ship Items action. A listing of all orders with a \'Shipped\' status can be found under the More tab of the View Orders screen.");
        orderStatusDescriptionMap.put("Cancelled", "Seller has cancelled an order, due to a stock inconsistency or other reasons. Stock levels will automatically update depending on your Inventory Settings. Cancelling an order will not refund the order. This status is triggered automatically when an order using an authorize-only payment gateway is voided in the control panel before capturing payment.");
        orderStatusDescriptionMap.put("Declined", "Seller has marked the order as declined.");
        orderStatusDescriptionMap.put("Refunded", "Seller has used the Refund action to refund the whole order. A listing of all orders with a \'Refunded\' status can be found under the More tab of the View Orders screen.");
        orderStatusDescriptionMap.put("Disputed", "Customer has initiated a dispute resolution process for the PayPal transaction that paid for the order or the seller has marked the order as a fraudulent order.");
        orderStatusDescriptionMap.put("Manual Verification Required", "Order on hold while some aspect, such as tax-exempt documentation, is manually confirmed. Orders with this status must be updated manually. Capturing funds or other order actions will not automatically update the status of an order marked Manual Verification Required.");
        orderStatusDescriptionMap.put("Partially Refunded", "Seller has partially refunded the order.");
    }

}
