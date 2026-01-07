package com.epicmed.developer.assessment.util.constant;

import org.springframework.data.domain.Sort;

public class GeneralConstant {

    private GeneralConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String APPLICATION = "APPLICATION";
    public static final String DOUBLE_DASH = "--";
    public static final String EMPTY_STRING = "";
    public static final String SPACE_STRING = " ";
    public static final String SEMICOLON_SEPARATOR = ";";

    public static final String X_TRACE_ID = "X-TRACE-ID";

    public static final String VAR_CACHE_USER_DUMMY = "userDummyData";
    public static final String KEY_DUMMY_DTO = "DUMMY_DTO";

    public static final String RESP_CODE_SUCCESS = "00";
    public static final String RESP_MESSAGE_SUCCESS = "Success";
    public static final String RESP_CODE_DO_NOT_HONOR = "05";
    public static final String RESP_MESSAGE_DO_NOT_HONOR = "Do not honor";
    public static final String RESP_CODE_SYSTEM_MALFUNCTION = "96";
    public static final String RESP_MESSAGE_SYSTEM_MALFUNCTION = "System malfunction";

    public static final Integer NOT_EXIST = 0;
    public static final Integer AUTH_INDEX_OF_ACCESS_TOKEN = 1;

    public static String getSortSingleSort(Sort sort){
        if (sort.isSorted()) {
            var order = sort.get().findFirst();
            if (order.isPresent()) {
                return order.get().getProperty() + "," + order.get().getDirection().name();
            }
        }
        return null;
    }
}
