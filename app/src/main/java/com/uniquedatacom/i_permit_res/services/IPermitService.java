package com.uniquedatacom.i_permit_res.services;

import com.uniquedatacom.i_permit_res.models.*;
import com.google.gson.JsonObject;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface IPermitService
{
    // SECURITYSIGNUP API
    @POST(APIConstants.SECURITYSIGNUP)
    Observable<SecuritySignUpResponseModel> SecuritySignUpResponse(@Body JsonObject data);

    // EMPLOYEESIGNUP API
    @POST(APIConstants.EMPLOYEESIGNUP)
    Observable<EmployeeSignUpResponseModel> EmployeeSignUpResponse(@Body JsonObject data);


    // SECURITYLOGIN API
    @POST(APIConstants.SECURITYLOGIN)
    Observable<SecurityLoginResponseModel> SecurityLoginResponse(@Body JsonObject data);

    // SECURITYLOGIN API
    @POST(APIConstants.EMPLOYEELOGIN)
    Observable<EmployeeLoginResponseModel> EmployeeLoginResponse(@Body JsonObject data);

    // VISITORSIGNUP API
    @POST(APIConstants.VISITORSIGNUP)
    Observable<VisitorSignUpResponseModel> VisitorSignUpResponse(@Body JsonObject data);

    // VISITORSIGNUP API
    @POST(APIConstants.VISITORDETAILSLIST)
    Observable<VisitorDetailsListResponseModel> VisitorsListResponse(@Body JsonObject data);

    // SECURITYLOGIN API
    @POST(APIConstants.SECURITYPROFILEDETAILS)
    Observable<SecurityProfileResponseModel> SecurityProfileDetails(@Body JsonObject data);

    // SECURITYLOGIN API
    @POST(APIConstants.EMPYOLEEPROFILEDETAILS)
    Observable<EmpProfileResponseModel> EmpProfileDetails(@Body JsonObject data);

    // EMPFORGOTPASSWORD API
    @POST(APIConstants.EMPFORGOTPASSWORD)
    Observable<ForgotPasswordResponseModel> EmpForgotPassword(@Body JsonObject data);

    // SECFORGOTPASSWORD API
    @POST(APIConstants.SECFORGOTPASSWORD)
    Observable<ForgotPasswordResponseModel> SecForgotPassword(@Body JsonObject data);


    // EMPCHANGEPASSWORD API
    @POST(APIConstants.EMPCHANGEPASSWORD)
    Observable<ChangePasswordResponseModel> EmpChangePassword(@Body JsonObject data);

    // SECCHANGEPASSWORD API
    @POST(APIConstants.SECCHANGEPASSWORD)
    Observable<ChangePasswordResponseModel> SecChangePassword(@Body JsonObject data);

    // EMPPROFILESETTINGS API
    @POST(APIConstants.EMPPROFILESETTINGS)
    Observable<EmpProfileSettingsResponseModel> EmpProfileSettings(@Body JsonObject data);

    // SECNAMELIST API
    @POST(APIConstants.SECNAMELIST)
    Observable<GetSecNameListResponseModel> SecNameList(@Body JsonObject data);

    // EMPNAMELIST API
    @POST(APIConstants.EMPNAMELIST)
    Observable<GetEmpNameListResponseModel> EmpNameList(@Body JsonObject data);

    // EMPNAMELIST API
    @POST(APIConstants.EMPNOTIFICATIONS)
    Observable<EmpNotificationsResponseModel> EmpNotifications(@Body JsonObject data);

    // EMPNAMELIST API
    @POST(APIConstants.SECNOTIFICATIONS)
    Observable<SecNotificationsResponseModel> SecNotificatiosn(@Body JsonObject data);

    // EMPNAMELIST API
    @POST(APIConstants.VISITORPREBOOKING)
    Observable<VisitorPreBookingResponseModel> VisitorPreBooking(@Body JsonObject data);

    // EMPNAMELIST API
    @POST(APIConstants.REQUESTSLIST)
    Observable<VisitorsRequestsListResponseModel> ReQuestsList(@Body JsonObject data);

    // EMPLOGS API
    @POST(APIConstants.EMPLOGS)
    Observable<EmpLogsResponseModel> EmpLogs(@Body JsonObject data);

    // SECLOGS API
    @POST(APIConstants.SECLOGS)
    Observable<SecLogsResponseModel> SecLogs(@Body JsonObject data);

    // COMPANYIDVALIDATE API
    @POST(APIConstants.COMPANYIDVALIDATE)
    Observable<CompanyIDValidateResponseModel> CompanyIdValidate(@Body JsonObject data);

    // VERSION API
    @POST(APIConstants.VERSION)
    Observable<VersionResponseModel> VersionUpdate(@Body JsonObject data);


    // approved-0,pre_approved-1,denied-2
    // COMPANYIDVALIDATE API
    @POST(APIConstants.VISITORSTATUS)
    Observable<VisitorStatusResponseModel> VisitorStatus(@Body JsonObject data);

    // GETSECURITYPREBOOKINGLIST API
    @POST(APIConstants.GETSECURITYPREBOOKINGLIST)
    Observable<GetSecurityPreApprovedListResponseModel> GetSecurityPreApprovedList(@Body JsonObject data);

    // EMPLOYEEACCEPTLIST API
    @POST(APIConstants.EMPPREAPPROVEDLIST)
    Observable<EmpPreApprovedResponseModel> EmpPreApprovedList(@Body JsonObject data);

    // ACTIVITYVISITOREMP API
    @POST(APIConstants.ACTIVITYVISITOREMP)
    Observable<ActivityVisitorsEmpResponseModel> ActivityVisitorsEmp(@Body JsonObject data);

    // ACTIVITYVISITORSEC API
    @POST(APIConstants.ACTIVITYVISITORSEC)
    Observable<ActivityVisitorsSecResponseModel> ActivityVisitorsSec(@Body JsonObject data);


    // EMPLOYEEACCEPTLIST API
    @POST(APIConstants.EMPLOYEEACCEPTLIST)
    Observable<EmpAcceptResponseModel> EmpAcceptList(@Body JsonObject data);



    // EMPLOYEEACCEPTLIST API
    @POST(APIConstants.SECACCEPTLIST)
    Observable<SecAcceptResponseModel> SecAcceptList(@Body JsonObject data);


    // EMPLOYEEDENIEDLIST API
    @POST(APIConstants.EMPLOYEEDENIEDLIST)
    Observable<EmpDeniedListResponseModel> EmpDeniedList(@Body JsonObject data);


    // EMPLOYEEDENIEDLIST API
    @POST(APIConstants.SECDENIEDLIST)
    Observable<EmpDeniedListResponseModel> SecDeniedList(@Body JsonObject data);


    // EMPLOYEEDENIEDLIST API
    @POST(APIConstants.SECSENDOTP)
    Observable<SecSendOTPResponseModel> SecSendOTP(@Body JsonObject data);


    // EMPLOYEEDENIEDLIST API
    @POST(APIConstants.EMPSENDOTP)
    Observable<EmpSendOTPResponseModel> EmpSendOTP(@Body JsonObject data);

    // EMPLOYEEDENIEDLIST API
    @POST(APIConstants.EMPPROFILEDETAILS)
    Observable<GetEmpProfileDetailsResponseModel> GetEmpProfileDetails(@Body JsonObject data);

    // SECVERIFYOTP API
    @POST(APIConstants.SECVERIFYOTP)
    Observable<SecVerifyOTPResponseModel> SecVerifyOTPResponse(@Body JsonObject data);

    // EMPLOYEEDENIEDLIST API
    @POST(APIConstants.EMPVERIFYOTP)
    Observable<EmpVerifyOTPResponseModel> EmpVerifyOTPResponse(@Body JsonObject data);

    // EXIT API
    @POST(APIConstants.EXIT)
    Observable<ExitResponseModel> ExitResponse(@Body JsonObject data);

    // SearchResponseModel API
    @POST(APIConstants.SEARCH)
    Observable<SearchResponseModel> SearchResponse(@Body JsonObject data);


    // ValidateTimeResponse API
    @POST(APIConstants.VALIDATETIME)
    Observable<ValidateTimeResponseModel> ValidateTimeResponse(@Body JsonObject data);


    // EXIT API
    @POST(APIConstants.VERIFYVISITOROTP)
    Observable<VerifyVisitorOTPResponseModel> VerifyVisitorOTPResponse(@Body JsonObject data);


    @GET
    Observable<CompanyListResponseModel> CompanyList(@Url String url);

    @GET
    Observable<LanguagesListResponseModel> LanguagesList(@Url String url);

    @GET
    Observable<ResidentDetailsResponseModel> ResidentDetailsList(@Url String url);


}
