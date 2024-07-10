package com.btracsolutions.yesparking.utils


object ApiConstants {
    //REQUEST TYPES
    const val REQUEST_TYPE_SIGNUP = "signup"
    const val REQUEST_TYPE_LOGIN = "login"
    const val REQUEST_TYPE_LOGOUT = "logout"
    const val REQUEST_TYPE_OTP_VRIFY = "otp_verify"
    const val REQUEST_TYPE_OTP_RESEND = "otp_resend"
    const val REQUEST_TYPE_FORGOT_PASS_OTP = "forgot_pass_send_otp"
    const val REQUEST_TYPE_FORGOT_PASS_SUBMIT_OTP = "forgot_pass_submit_otp"
    const val REQUEST_TYPE_UPDATE_PASSWORD = "update_pass"
    const val REQUEST_TYPE_FILEUPLOAD = "file_upload"
    const val REQUEST_TYPE_NIDUPLOAD = "nid_upload"
    const val REQUEST_TYPE_PROFILEPIC_UPLOAD = "profilepic_upload"
    const val REQUEST_TYPE_UPDATE_USERINFO = "updateuserinfo"
    const val REQUEST_TYPE_UPDATE_DELETE_USER = "deleteuser"
    const val REQUEST_TYPE_ADDVEHICLE = "add_vehicle"
    const val REQUEST_TYPE_EDITVEHICLE = "edit_vehicle"
    const val REQUEST_TYPE_PARKING_SPAPCES = "parking_spaces"
    const val REQUEST_TYPE_PARKING_BOOK = "parking_book"
    const val REQUEST_TYPE_PARKING_CHECKIN = "checkin"
    const val REQUEST_TYPE_BOOKING_CANCEL = "cancel_booking"
    const val REQUEST_TYPE_PARKING_CHECKOUT = "checkout"
    const val REQUEST_TYPE_PARKING_EXTEND = "extend"
    const val REQUEST_TYPE_PAYMENT = "payment"
    const val REQUEST_TYPE_PAYMENT_CHEKC = "payment_check"
    const val REQUEST_TYPE_PROMO= "promo"
    const val REQUEST_TYPE_RATING= "rating"
    const val REQUEST_TYPE_DATA_SYNC= "sync"
    const val REQUEST_TYPE_HISTORY= "history"

    const val ERROR_MESSAGE_INTERNAL_SERVER_ERROR = "Internal server error"
    const val ERROR_MESSAGE_NO_INTERNET = "No internet"
    const val ACTIVITY_DATE_FORMAT_MMM_dd_yyyy = "MMM dd, yyyy"
    const val DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val ACTIVITY_DATE_FORMAT_HH_MM_MMM_DD_YYYY= "hh:mm a, MMM dd,yy"
    const val SERVER_DATE_FORMAT = "yyyy-MM-dd"
    const val SERVER_DATE_FORMAT_WITH_TIME = "yyyy-MM-dd HH:mm:ss"
    const val ONLY_TIME_HH_MM = "hh:mm a"

    //END POINTS
    //const val BASE_URL = "http://eidokan.com:3005/banani-club-member/api/v1/"
    var BASE_URL = "https://api.yesparkingbd.com"
    var DEV = "/dev"
    var PROD = "/prod"
    var API_VERSION="/api/v1/"
    var PAYMENT_CONFIRM_URL = ""
    var MAP_API_KEY_ANDROID = "AIzaSyD__QnBu74p6QQ9seocmlq_dn5LDZlmrxk"
    var VERSION_CODE_ANDROID = ""
    var FORCE_UPDATE = false
    const val LOGIN = "auth/login"
    const val LOGOUT = "auth/logout"
    const val SIGNUP = "signup/register/"
    const val OTP_VERIFY = "signup/submit-otp"
    const val OTP_RESEND = "signup/resend-otp"
    const val REFRESH_TOKEN = "tokens/refresh"
    const val FORGOT_PASS_SEND_OTP = "auth/forget-password/send-otp"
    const val FORGOT_PASS_SUBMIT_OTP = "auth/forget-password/submit-otp"
    const val FORGOT_PASS_RESEND_OTP = "auth/forget-password/resend-otp"
    const val FORGOT_PASS = "auth/forget-password"
    const val UPLOAD_FILE = "vehicles/files"
    const val ADD_VEHICLE = "vehicles"
    const val PARKING_SPACES = "parking-spaces"
    const val UPDATE_USER = "users/"
    const val BOOK_PARKING = "bookings"
    const val CHECK_IN_OUT = "bookings/"
    const val PAYMENTS = "payments"
    const val PROMO = "promo-codes/"
    const val DATASYNC = "data-sync/users/"




    const val TYPE_VEHICLEREGISTRATIONPHOTO = "vehicleRegistrationPhoto"
    const val TYPE_VEHICLEPHOTO = "vehiclePhoto"
    const val TYPE_ADDVEHICLE = "addvehicle"
    const val TYPE_NID = "addnid"
    const val TYPE_NID_PHOTO = "addnidphoto"
    const val TYPE_PROFILEPIC = "profilepic"



    const val PARKING_HISTORY = "Parking History"
    const val EDIT_PROFILE = "Edit Profile"
    const val PAYMENT_HISTORY = "Payment History"
    const val PAYMENT_METHOD = "Payment Method"
    const val SECURITY = "Security"
    const val HELPLINE = "Helpline"

    const val VEHICLE = "Vehicle"
    const val NID = "NID"
}