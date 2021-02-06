package com.uniquedatacom.i_permit_res.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.EmpSendOTPResponseModel;
import com.uniquedatacom.i_permit_res.models.EmpVerifyOTPResponseModel;
import com.uniquedatacom.i_permit_res.models.SecSendOTPResponseModel;
import com.uniquedatacom.i_permit_res.models.SecVerifyOTPResponseModel;
import com.uniquedatacom.i_permit_res.models.SendOTPRequestModel;
import com.uniquedatacom.i_permit_res.models.VerifyOTPRequestModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.uniquedatacom.i_permit_res.services.Utilities.isNetworkAvailable;

public class OTPActivity extends Activity {
    private EditText num1EdtTxt,num2EdtTxt,num3EdtTxt,num4EdtTxt;
    private TextView timerTv,verifytxv,verifyotptxv;
    private Button btnresend,verifyBtn;
    private Subscription mSubscription;
    private  String FromActivity = "";
    private String mobileNumber;
    private String TAG = "OTPActivity";
    private String UserType = "";
    private TextView titleTv;
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        isNetworkAvailable(OTPActivity.this);
        UserType = getIntent().getStringExtra("UserType");
        Log.i(TAG, "onCreate: "+UserType);
        /* intializing and assigning ID's */
        initViews();
        /* Navigation's and using the views */

        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        setViews();
        TextViewTranslation(LangCode,timerTv);
        TextViewTranslation(LangCode,titleTv);
        TextViewTranslation(LangCode,verifyotptxv);
        TextViewTranslation(LangCode,verifytxv);
        ButtonTranslation(LangCode,btnresend);
        ButtonTranslation(LangCode,verifyBtn);
        SetFromActivity();
    }

    /**
     * To initialize the views
     */
    private void initViews()
    {
        titleTv = findViewById(R.id.titleTv);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        num1EdtTxt =(EditText)findViewById(R.id.edttxt1);
        num2EdtTxt =(EditText)findViewById(R.id.edttxt2);
        num3EdtTxt =(EditText)findViewById(R.id.edttxt3);
        num4EdtTxt =(EditText)findViewById(R.id.edttxt4);
        timerTv = (TextView)findViewById(R.id.timerTv);
        verifytxv = (TextView)findViewById(R.id.verifytxv);
        verifyotptxv = (TextView)findViewById(R.id.verifyotptxv);
        verifyBtn = findViewById(R.id.verifyBtn);
        btnresend = findViewById(R.id.btnresend);
        EditTextListeners();
        SetCountDownTimer();
//        GetSharedPrefs();
    }

    /**
     * SetFromActivity
     */
    private void SetFromActivity()
    {
        FromActivity = getIntent().getStringExtra("FromActivity");
        Log.d("OTPActivity", "Activity: " + FromActivity);
        if(FromActivity.equals("RegisterScreen"))
        {
            mobileNumber = getIntent().getStringExtra("mobileNumber");
            Log.i(TAG, "mobileNumber: "+mobileNumber);
           
        }
    }


    /**
     * EditText listeners to move from one box to other
     */
    private void EditTextListeners()
    {
        EditTxt1Watcher();
        EditTxt2Watcher();
        EditTxt3Watcher();
        EditTxt4Watcher();
    }


    /**
     * EditTxt1Watcher
     */
    private void EditTxt1Watcher()
    {
        num1EdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(!num1EdtTxt.getText().toString().equals(""))
                {
                    num2EdtTxt.requestFocus();
                }

            }
        });

        num1EdtTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    num1EdtTxt.requestFocus();
                }
                return false;
            }
        });

    }

    /**
     * EditTxt2Watcher
     */
    private void EditTxt2Watcher()
    {
        num2EdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!num2EdtTxt.getText().toString().equals(""))
                {
                    num3EdtTxt.requestFocus();
                }
            }
        });

        num2EdtTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(num2EdtTxt.getText().toString().equals(""))
                    {
                        num1EdtTxt.requestFocus();
                    }

                }
                return false;
            }
        });
    }

    /**
     * EditTxt3Watcher
     */
    private void EditTxt3Watcher()
    {
        num3EdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!num3EdtTxt.getText().toString().equals(""))
                {
                    num4EdtTxt.requestFocus();
                }
            }
        });


        num3EdtTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(num3EdtTxt.getText().toString().equals(""))
                    {
                        num2EdtTxt.requestFocus();
                    }

                }
                return false;
            }
        });
    }

    /**
     * EditTxt4Watcher
     */
    private void EditTxt4Watcher()
    {
        num4EdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!num4EdtTxt.getText().toString().equals(""))
                {
                    num4EdtTxt.requestFocus();
                }
                else {
                }

            }
        });
        num4EdtTxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(num4EdtTxt.getText().toString().equals(""))
                    {
                        num3EdtTxt.requestFocus();
                    }
                }
                return false;
            }
        });
    }


    /**
     *SetCountDownTimer
     */
    private void SetCountDownTimer()
    {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), " %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timerTv.setText(text);
                // timerTv.setText("00:00:" + millisUntilFinished / 1000);
                btnresend = (Button)findViewById(R.id.btnresend);
                btnresend.setEnabled(false);
                btnresend.setAlpha(0.5f);

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                timerTv.setText("");
                btnresend = (Button)findViewById(R.id.btnresend);
                btnresend.setEnabled(true);
                btnresend.setAlpha(1.0f);
            }

        }.start();
    }



    /**
     * Set the views
     */
    private void setViews()
    {
        SetSubmitBtn();
        SetResendOTP();
    }


    /**
     * SetSubmitBtn
     */
    private void SetSubmitBtn()
    {
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNumber = getIntent().getStringExtra("mobileNumber");
                Log.i(TAG, "mobileNumber: "+mobileNumber);
                String otpEntered = num1EdtTxt.getText().toString()+num2EdtTxt.getText().toString()+
                        num3EdtTxt.getText().toString()+num4EdtTxt.getText().toString();
                if(UserType.equals("Security"))
                {
                    SecVerifyOTPAPI(mobileNumber,otpEntered);
                }else {
                    EmpVerifyOTPAPI(mobileNumber,otpEntered);
                }


            }
        });
    }


    /**
     * SetResendOTP
     */
    private void SetResendOTP()
    {
        btnresend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnresend.setAlpha(0.5f);
                        SetCountDownTimer();
                        if(UserType.equals("Security"))
                        {
                            CallSecSendOTP();
                        }else {
                            CallEmployeeSendOTP();
                        }
//                        CallResendOtp(mobileNumber);
                        //clear edittext fields
                        num1EdtTxt.setText("");
                        num1EdtTxt.requestFocus();
                        num2EdtTxt.setText("");
                        num3EdtTxt.setText("");
                        num4EdtTxt.setText("");
                        break;

                    case MotionEvent.ACTION_UP:
                        btnresend.setAlpha(1.0f);
                        break;
                }

                return true;
            }
        });
    }



    /**
     * CallEmployeeSendOTP
     */
    private void CallEmployeeSendOTP()
    {
        JsonObject object = SendOTPObject();
        IPermitService service = ServiceFactory.createRetrofitService(OTPActivity.this, IPermitService.class);
        mSubscription = service.EmpSendOTP(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpSendOTPResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(OTPActivity.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Toast.makeText(OTPActivity.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(EmpSendOTPResponseModel mRespone)
                    {
                        Toast.makeText(OTPActivity.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();

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
        requestModel.setMobileNumber(mobileNumber);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



    /**
     * CallSecSendOTP
     */
    private void CallSecSendOTP()
    {
        JsonObject object = SendOTPObject();
        IPermitService service = ServiceFactory.createRetrofitService(OTPActivity.this, IPermitService.class);
        mSubscription = service.SecSendOTP(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecSendOTPResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(OTPActivity.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Toast.makeText(OTPActivity.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SecSendOTPResponseModel mRespone)
                    {
                        Toast.makeText(OTPActivity.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /**
     * call SecVerifyOTPAPI
     */
    private void SecVerifyOTPAPI(String mobile, String otpEntered)
    {
        JsonObject object = verifyotpObject(mobile,otpEntered);
        IPermitService service = ServiceFactory.createRetrofitService(this, IPermitService.class);
        mSubscription = service.SecVerifyOTPResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecVerifyOTPResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
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
                    public void onNext(SecVerifyOTPResponseModel mRespone)
                    {
//                        hideProgressDialog();
                        Toast.makeText(OTPActivity.this, "" + mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            MoveToSecHomeScreen();

                        }
                        else
                        {

                        }
                    }
                });
    }


    /**
     * call EmpVerifyOTPAPI
     */
    private void EmpVerifyOTPAPI(String mobile, String otpEntered)
    {
        JsonObject object = verifyotpObject(mobile,otpEntered);
        IPermitService service = ServiceFactory.createRetrofitService(this, IPermitService.class);
        mSubscription = service.EmpVerifyOTPResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpVerifyOTPResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
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
                    public void onNext(EmpVerifyOTPResponseModel mRespone)
                    {
//                        hideProgressDialog();
                        Toast.makeText(OTPActivity.this, "" + mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            MoveToEmpHomeScreen();
                        }
                        else
                        {

                        }
                    }
                });
    }

    private void MoveToSecHomeScreen() {
        Intent i = new Intent(OTPActivity.this,LoginScreen.class);
        i.putExtra("UserType","Security");
        startActivity(i);
    }

    private void MoveToEmpHomeScreen() {
        Intent i = new Intent(OTPActivity.this,LoginScreen.class);
        i.putExtra("UserType","Employee");
        startActivity(i);
    }

    /**
     * Json object of verifyotpObject
     *
     * @return
     */
    private JsonObject verifyotpObject(String mobile, String otpEntered) {
        VerifyOTPRequestModel requestModel = new VerifyOTPRequestModel();
        requestModel.setMobileNumber(mobile);
        requestModel.setOtp(otpEntered);
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(OTPActivity.this,RegisterScreen.class);
        i.putExtra("UserType",UserType);
        startActivity(i);
        finish();
    }
}