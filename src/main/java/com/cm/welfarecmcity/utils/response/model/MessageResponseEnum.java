package com.cm.welfarecmcity.utils.response.model;

public enum MessageResponseEnum {
    FETCH_DATA_SUCCESS("Fetch data successfully."),

    OPERATOR_DATA_SUCCESS("Operator successfully."),

    INSERT_DATA_SUCCESS("Data has been added to the system successfully."),
    INSERT_DATA_FAIL("Data has been added to the system unsuccessfully."),
    UPDATE_DATA_SUCCESS("Data has been updated to the system successfully."),
    DELETE_DATA_SUCCESS("Data has been deleted to the system successfully."),
    UPDATE_AND_FETCH_DATA_SUCCESS("Update and fetch data successfully."),
    CHECKOUT_ORDER_FAIL("Data invalid."),
    RESULT_FAILURE("FAILURE"),
    RESULT_SUCCESS("SUCCESS");

    private final String text;

    MessageResponseEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
