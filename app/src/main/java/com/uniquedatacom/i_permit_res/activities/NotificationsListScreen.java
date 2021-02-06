package com.uniquedatacom.i_permit_res.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import com.google.android.material.textfield.TextInputLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.adapters.EmployeeNotificationAdapter;
import com.uniquedatacom.i_permit_res.adapters.SecurityNotificationAdapter;
import com.uniquedatacom.i_permit_res.models.EmpNotificationsRequestModel;
import com.uniquedatacom.i_permit_res.models.EmpNotificationsResponseModel;
import com.uniquedatacom.i_permit_res.models.SecNotificationsRequestModel;
import com.uniquedatacom.i_permit_res.models.SecNotificationsResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationsListScreen extends AppCompatActivity {

    private Subscription mSubscription;
    private RecyclerView NotificationRv;
    private EmployeeNotificationAdapter empNotificationAdapter;
    private SecurityNotificationAdapter securityNotificationAdapter;
    private String UserType;
    private String TAG = "NotificationsListScreen";
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private TextView titleTv;
    private ImageView notificationIV;
    private Button backBtn;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_list_screen);
        titleTv = findViewById(R.id.titleTv);
        notificationIV = findViewById(R.id.notificationIV);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GetSharedPrefs();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        TextViewTranslation(LangCode,titleTv);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
        UserType = preferences.getString("UserType","N/A");
        Log.i(TAG, "GetSharedPrefs: UserType: "+UserType);
        if(UserType.equals("Security"))
        {
            getSecNotificationListAPI();
        }
        else if(UserType.equals("Employee"))
        {
            getEmpNotificationListAPI();
        }

        GetNotificationsTimer();
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



    /**************************START OF GetNotificationsTimer()*********************************/
    private void GetNotificationsTimer()
    {
        handler = new Handler();
        handler.postDelayed(mRunnable, 10000);
    }

    private Runnable mRunnable = new Runnable() {

        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            Log.e("Notifications Handlers", "Calls");
            if(UserType.equals("Security"))
            {
                getSecNotificationListAPI();
            }
            else if(UserType.equals("Employee"))
            {
                getEmpNotificationListAPI();
            }

            handler.postDelayed(mRunnable, 10000);
        }
    };
    /**************************END OF GetNotificationsTimer()*********************************/



    private void getEmpNotificationListAPI() {

        JsonObject object = NotificationListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getApplicationContext(), IPermitService.class);
        mSubscription = service.EmpNotifications(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpNotificationsResponseModel>() {
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
                    public void onNext(EmpNotificationsResponseModel mResponse) {
                        NotificationRv = findViewById(R.id.NotificationsRV);
                        empNotificationAdapter = new EmployeeNotificationAdapter(NotificationsListScreen.this, mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
                        NotificationRv.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        NotificationRv.setItemAnimator(new DefaultItemAnimator());
//                        NotificationRv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                        NotificationRv.setAdapter(empNotificationAdapter);

                    }

                });
    }



    /**
     * Json Object of blockListObject
     *
     * @return
     */
    private JsonObject NotificationListObject() {
        EmpNotificationsRequestModel mRequest = new EmpNotificationsRequestModel();
        mRequest.setEId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }




    private void getSecNotificationListAPI() {

        JsonObject object = SecNotificationListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getApplicationContext(), IPermitService.class);
        mSubscription = service.SecNotificatiosn(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecNotificationsResponseModel>() {
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
                    public void onNext(SecNotificationsResponseModel mResponse) {
                        NotificationRv = findViewById(R.id.NotificationsRV);
                        securityNotificationAdapter = new SecurityNotificationAdapter(NotificationsListScreen.this, mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
                        NotificationRv.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        NotificationRv.setItemAnimator(new DefaultItemAnimator());
//                        NotificationRv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                        NotificationRv.setAdapter(securityNotificationAdapter);
                    }
                });
    }



    /**
     * Json Object of SecNotificationListObject
     *
     * @return
     */
    private JsonObject SecNotificationListObject() {
        SecNotificationsRequestModel mRequest = new SecNotificationsRequestModel();
        mRequest.setSecId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            handler.removeCallbacks(mRunnable);
            handler.removeCallbacksAndMessages(null);
        }catch (Exception e)
        {

        }
        Intent i = new Intent(NotificationsListScreen.this,SecurityNavActivity.class);
        startActivity(i);
        finish();
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