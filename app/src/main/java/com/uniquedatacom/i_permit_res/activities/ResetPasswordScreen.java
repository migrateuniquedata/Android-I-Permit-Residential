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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.ChangePasswordRequestModel;
import com.uniquedatacom.i_permit_res.models.ChangePasswordResponseModel;
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

public class ResetPasswordScreen extends Activity {
    private static final String TAG = "ResetPasswordScreen";
    private EditText newPasswordET,reenterpasswordET;
    private Button saveBtn;
    private TextView forgotpwdtvs;
    private String UserType,Value;
    private Subscription mSubscription;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_screen);
        UserType = getIntent().getStringExtra("UserType");
        Value = getIntent().getStringExtra("Value");
        GetSharedPrefs();
        UserType = getIntent().getStringExtra("UserType");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        SetViews();
        TextViewTranslation(LangCode,forgotpwdtvs);
        ETTranslation(LangCode,newPasswordET);
        ETTranslation(LangCode,reenterpasswordET);
        ButtonTranslation(LangCode,saveBtn);

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

    }




    private void SetViews()
    {
        forgotpwdtvs = findViewById(R.id.forgotpwdtvs);
        newPasswordET = findViewById(R.id.newPasswordET);
        reenterpasswordET = findViewById(R.id.reenterpasswordET);
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPasswordET.getText().toString().equals(""))
                {
                    newPasswordET.setError("Please enter New Password");
                    newPasswordET.requestFocus();
                }
                else if(reenterpasswordET.getText().toString().equals(""))
                {
                    reenterpasswordET.setError("Please re-enter New Password");
                    reenterpasswordET.requestFocus();
                }
                else if(!newPasswordET.getText().toString().equals(reenterpasswordET.getText().toString()))
                {
                    reenterpasswordET.setError("Both the Passwords should be same");
                    reenterpasswordET.requestFocus();
                }
                else {
                    if(UserType.equals("Security"))
                    {
                        CallSecChangePasswordAPI();
                    }else {
                        CallEmpChangePasswordAPI();
                    }
                }

            }
        });
    }


    /**
     * CallSecForgotPasswordAPI
     */
    private void CallSecChangePasswordAPI()
    {
        JsonObject object = SecChangePasswordObject();
        IPermitService service = ServiceFactory.createRetrofitService(ResetPasswordScreen.this, IPermitService.class);
        mSubscription = service.SecChangePassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangePasswordResponseModel>() {
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
                    public void onNext(ChangePasswordResponseModel mRespone)
                    {
                        Toast.makeText(ResetPasswordScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            Intent i = new Intent(ResetPasswordScreen.this,LoginScreen.class);
                            i.putExtra("UserType",UserType);
                            startActivity(i);
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
    private JsonObject SecChangePasswordObject()
    {
        ChangePasswordRequestModel requestModel = new ChangePasswordRequestModel();
        requestModel.setValue(Value);
        requestModel.setNewPassword(newPasswordET.getText().toString());
        requestModel.setConfirmPassword(reenterpasswordET.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /**
     * CallEmpChangePasswordAPI
     */
    private void CallEmpChangePasswordAPI()
    {
        JsonObject object = EmpChangePasswordObject();
        IPermitService service = ServiceFactory.createRetrofitService(ResetPasswordScreen.this, IPermitService.class);
        mSubscription = service.EmpChangePassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangePasswordResponseModel>() {
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
                    public void onNext(ChangePasswordResponseModel mRespone)
                    {
                        Toast.makeText(ResetPasswordScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            Intent i = new Intent(ResetPasswordScreen.this,LoginScreen.class);
                            i.putExtra("UserType",UserType);
                            startActivity(i);
                        }
                        else {

                        }

                    }
                });
    }

    /**
     * Json object of EmpChangePasswordObject()
     * @return
     */
    private JsonObject EmpChangePasswordObject()
    {
        ChangePasswordRequestModel requestModel = new ChangePasswordRequestModel();
        requestModel.setValue(Value);
        requestModel.setNewPassword(newPasswordET.getText().toString());
        requestModel.setConfirmPassword(reenterpasswordET.getText().toString());
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