package com.uniquedatacom.i_permit_res.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.CompanyListResponseModel;
import com.uniquedatacom.i_permit_res.models.EmpSendOTPResponseModel;
import com.uniquedatacom.i_permit_res.models.EmployeeSignUpRequestModel;
import com.uniquedatacom.i_permit_res.models.EmployeeSignUpResponseModel;
import com.uniquedatacom.i_permit_res.models.SecSendOTPResponseModel;
import com.uniquedatacom.i_permit_res.models.SecuritySignUpRequestModel;
import com.uniquedatacom.i_permit_res.models.SecuritySignUpResponseModel;
import com.uniquedatacom.i_permit_res.models.SendOTPRequestModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterScreen extends Activity {
    private TextView titleTv,companyNameTv,cname;
    private ImageView notificationIV,pwdToggleIv,CpwdToggleIv;
    private Button backBtn,signupBtn,verifyBtn;
    private TextInputEditText usernameEt,emailEt,securityEt,empCodeEt,passwordEt,cpasswordEt,MobileEt,otpEt,blockEt;
    private Subscription mSubscription;
    private Spinner companySpinner;
    private String UserType = "";
    private String TAG = "RegisterScreen";
    private String compID,companyName;
    String Language = "English";
    private TextInputLayout userNameEtLayout,emailEtLayout,empCodeEtLayout,BlockEtLayout,passwordEtLayout,cpasswordEtLayout,MobileEtLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        UserType = getIntent().getStringExtra("UserType");
        Log.i(TAG, "onCreate: "+UserType);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        SetViews();
//        TextViewTranslation(LangCode,companyNameTv);
        TextViewTranslation(LangCode,titleTv);
        TextViewTranslation(LangCode,cname);
        InputLayoutTranslation(LangCode,userNameEtLayout);
        InputLayoutTranslation(LangCode,emailEtLayout);
        InputLayoutTranslation(LangCode,empCodeEtLayout);
        InputLayoutTranslation(LangCode,BlockEtLayout);
        InputLayoutTranslation(LangCode,passwordEtLayout);
        InputLayoutTranslation(LangCode,cpasswordEtLayout);
        InputLayoutTranslation(LangCode,MobileEtLayout);
        ETTranslation(LangCode,passwordEt);
        ButtonTranslation(LangCode,signupBtn);
//        GetCompanyList();
    }

       private void SetViews()
    {
        titleTv = findViewById(R.id.titleTv);
        if(UserType.equals("Security"))
        {
            titleTv.setText("Security Sign Up");
        }else {
            titleTv.setText("Resident Sign Up");
        }

        notificationIV = findViewById(R.id.notificationIV);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        companyNameTv = findViewById(R.id.companyNameTv);
        SharedPreferences sharedpreferences = getSharedPreferences("CompanyDetails", Context.MODE_PRIVATE);
        compID = sharedpreferences.getString("CompanyID","N/A");
        companyName = sharedpreferences.getString("CompanyName","N/A");
        Log.i(TAG, "SetViews: "+compID+"-->"+companyName);
        companyNameTv.setText(companyName);
        usernameEt = findViewById(R.id.usernameEt);
        companySpinner = findViewById(R.id.companySpinner);
        companySpinner.setVisibility(View.INVISIBLE);
        emailEt = findViewById(R.id.emailEt);
        securityEt = findViewById(R.id.securityEt);
        empCodeEt = findViewById(R.id.empCodeEt);
        blockEt = findViewById(R.id.blockEt);
        passwordEt = findViewById(R.id.passwordEt);
        cpasswordEt = findViewById(R.id.cpasswordEt);
        MobileEt = findViewById(R.id.MobileEt);
        otpEt = findViewById(R.id.otpEt);
        userNameEtLayout = findViewById(R.id.userNameEtLayout);
        emailEtLayout = findViewById(R.id.emailEtLayout);
        empCodeEtLayout = findViewById(R.id.empCodeEtLayout);
        BlockEtLayout = findViewById(R.id.BlockEtLayout);
        passwordEtLayout = findViewById(R.id.passwordEtLayout);
        cpasswordEtLayout = findViewById(R.id.cpasswordEtLayout);
        MobileEtLayout = findViewById(R.id.MobileEtLayout);
        pwdToggleIv = findViewById(R.id.pwdToggleIv);
        CpwdToggleIv = findViewById(R.id.CpwdToggleIv);
        cname =findViewById(R.id.cname);
        verifyBtn =findViewById(R.id.verifyBtn);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(RegisterScreen.this, "Your OTP is 1234", Toast.LENGTH_SHORT).show();
                if(MobileEt.getText().toString().equals(""))
                {
                    Toast.makeText(RegisterScreen.this, getString(R.string.PleaseEnterMobileNumber), Toast.LENGTH_SHORT).show();
                }
                else if (!isValidPhone() || MobileEt.getText().toString().startsWith("0")) {
                    MobileEt.setError(getString(R.string.PleaseEnterValidNo));
                    MobileEt.requestFocus();
                }
                else {
                    if(UserType.equals("Security"))
                    {
                        CallSecSendOTP();
                    }else {
                        CallEmployeeSendOTP();
                    }
                }

            }
        });
        signupBtn =findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterValidation();
            }
        });
        pwdToggleIv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ShowHidePass(v,pwdToggleIv,passwordEt);

                return false;
            }
        });
        CpwdToggleIv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ShowHidePass(v,CpwdToggleIv,cpasswordEt);

                return false;
            }
        });
    }




    public void ShowHidePass(View view, ImageView pwdToggleIv,EditText passwordEt){

        if(view.getId()== pwdToggleIv.getId()){

            if(passwordEt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                pwdToggleIv.setImageResource(R.drawable.password_show);

                //Show Password
                passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                pwdToggleIv.setImageResource(R.drawable.password_hide);

                //Hide Password
                passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
    /**
     * RegisterValidation
     * @return
     */
    private boolean RegisterValidation()
    {
        if (TextUtils.isEmpty(usernameEt.getText().toString())) {
            usernameEt.setError(getString(R.string.PleaseEnterUserName));
            usernameEt.requestFocus();
            return false;
        }
        else if (!isValidEmail(emailEt.getText().toString())) {
            emailEt.setError(getString(R.string.PleaseEnterEmailID));
            emailEt.requestFocus();
            return false;
        }
        else if (!isValidPhone() || MobileEt.getText().toString().startsWith("0")) {
            MobileEt.setError(getString(R.string.PleaseEnterValidNo));
            MobileEt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(passwordEt.getText().toString())) {
            passwordEt.setError(getString(R.string.PleaseEnterPassword));
            passwordEt.requestFocus();
            return false;
        } else if (passwordEt.getText().toString().isEmpty() || passwordEt.length() < 6) {
            passwordEt.setError(getString(R.string.PasswordStrength6));
            passwordEt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(cpasswordEt.getText().toString())) {
            cpasswordEt.setError(getString(R.string.PleaseEnterPassword));
            cpasswordEt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(empCodeEt.getText().toString())) {
            empCodeEt.setError(getString(R.string.PleaseEnterEmpCode));
            empCodeEt.requestFocus();
            return false;
        }
//        else if (TextUtils.isEmpty(otpEt.getText().toString())) {
//        otpEt.setError(getString(R.string.PleaseEnterOTP));
//        otpEt.requestFocus();
//        return false;
//         }
        else if (!(passwordEt.getText().toString().toLowerCase()
                .equalsIgnoreCase(cpasswordEt.getText().toString().toLowerCase()))) {
            passwordEt.setError(getString(R.string.err_password_do_not_match));
            passwordEt.requestFocusFromTouch();
            passwordEt.requestFocus();
        }
        else {
            signupBtn.setEnabled(false);
            signupBtn.setAlpha(0.5f);
            if(UserType.equals("Security"))
            {
                CallSecurityRegisterApi();
            }else {
                CallEmployeeRegisterApi();
            }

        }
        return true;
    }




    /**
     * Valid mobile number or not
     *
     * @return
     */
    private boolean isValidPhone()
    {
        String target = MobileEt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    /**
     * valid email or not
     *
     * @param email
     * @return
     */
    private static boolean isValidEmail(String email) {
        if(email.equals(""))
        {
            return false;
        }
        else {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

    }



    /**
     * CallSecurityRegisterApi
     */
    private void CallSecurityRegisterApi()
    {
        JsonObject object = SecurityRegisterPageObject();
        IPermitService service = ServiceFactory.createRetrofitService(RegisterScreen.this, IPermitService.class);
        mSubscription = service.SecuritySignUpResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecuritySignUpResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Toast.makeText(RegisterScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SecuritySignUpResponseModel mRespone)
                    {
                        Toast.makeText(RegisterScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();

                        if(mRespone.getStatus() == 1)
                        {
//                            Intent i = new Intent(RegisterScreen.this,LoginScreen.class);
                            Intent i = new Intent(RegisterScreen.this,OTPActivity.class);
                            i.putExtra("UserType","Security");
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
     * Json object of registerPageObject
     * @return
     */
    private JsonObject SecurityRegisterPageObject()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        String token = pref.getString("Dev_Token","N/A");
        SecuritySignUpRequestModel requestModel = new SecuritySignUpRequestModel();
        requestModel.setDevice_token(token);
        requestModel.setName(usernameEt.getText().toString());
        String email = emailEt.getText().toString().replaceAll("\\s+$", "");
        requestModel.setEmailId(email);
        requestModel.setMobileNumber(MobileEt.getText().toString());
        requestModel.setConfirmPassword(cpasswordEt.getText().toString());
        requestModel.setPassword(passwordEt.getText().toString());
        requestModel.setEmpCode(empCodeEt.getText().toString());
//        requestModel.setOtp(otpEt.getText().toString());
        requestModel.setLanguage(Language);
        if(Language.equals("English"))
        {
            requestModel.setLanguageType("en");
        }
        else if(Language.equals("Telugu"))
        {
            requestModel.setLanguageType("te");
        } else if(Language.equals("Hindi"))
        {
            requestModel.setLanguageType("hi");
        }
        else if(Language.equals("Urdu"))
        {
            requestModel.setLanguageType("ur");
        }

        requestModel.setCompanyId(compID);
        requestModel.setType(APIConstants.SECURITYTYPE);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /**
     * CallEmployeeRegisterApi
     */
    private void CallEmployeeRegisterApi()
    {
        JsonObject object = EmployeeRegisterPageObject();
        IPermitService service = ServiceFactory.createRetrofitService(RegisterScreen.this, IPermitService.class);
        mSubscription = service.EmployeeSignUpResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmployeeSignUpResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Toast.makeText(RegisterScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(EmployeeSignUpResponseModel mRespone)
                    {
                        Toast.makeText(RegisterScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        signupBtn.setEnabled(true);
                        signupBtn.setAlpha(1.0f);
                        if(mRespone.getStatus() == 1)
                        {
                            Intent i = new Intent(RegisterScreen.this,OTPActivity.class);
                            i.putExtra("UserType","Employee");
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
     * Json object of registerPageObject
     * @return
     */
    private JsonObject EmployeeRegisterPageObject()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        String token = pref.getString("Dev_Token","N/A");
        EmployeeSignUpRequestModel requestModel = new EmployeeSignUpRequestModel();
        requestModel.setDevice_token(token);
        requestModel.setName(usernameEt.getText().toString());
        String email = emailEt.getText().toString().replaceAll("\\s+$", "");
        requestModel.setEmailId(email);
        requestModel.setMobileNumber(MobileEt.getText().toString());
        requestModel.setConfirmPassword(cpasswordEt.getText().toString());
        requestModel.setPassword(passwordEt.getText().toString());
        requestModel.setFlat_number(empCodeEt.getText().toString());
        requestModel.setBlock_number(blockEt.getText().toString());
//        requestModel.setOtp(otpEt.getText().toString());
        requestModel.setLanguage(Language);
        if(Language.equals("English"))
        {
            requestModel.setLanguageType("en");
        }
        else if(Language.equals("Telugu"))
        {
            requestModel.setLanguageType("te");
        } else if(Language.equals("Hindi"))
        {
            requestModel.setLanguageType("hi");
        }
        else if(Language.equals("Urdu"))
        {
            requestModel.setLanguageType("ur");
        }

        requestModel.setCompanyId(compID);
        requestModel.setType(APIConstants.EMPTYPE);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    private void GetCompanyList() {

        IPermitService service = ServiceFactory.createRetrofitService(this, IPermitService.class);
        mSubscription = service.CompanyList(APIConstants.COMPANYLIST)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CompanyListResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onNext(CompanyListResponseModel mResponse) {
                        Log.i(TAG, "onNext: CustomerHomeOptionsModel"+mResponse);
                        String[] spinnerArray = new String[mResponse.getResult().size()];
                        HashMap<String,String> spinnerMap = new HashMap<String, String>();
                        for (int i = 0; i < mResponse.getResult().size(); i++) {
                            String CompanyName = mResponse.getResult().get(i).getCompanyName();
                            String companyID = mResponse.getResult().get(i).getCompanyId();
                            Log.i(TAG, "onNext: CompanyName: "+CompanyName+"-->"+companyID);
                            spinnerMap.put(companyID,CompanyName);
                            spinnerArray[i] = CompanyName;
                            ArrayAdapter<String> adapter =new ArrayAdapter<String>(RegisterScreen.this,android.R.layout.simple_spinner_item, spinnerArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            companySpinner.setAdapter(adapter);
                            String name = companySpinner.getSelectedItem().toString();
                            String id = spinnerMap.get(name);
                            Log.i(TAG, "onNext: "+name+"-->"+id);
                            companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Log.i(TAG, "onItemSelected: "+companySpinner.getSelectedItem().toString());
                                    for (Map.Entry<String, String> entry : spinnerMap.entrySet()) {
                                        if (entry.getValue().equals(companySpinner.getSelectedItem().toString())) {
                                            String SelectedCompID = entry.getKey();
                                            Log.i(TAG, "onItemSelected: "+SelectedCompID);
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }

                    }
                });
    }




    /**
     * CallEmployeeSendOTP
     */
    private void CallEmployeeSendOTP()
    {
        JsonObject object = SendOTPObject();
        IPermitService service = ServiceFactory.createRetrofitService(RegisterScreen.this, IPermitService.class);
        mSubscription = service.EmpSendOTP(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpSendOTPResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Toast.makeText(RegisterScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(EmpSendOTPResponseModel mRespone)
                    {
                        Toast.makeText(RegisterScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /**
     * Json object of EmployeeSendOTPObject
     * @return
     */
    private JsonObject SendOTPObject()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
        String token = pref.getString("Dev_Token","N/A");
        SendOTPRequestModel requestModel = new SendOTPRequestModel();
        requestModel.setMobileNumber(MobileEt.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



    /**
     * CallSecSendOTP
     */
    private void CallSecSendOTP()
    {
        JsonObject object = SendOTPObject();
        IPermitService service = ServiceFactory.createRetrofitService(RegisterScreen.this, IPermitService.class);
        mSubscription = service.SecSendOTP(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecSendOTPResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Toast.makeText(RegisterScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SecSendOTPResponseModel mRespone)
                    {
                        Toast.makeText(RegisterScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }



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


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent i = new Intent(RegisterScreen.this,WelcomeScreen.class);
        startActivity(i);
    }
}
