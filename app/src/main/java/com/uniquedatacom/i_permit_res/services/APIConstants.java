package com.uniquedatacom.i_permit_res.services;

public interface APIConstants
{
    String LANGUAGE_API_KEY = "AIzaSyA6czlmz3NjmKUGBJmDRyFuduf1D_aYYLg";
//    String LANGUAGE_API_KEY = "AIzaSyDsPUfpQpe7Rh4RgICT1Gii5Pr1-XScjVI";

    String BASE_URL = "http://54.198.58.188:9000/i_res/";

    String IMAGE_URL = "http://54.198.58.188/ires/";

    String SECURITYTYPE = "0";

    String EMPTYPE = "1";
    // Register
    String SECURITYSIGNUP = BASE_URL + "security_signup";

    String EMPLOYEESIGNUP = BASE_URL + "resident_signup";

    String SECURITYLOGIN = BASE_URL + "login";

    String EMPLOYEELOGIN = BASE_URL + "resident_login";

    String VISITORSIGNUP = BASE_URL + "visitor_signup";

    String VISITORDETAILSLIST = BASE_URL + "get_visitor_details";

    String COMPANYLIST = BASE_URL + "get_community_list";

    String SECURITYPROFILEDETAILS = BASE_URL + "get_security_profile_details";

    String EMPYOLEEPROFILEDETAILS = BASE_URL + "get_resident_profile_details";

    String SECFORGOTPASSWORD = BASE_URL + "security_forgot_password";

    String SECCHANGEPASSWORD = BASE_URL + "security_change_password";

    String EMPFORGOTPASSWORD = BASE_URL + "resident_forgot_password";

    String EMPCHANGEPASSWORD = BASE_URL + "resident_change_password";

    String SECPROFILESETTINGS = BASE_URL + "profile_settings";

    String EMPPROFILESETTINGS = BASE_URL + "resident_profile_settings";

    String SECNAMELIST = BASE_URL + "security_name_list";

    String EMPNAMELIST = BASE_URL + "resident_name_list";

    String EMPNOTIFICATIONS = BASE_URL + "get_resident_notifications";

    String SECNOTIFICATIONS = BASE_URL + "get_security_notifications";

    String VISITORPREBOOKING = BASE_URL + "visitor_pre_booking";

    String REQUESTSLIST = BASE_URL + "get_visitor_requests_list";

    String EMPLOGS = BASE_URL + "logs_for_resident";

    String SECLOGS = BASE_URL + "logs_for_security";

    String COMPANYIDVALIDATE = BASE_URL + "community_id_validate";

    String VISITORSTATUS = BASE_URL + "visitor_status";

    String GETSECURITYPREBOOKINGLIST = BASE_URL + "get_security_prebooking_list";

    String EMPPREAPPROVEDLIST = BASE_URL + "get_resident_prebooking_list";

    String EMPLOYEEACCEPTLIST = BASE_URL + "get_resident_accept_list";

    String SECACCEPTLIST = BASE_URL + "get_security_accept_list";

    String EMPLOYEEDENIEDLIST = BASE_URL + "get_resident_denied_list";

    String SECDENIEDLIST = BASE_URL + "get_security_denied_list";

    String SECSENDOTP = BASE_URL + "security_send_otp";

    String EMPSENDOTP = BASE_URL + "resident_send_otp";

    String SECVERIFYOTP = BASE_URL + "security_verify_otp";

    String EMPVERIFYOTP = BASE_URL + "resident_verify_otp";

    String EMPPROFILEDETAILS = BASE_URL + "get_resident_profile_settings";

    String ACTIVITYVISITOREMP = BASE_URL + "activity_screen_visitors_for_resident";

    String ACTIVITYVISITORSEC = BASE_URL + "activity_screen_visitors_for_security";

    String EXIT = BASE_URL + "outgoingtime";

    String LANGUAGES_LIST = BASE_URL + "language_list";

    String SEARCH = BASE_URL + "search";

    String VALIDATETIME = BASE_URL + "validate_time";

    String VERIFYVISITOROTP = BASE_URL + "verify_visitor_otp";

    String VERSION = BASE_URL + "version";

    String RESIDENTLIST = BASE_URL+"resident_details_list";

}
