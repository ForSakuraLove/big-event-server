package com.pactera.bigevent.common.entity.constants;

public class ResponseCode {
    public static final String SUCCESS                            = "200";
    public static final String INVALID_AUTHORIZATION 	          = "403_1"; // "Invalid Authorization"
    public static final String TOKEN_INVALID 			          = "403_2"; // "The access token invalid, please check it again!"
    public static final String USER_NOT_EXIST 			          = "403_3"; // "The user does not exist!"
    public static final String ROLE_CHANGE 				          = "403_4"; // "Your account has been changed roles, please login again"
    public static final String USER_DISABLE 			          = "403_5"; // "Your account has been disabled, Please contact to admin"

    public static final String REDIRECT_ERROR 			          = "400_1"; // "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication"
    public static final String SHIPPING_CONDITION_ERROR           = "400_2"; // "ShippingCondition must be more than or equal to 1 and lower than or equal to 3
    public static final String DOWNLOAD_TIMEOUT                   = "400_3"; // "Timeout while file download, please try again"
    public static final String DOWNLOAD_BUSY                      = "400_4"; // "Server is busy, please try again"

    public static final String METHOD_NOT_ALLOWED                 = "405";
    public static final String BIZ_ERROR                          = "203";
    public static final String INTERNAL_SERVER_ERROR              = "500";

    public static final String PRODUCT_MASTER_EXIST               = "202_21"; // Product already exists

    public static final String FILE_NOT_EXIST                    = "202_37"; // Not found file

    public static final String EQUIPMENT_CHECK_ERROE               ="206";

    public static final String TOKEN_INVALID_CHECK 			       = "406"; // "The access token invalid, please check it again!"

    public static String showMessage(Class<?> classz, Object id) {
        String className = classz.getSimpleName();
        return String.format("Can't find %s with id = %s", className, id);
    }
}
