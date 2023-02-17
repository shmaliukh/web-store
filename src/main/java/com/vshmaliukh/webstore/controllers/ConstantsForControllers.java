package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.OrderStatus;

public final class ConstantsForControllers {


    private ConstantsForControllers() {
    }

    public static final String ORDER_PAGE = "order";
    public static final String OAUTH_LOGIN_PAGE = "oauth-login";
    public static final String HOME_PAGE = "home";
    public static final String LOGIN_PAGE = "login";
    public static final String MAIN_PAGE = "main";

    public static String ORDER_STATUS_COMPLETED_STR = OrderStatus.COMPLETED.getStatus().getStatusName();

}
