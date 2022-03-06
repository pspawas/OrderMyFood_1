package com.omf.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorConstant {

    public static final String REST90 = "REST90";
    public static final String REST100 = "REST100";

    public static final String REST101 = "REST101";
    public static final String REST102 = "REST102";
    public static final String REST103 = "REST103";
    public static final String REST104 = "REST104";
    public static final String ORDER90 = "ORDER90";
    public static final String ORDER92 = "ORDER92";
    public static final String ORDER105 = "ORDER105";
    public static final String ORDER106 = "ORDER106";
    public static final String ORDER107 = "ORDER107";
    public static final String CUSTOMER91 = "CUSTOMER91";
    public static final String ORDER91 = "ORDER91";
    public static final String ORDER93 = "ORDER93";
    public static final String ITEM91 = "ITEM91";
    public static final String MENU90 = "MENU90";
    public static final String MENU100 = "MENU100";
    public static final String MENU101 = "MENU101";
    public static final String MENU102 = "MENU102";
    public static final String ORDER115 = "ORDER115";
    public static final String ORDER112 = "ORDER112";
    public static final String ORDER108 = "ORDER108";
    public static final String ORDER109 = "ORDER109";
    public static final String ORDER116 = "ORDER116";
    public static final String ORDER117 = "ORDER117";
    static final Map<String, String> REST_ERRORS = new HashMap<>();
    private static final String ORDER110 = "ORDER110";
    private static final String ORDER111 = "ORDER111";
    private static final String ORDER113 = "ORDER113";
    private static final String ORDER114 = "ORDER114";
    private static final String ORDER100 = "ORDER100";
    private static final String ORDER101 = "ORDER101";
    private static final String ORDER102 = "ORDER102";
    private static final String ORDER103 = "ORDER103";
    private static final String ORDER104 = "ORDER104";

    static {
        REST_ERRORS.put(ORDER90, "Menu item with customer already exist.");
        REST_ERRORS.put(ORDER91, "Restaurant not found.");
        REST_ERRORS.put(ORDER92, "Customer/some orders not found.");
        REST_ERRORS.put(ORDER93, "Orders not found.");
        REST_ERRORS.put(ORDER100, "Invalid restaurant name given.");
        REST_ERRORS.put(ORDER101, "Invalid restaurant location given.");
        REST_ERRORS.put(ORDER102, "Restaurant restaurant distance given.");
        REST_ERRORS.put(ORDER103, "Restaurant restaurant cuisine given.");
        REST_ERRORS.put(ORDER104, "Restaurant restaurant budget given.");
        REST_ERRORS.put(ORDER105, "Menu item's change not found.");
        REST_ERRORS.put(ORDER106, "Orders already canceled.");
        REST_ERRORS.put(ORDER107, "Some orders canceled and some not found.");

        REST_ERRORS.put(ORDER108, "Invalid Customer cell number given.");
        REST_ERRORS.put(ORDER109, "Invalid Customer name given.");
        REST_ERRORS.put(ORDER110, "Invalid Payment Id.");
        REST_ERRORS.put(ORDER111, "Invalid Delivery time given.");
        REST_ERRORS.put(ORDER112, "Invalid Special note given.");
        REST_ERRORS.put(ORDER113, "Invalid Order time given.");
        REST_ERRORS.put(ORDER114, "Invalid Total price given.");
        REST_ERRORS.put(ORDER115, "Invalid Item Detail given.");

        REST_ERRORS.put(ORDER116, "Invalid Order ID given.");
        REST_ERRORS.put(ORDER117, "Invalid Order ID size given.");

        REST_ERRORS.put(MENU90, "Menu items already exist for given restaurant ID.");
        REST_ERRORS.put(MENU100, "Invalid menu name given.");
        REST_ERRORS.put(MENU101, "Invalid menu description given.");
        REST_ERRORS.put(MENU102, "Invalid menu price given.");

        REST_ERRORS.put(REST90, "Restaurant name already exist.");
        REST_ERRORS.put(REST100, "Invalid restaurant name given.");
        REST_ERRORS.put(REST101, "Invalid restaurant location given.");
        REST_ERRORS.put(REST102, "Restaurant restaurant distance given.");
        REST_ERRORS.put(REST103, "Restaurant restaurant cuisine given.");
        REST_ERRORS.put(REST104, "Restaurant restaurant budget given.");

        REST_ERRORS.put(CUSTOMER91, "Customer not found.");
        REST_ERRORS.put(ITEM91, "Customer/Item not found.");
    }

    static List getError(String errorCode) {
        return List.of(OrderMyFoodError.build(errorCode));
    }
}
