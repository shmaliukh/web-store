package com.vshmaliukh.webstore;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ItemStatus {

    IN_STOCK(new Status("In stock",
            "You’re currently accepting orders for this product and can fulfill the purchase request. You’re certain that the product will ship (or be in-transit to the customer) in a timely manner because it's available for sale. You can deliver the product to all of the locations that you support in your product data and account shipping settings.")),
    OUT_OF_STOCK(new Status("Out of stock",
            "You’re not currently accepting orders for this product, or the product is not available for purchase or needs to be backordered.")),
    PREORDER(new Status("Preorder",
            "You’re currently taking orders for this product, but it’s not yet been released for sale. You're required to provide the availability date [availability_date] attribute to indicate the day that the product becomes available for delivery.")),
    BACKORDER(new Status("Backorder",
            "The product is not available at the moment, but you’re accepting orders and it'll be shipped as soon as it becomes available again. You're required to provide the availability date [availability_date] attribute to indicate the day that the product becomes available for delivery."));

    @Getter
    final Status status;

    ItemStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status.getStatusName();
    }

    public static List<String> getStatusNameList() {
        return Arrays.stream(values())
                .map(ItemStatus::getStatus)
                .map(Status::getStatusName)
                .collect(Collectors.toList());
    }

}
