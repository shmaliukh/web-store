package com.vshmaliukh.webstore;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum OrderStatus {

    PENDING(new Status("Pending", "Customer started the checkout process but did not complete it. Incomplete orders are assigned a 'Pending' status and can be found under the More tab in the View Orders screen.")),
    AWAITING_PAYMENT(new Status("Awaiting Payment", "Customer has completed the checkout process, but payment has yet to be confirmed. Authorize only transactions that are not yet captured have this status.")),
    AWAITING_FULFILLMENT(new Status("Awaiting Fulfillment", "Customer has completed the checkout process and payment has been confirmed.")),
    AWAITING_SHIPMENT(new Status("Awaiting Shipment", "Order has been pulled and packaged and is awaiting collection from a shipping provider.")),
    AWAITING_PICKUP(new Status("Awaiting Pickup", "Order has been packaged and is awaiting customer pickup from a seller-specified location.")),
    PARTIALLY_SHIPPED(new Status("Partially Shipped", "Only some items in the order have been shipped.")),
    COMPLETED(new Status("Completed", "Order has been shipped/picked up, and receipt is confirmed; client has paid for their digital product, and their file(s) are available for download.")),
    SHIPPED(new Status("Shipped", "Order has been shipped, but receipt has not been confirmed; seller has used the Ship Items action. A listing of all orders with a 'Shipped' status can be found under the More tab of the View Orders screen.")),
    CANCELLED(new Status("Cancelled", "Seller has cancelled an order, due to a stock inconsistency or other reasons. Stock levels will automatically update depending on your Inventory Settings. Cancelling an order will not refund the order. This status is triggered automatically when an order using an authorize-only payment gateway is voided in the control panel before capturing payment.")),
    DECLINED(new Status("Declined", "Seller has marked the order as declined.")),
    REFUNDED(new Status("Refunded", "Seller has used the Refund action to refund the whole order. A listing of all orders with a 'Refunded' status can be found under the More tab of the View Orders screen.")),
    DISPUTED(new Status("Disputed", "Customer has initiated a dispute resolution process for the PayPal transaction that paid for the order or the seller has marked the order as a fraudulent order.")),
    MANUAL_VERIFICATION_REQUIRED(new Status("Manual Verification Required", "Order on hold while some aspect, such as tax-exempt documentation, is manually confirmed. Orders with this status must be updated manually. Capturing funds or other order actions will not automatically update the status of an order marked Manual Verification Required.")),
    PARTIALLY_REFUNDED(new Status("Partially Refunded", "Seller has partially refunded the order."));

    @Getter
    final Status status;

    OrderStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status.getStatusName();
    }

    public static Map<String, String> getStatusNameDescriptionMap() {
        return Arrays.stream(values())
                .map(OrderStatus::getStatus)
                .collect(Collectors.toMap(Status::getStatusName, Status::getDescription));
    }

}
