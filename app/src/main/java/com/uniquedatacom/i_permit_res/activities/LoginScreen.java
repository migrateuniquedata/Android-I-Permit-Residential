package com.uniquedatacom.i_permit_res.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.EmpSendOTPResponseModel;
import com.uniquedatacom.i_permit_res.models.EmployeeLoginRequestModel;
import com.uniquedatacom.i_permit_res.models.EmployeeLoginResponseModel;
import com.uniquedatacom.i_permit_res.models.SecSendOTPResponseModel;
import com.uniquedatacom.i_permit_res.models.SecurityLoginRequestModel;
import com.uniquedatacom.i_permit_res.models.SecurityLoginResponseModel;
import com.uniquedatacom.i_permit_res.models.SendOTPRequestModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginScreen extends Activity {
    private Button loginBtn,backBtn;
    private TextInputEditText EmailEt,passwordEt;
    private ImageView pwdToggleIv;
    private ProgressDialog mProgressDialog;
    private Subscription mSubscription;
    private TextView titleTv,forgotPasswordTv;
    private ImageView notificationIV;
    private String TAG = "LoginScreen";
    private String UserType;
    private TextInputLayout emailEtLayout,passwordEtLayout;
    String LangCode;
    private String compID,companyName;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        UserType = getIntent().getStringExtra("UserType");
        Log.i(TAG, "onCreate: "+UserType);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language +"-->"+ LangCode);
        SetViewsFromLayout();
        TextViewTranslation(LangCode,forgotPasswordTv);
        TextViewTranslation(LangCode,titleTv);
        ETTranslation(LangCode,EmailEt);
        ETTranslation(LangCode,passwordEt);
        InputLayoutTranslation(LangCode,emailEtLayout);
        InputLayoutTranslation(LangCode,passwordEtLayout);
        ETTranslation(LangCode,passwordEt);
        ButtonTranslation(LangCode,loginBtn);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SetViewsFromLayout()
    {
        titleTv = findViewById(R.id.titleTv);
        if(UserType.equals("Security"))
        {
            titleTv.setText("Security Login");
        }else {
            titleTv.setText("Resident Login");
        }
        notificationIV = findViewById(R.id.notificationIV);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        emailEtLayout = findViewById(R.id.emailEtLayout);
        passwordEtLayout = findViewById(R.id.passwordEtLayout);
        EmailEt = findViewById(R.id.EmailEt);
        if(UserType.equals("Security"))
        {
            emailEtLayout.setHint("User ID");
        }else {
            emailEtLayout.setHint("Mobile Number");
        }
        passwordEt = findViewById(R.id.passwordEt);
        forgotPasswordTv = findViewById(R.id.forgotPasswordTv);
        forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginScreen.this,ForgotPasswordScreen.class);
                i.putExtra("UserType",UserType);
                startActivity(i);
            }
        });
        pwdToggleIv = findViewById(R.id.pwdToggleIv);
        pwdToggleIv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ShowHidePass(v,pwdToggleIv,passwordEt);
                return false;
            }
        });
        LoginOnClick();
    }



    public void ShowHidePass(View view, ImageView pwdToggleIv,EditText passwordEt){

        if(view.getId()== pwdToggleIv.getId()){

            if(passwordEt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                pwdToggleIv.setImageResource(R.drawable.password_show);

                //Show Password
                passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                passwordEt.setSelection(passwordEt.getText().toString().length());
            }
            else{
                pwdToggleIv.setImageResource(R.drawable.password_hide);

                //Hide Password
                passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passwordEt.setSelection(passwordEt.getText().toString().length());

            }
        }
    }
    private void LoginOnClick()
    {
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginValidation())
                {
                    showProgressDialog();
                    if(UserType.equals("Security"))
                    {
                        CallSecurityLoginApi();
                    }else {
                        CallEmpLoginApi();
                    }
                }
            }
        });
    }

    // to intialize the Progress Dialog
    private void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    // to start the Progress Dialog
    public void showProgressDialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null)
                        initProgressDialog();
                    if (!mProgressDialog.isShowing())
                        mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // to hide the Progress Dialog
    public void hideProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mProgressDialog = null;
                }
            }
        });
    }


    private boolean LoginValidation()
    {
        if (TextUtils.isEmpty(EmailEt.getText().toString())) {
            EmailEt.setError(getString(R.string.PleaseEnterValidNoOrEmail));
            EmailEt.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(passwordEt.getText().toString())) {
            passwordEt.setError(getString(R.string.PleaseEnterPassword));
            passwordEt.requestFocus();
            return false;
        }
//        else if(isValidMail(usernameEt.getText().toString())  || isValidMobile(passwordEt.getText().toString()))
//        {
//            usernameEt.setError(getString(R.string.PleaseEnterValidNoOrEmail));
//            usernameEt.requestFocus();
//            return false;
//        }
        return true;
    }


    /**
     * Call Login API
     */
    private void CallSecurityLoginApi()
    {
        JsonObject object = SecurityLoginPageObject();
        IPermitService service = ServiceFactory.createRetrofitService(this, IPermitService.class);
        mSubscription = service.SecurityLoginResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecurityLoginResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SecurityLoginResponseModel mRespone) {
                        hideProgressDialog();
//                        Toast.makeText(LoginScreen.this, "" + mRespone.getMessage(), Toast.LENGTH_SHORT).show();

                        if(mRespone.getStatus() == 1)
                        {
                            Log.i(TAG, "onNext: "+mRespone.getResult().get(0).getSecId());
//                            if(mRespone.getResult().get(0).getVerifyStatus() == false)
//                            {
//                                EnterOTPDialog();
//                            }
//                            else{
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("mobile_number", mRespone.getResult().get(0).getMobileNumber()); // Storing string
                                editor.putString("name", mRespone.getResult().get(0).getName());
                                editor.putString("email_id", mRespone.getResult().get(0).getEmailId());
                                editor.putString("user_pic", mRespone.getResult().get(0).getUserPic());
                                editor.putInt("user_id", mRespone.getResult().get(0).getSecId());
                                editor.apply();
                                editor.commit();
                                CustomDialogWithOneBtn(LoginScreen.this,getString(R.string.Success),"Login Successful","Ok", new Runnable() {
                                    @Override
                                    public void run() {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                SharedPreferences pref = getApplicationContext().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
                                                SharedPreferences.Editor editor = pref.edit();
                                                editor.putString("UserType", "Security");
                                                editor.apply();
                                                editor.commit();

                                                Intent i = new Intent(LoginScreen.this,SecurityNavActivity.class);
                                                i.putExtra("user_id",mRespone.getResult().get(0).getSecId());
                                                i.putExtra("UserType","Security");
                                                startActivity(i);
                                                finish();
                                            }
                                        },1000);
                                    }
                                });
//                            }

                        }
                        else {
                            CustomDialogWithOneBtn(LoginScreen.this,getString(R.string.Failure),mRespone.getMessage(),"Ok", new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }

                    }
                });
    }


    /**
     * Json object of loginPageObject
     *
     * @return
     */
    private JsonObject SecurityLoginPageObject() {
        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("CompanyDetails", Context.MODE_PRIVATE);
        compID = sharedpreferences.getString("CompanyID","N/A");
        companyName = sharedpreferences.getString("CompanyName","N/A");
        Log.i(TAG, "SetViews: "+compID+"-->"+companyName);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        String token = pref.getString("Dev_Token","N/A");
        SecurityLoginRequestModel requestModel = new SecurityLoginRequestModel();
        String userName = EmailEt.getText().toString().replaceAll("\\s+$", "");
        requestModel.setDevice_token(token);
        requestModel.setUsername(userName);
        requestModel.setPassword(passwordEt.getText().toString());
        requestModel.setLanguage("English");
        requestModel.setLanguage_type("EN");
        requestModel.setType(APIConstants.SECURITYTYPE);
        requestModel.setCompany_id(compID);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



    /**
     * CallEmpLoginApi
     */
    private void CallEmpLoginApi()
    {
        JsonObject object = EmpLoginPageObject();
        IPermitService service = ServiceFactory.createRetrofitService(this, IPermitService.class);
        mSubscription = service.EmployeeLoginResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmployeeLoginResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(EmployeeLoginResponseModel mRespone) {
                        hideProgressDialog();
//                        Toast.makeText(LoginScreen.this, "" + mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            Log.i(TAG, "onNext: EID"+mRespone.getResult().get(0).getEId());
                            if(mRespone.getResult().get(0).getVerifyStatus() == false)
                            {
                                EnterOTPDialog();
                            }
                            else{
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("mobile_number", mRespone.getResult().get(0).getMobileNumber()); // Storing string
                                editor.putString("name", mRespone.getResult().get(0).getName());
                                editor.putString("email_id", mRespone.getResult().get(0).getEmailId());
                                editor.putString("user_pic", mRespone.getResult().get(0).getUserPic());
                                editor.putInt("user_id", mRespone.getResult().get(0).getEId());
                                editor.apply();
                                editor.commit();
                                CustomDialogWithOneBtn(LoginScreen.this,getString(R.string.Success),"Login Successful","Ok", new Runnable() {
                                @Override
                                public void run() {

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("UserType", "Employee");
                                            editor.apply();
                                            editor.commit();
                                            Intent i = new Intent(LoginScreen.this,SecurityNavActivity.class);
                                            i.putExtra("user_id",mRespone.getResult().get(0).getEId());
                                            i.putExtra("UserType","Employee");
                                            startActivity(i);
                                            finish();
                                        }
                                    },1000);

                                }
                            });

                        }

                        }
                        else {
                            CustomDialogWithOneBtn(LoginScreen.this,getString(R.string.Failure),mRespone.getMessage(),"Ok", new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }

                    }
                });
    }


    /**
     * Json object of EmpLoginPageObject
     *
     * @return
     */
    private JsonObject EmpLoginPageObject() {
        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("CompanyDetails", Context.MODE_PRIVATE);
        compID = sharedpreferences.getString("CompanyID","N/A");
        companyName = sharedpreferences.getString("CompanyName","N/A");
        Log.i(TAG, "SetViews: "+compID+"-->"+companyName);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        String token = pref.getString("Dev_Token","N/A");
        EmployeeLoginRequestModel requestModel = new EmployeeLoginRequestModel();
        requestModel.setDevice_token(token);
        String userName = EmailEt.getText().toString().replaceAll("\\s+$", "");
        requestModel.setUsername(userName);
        requestModel.setPassword(passwordEt.getText().toString());
        requestModel.setLanguage("English");
        requestModel.setLanguageType("EN");
        requestModel.setType(APIConstants.EMPTYPE);
        requestModel.setCompany_id(compID);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }




    private void EnterOTPDialog()
    {
        Dialog OTPDialog = new Dialog(this);
        OTPDialog.setContentView(R.layout.enter_otp_layout);
        OTPDialog.setCanceledOnTouchOutside(true);
        OTPDialog.show();
        EditText enterOtpEt = (EditText) OTPDialog.findViewById(R.id.enterOtpEt);
        enterOtpEt.setHint("Mobile Number");
        Button button = (Button) OTPDialog.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserType.equals("Security"))
                {
                    if(enterOtpEt.getText().toString().equals(""))
                    {
                        enterOtpEt.setError(getString(R.string.PleaseEnterMobileNumber));
                    }
                    else{
                        CallSecSendOTP(enterOtpEt.getText().toString());
                    }

                }else {
                    if(enterOtpEt.getText().toString().equals(""))
                    {
                        enterOtpEt.setError(getString(R.string.PleaseEnterMobileNumber));
                    }
                    else{
                        CallEmployeeSendOTP(enterOtpEt.getText().toString());
                    }

                }
            }
        });

    }



    /**************************START OF CustomDialogWithOneBtn*********************************/
    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param buttonNam1
     * @param runnable
     */
    public void CustomDialogWithOneBtn(Context context, String Title, String Msg, String buttonNam1, Runnable runnable)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogwithonebtn);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TitleTv);
        title.setText(Title);
        TextView msg = (TextView) dialog.findViewById(R.id.DescTv);
        msg.setText(Msg);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        okBtn.setText(buttonNam1);
        TextViewTranslation(LangCode,title);
        TextViewTranslation(LangCode,msg);
        ButtonTranslation(LangCode,okBtn);
        // if decline button is clicked, close the custom dialog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                runnable.run();
            }
        });

    }
    /**************************END OF CustomDialogWithOneBtn*********************************/



    private void ButtonTranslation(String languageCode, Button button)
    {
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(APIConstants.LANGUAGE_API_KEY)
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(button.getText().toString(),
                                Translate.TranslateOption.targetLanguage(languageCode));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (button != null) {
                            button.setText(translation.getTranslatedText());
                        }

                    }
                });
                return null;
            }
        }.execute();
    }

    private void ETTranslation(String languageCode, EditText editText)
    {
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(APIConstants.LANGUAGE_API_KEY)
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(editText.getText().toString(),
                                Translate.TranslateOption.targetLanguage(languageCode));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (editText != null) {
                            editText.setText(translation.getTranslatedText());
                        }

                    }
                });
                return null;
            }
        }.execute();
    }

    private void InputLayoutTranslation(String languageCode, TextInputLayout textInputLayout)
    {
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(APIConstants.LANGUAGE_API_KEY)
                        .build();
                Translate translate = options.getService();
                Log.i(TAG, "doInBackground: "+textInputLayout.getEditText().getText().toString().trim());
                final Translation translation =
                        translate.translate(textInputLayout.getHint().toString(),
                                Translate.TranslateOption.targetLanguage(languageCode));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (textInputLayout != null) {
//                            textInputLayout.getEditText().setHint(translation.getTranslatedText());
                            textInputLayout.setHint(translation.getTranslatedText());
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                textInputLayout.setTooltipText(translation.getTranslatedText());
//                            }
                        }

                    }
                });
                return null;
            }
        }.execute();
    }


    private void TextViewTranslation(String languageCode, TextView button)
    {
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(APIConstants.LANGUAGE_API_KEY)
                        .build();
                Translate translate = options.getService();

                final Translation translation =
                        translate.translate(button.getText().toString(),
                                Translate.TranslateOption.targetLanguage(languageCode));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (button != null) {
                            button.setText(translation.getTranslatedText());
                        }

                    }
                });
                return null;
            }
        }.execute();
    }



    /**
     * CallSecSendOTP
     */
    private void CallSecSendOTP(String mobile)
    {
        JsonObject object = SendOTPObject(mobile);
        IPermitService service = ServiceFactory.createRetrofitService(LoginScreen.this, IPermitService.class);
        mSubscription = service.SecSendOTP(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecSendOTPResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Toast.makeText(LoginScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SecSendOTPResponseModel mRespone)
                    {
//                        Toast.makeText(LoginScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
//                            Intent i = new Intent(RegisterScreen.this,LoginScreen.class);
                            Intent i = new Intent(LoginScreen.this,OTPActivity.class);
                            i.putExtra("UserType",UserType);
                            i.putExtra("FromActivity","RegisterScreen");
                            i.putExtra("mobileNumber",mRespone.getResult().get(0).getMobileNumber());
                            startActivity(i);
                        }
                        else {

                        }
                    }
                });
    }


    /**
     * Json object of EmployeeSendOTPObject
     * @return
     * @param mobile
     */
    private JsonObject SendOTPObject(String mobile)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        String token = pref.getString("Dev_Token","N/A");
        SendOTPRequestModel requestModel = new SendOTPRequestModel();
        requestModel.setMobileNumber(mobile);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /**
     * CallEmployeeSendOTP
     */
    private void CallEmployeeSendOTP(String mobile)
    {
        JsonObject object = SendOTPObject(mobile);
        IPermitService service = ServiceFactory.createRetrofitService(LoginScreen.this, IPermitService.class);
        mSubscription = service.EmpSendOTP(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpSendOTPResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Toast.makeText(LoginScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(EmpSendOTPResponseModel mRespone)
                    {
                        Toast.makeText(LoginScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            Intent i = new Intent(LoginScreen.this,OTPActivity.class);
                            i.putExtra("UserType",UserType);
                            i.putExtra("FromActivity","RegisterScreen");
                            i.putExtra("mobileNumber",mRespone.getResult().get(0).getMobileNumber());
                            startActivity(i);
                        }
                        else {

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent i = new Intent(LoginScreen.this,WelcomeScreen.class);
        startActivity(i);
        finish();
    }
}
