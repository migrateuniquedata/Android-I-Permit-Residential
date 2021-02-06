package com.uniquedatacom.i_permit_res.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.ForgotPasswordRequestModel;
import com.uniquedatacom.i_permit_res.models.ForgotPasswordResponseModel;
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

public class ForgotPasswordScreen extends Activity {
    private static final String TAG = "ForgotPasswordScreen";
    private EditText MobileET,OTPET;
    private Button getVerificationCodeBtn,submitBtn;
    private TextView titleTv,backBtn,forgotpwdtvs;
    private String UserType;
    private ImageView notificationIV;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);
        GetSharedPrefs();
        UserType = getIntent().getStringExtra("UserType");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        SetViews();
        TextViewTranslation(LangCode,titleTv);
        TextViewTranslation(LangCode,forgotpwdtvs);
        ETTranslation(LangCode,MobileET);
        ETTranslation(LangCode,OTPET);
        ButtonTranslation(LangCode,getVerificationCodeBtn);
        ButtonTranslation(LangCode,submitBtn);

    }

    private void SetViews()
    {
        titleTv = findViewById(R.id.titleTv);
        forgotpwdtvs = findViewById(R.id.forgotpwdtvs);
        if(UserType.equals("Security"))
        {
            titleTv.setText("Security Forgot Password");
        }else {
            titleTv.setText("Resident Forgot Password");
        }
        notificationIV = findViewById(R.id.notificationIV);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        OTPET = findViewById(R.id.OTPET);
        OTPET.setVisibility(View.INVISIBLE);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setVisibility(View.INVISIBLE);

        getVerificationCodeBtn = findViewById(R.id.getVerificationCodeBtn);
        getVerificationCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MobileET.getText().toString().equals(""))
                {
                    MobileET.setError(getString(R.string.MobileNumber));
                }
                else {
                    OTPET.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                    if(UserType.equals("Security"))
                    {
                       CallSecForgotPasswordAPI();
                    }else {
                        CallEmpForgotPasswordAPI();
                    }
                }
            }
        });
        MobileET = findViewById(R.id.MobileET);
        OTPET = findViewById(R.id.OTPET);
    }


    private void GetSharedPrefs()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginPref", 0);
        user_id = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Name = pref.getString("name",DEFAULT);
        Email = pref.getString("email_id",DEFAULT);
        UserPic = pref.getString("user_pic",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: "+user_id);

//        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
//        UserType = preferences.getString("UserType","N/A");
//        Log.i(TAG, "GetSharedPrefs: UserType: "+UserType);

    }




    /**
     * CallEmpForgotPasswordAPI
     */
    private void CallEmpForgotPasswordAPI()
    {
        JsonObject object = EmpForgotPasswordObject();
        IPermitService service = ServiceFactory.createRetrofitService(ForgotPasswordScreen.this, IPermitService.class);
        mSubscription = service.EmpForgotPassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ForgotPasswordResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  Toast.makeText(VisitorDetailScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onError: "+e.getLocalizedMessage());
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
//                                Toast.makeText(VisitorSignUpScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(ForgotPasswordResponseModel mRespone)
                    {
                        if(mRespone.getStatus() == 1)
                        {
//                            Toast.makeText(ForgotPasswordScreen.this, "Your OTP is "+mRespone.getOTP(), Toast.LENGTH_SHORT).show();
                            submitBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    OTPET = findViewById(R.id.OTPET);
                                    Log.i(TAG, "onClick: "+OTPET.getText().toString());
                                    if(OTPET.getText().toString().equals(mRespone.getOTP()))
                                    {
                                        Intent i = new Intent(ForgotPasswordScreen.this,ResetPasswordScreen.class);
                                        i.putExtra("UserType","Security");
                                        i.putExtra("Value",MobileET.getText().toString());
                                        startActivity(i);
                                    }
                                    else {
                                        OTPET.setError(getString(R.string.PleaseEnterValidOTP));
                                        OTPET.requestFocus();
                                    }
                                }
                            });
                        }
                        else {

                        }

                    }
                });
    }

    /**
     * Json object of EmpForgotPasswordObject()
     * @return
     */
    private JsonObject EmpForgotPasswordObject()
    {
        ForgotPasswordRequestModel requestModel = new ForgotPasswordRequestModel();
        requestModel.setValue(MobileET.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



    /**
     * CallSecForgotPasswordAPI
     */
    private void CallSecForgotPasswordAPI()
    {
        JsonObject object = SecForgotPasswordObject();
        IPermitService service = ServiceFactory.createRetrofitService(ForgotPasswordScreen.this, IPermitService.class);
        mSubscription = service.SecForgotPassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ForgotPasswordResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  Toast.makeText(VisitorDetailScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onError: "+e.getLocalizedMessage());
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
//                                Toast.makeText(VisitorSignUpScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(ForgotPasswordResponseModel mRespone)
                    {
                        if(mRespone.getStatus() == 1)
                        {
                            Toast.makeText(ForgotPasswordScreen.this, "Your OTP is "+mRespone.getOTP(), Toast.LENGTH_SHORT).show();
                            submitBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    OTPET = findViewById(R.id.OTPET);
                                    Log.i(TAG, "onClick: "+OTPET.getText().toString() +"-->"+mRespone.getOTP());
                                    if(OTPET.getText().toString().equals(mRespone.getOTP()))
                                    {
                                        Intent i = new Intent(ForgotPasswordScreen.this,ResetPasswordScreen.class);
                                        i.putExtra("UserType","Security");
                                        i.putExtra("Value",MobileET.getText().toString());
                                        startActivity(i);
                                    }
                                    else {
                                        OTPET.setError(getString(R.string.PleaseEnterValidOTP));
                                        OTPET.requestFocus();
                                    }
                                }
                            });
                        }
                        else {

                        }

                    }
                });
    }

    /**
     * Json object of SecForgotPasswordObject()
     * @return
     */
    private JsonObject SecForgotPasswordObject()
    {
        ForgotPasswordRequestModel requestModel = new ForgotPasswordRequestModel();
        requestModel.setValue(MobileET.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
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

}