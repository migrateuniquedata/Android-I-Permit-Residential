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
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.services.APIConstants;


import static com.uniquedatacom.i_permit_res.services.Utilities.isNetworkAvailable;

public class SplashScreen extends Activity
{
    private String TAG = "SplashScreen";
    private Button languageBtn;
//    private ProgressBar progressBar;
    private String LangCode;
    private com.github.ybq.android.spinkit.SpinKitView spinKitView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        CustomApplication.localeManager.setNewLocale(this, LanguagesSupport.Language.SPANISH);
        isNetworkAvailable(SplashScreen.this);
        if (isNetworkAvailable(SplashScreen.this) == false) {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        } else {
            MoveToRegister();
        }
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


    private void MoveToRegister()
    {
        languageBtn = findViewById(R.id.languageBtn);
//        progressBar = findViewById(R.id.progressBar);
        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginPref", 0);
        int SecId = pref.getInt("user_id", 0);
        Log.i(TAG, "SplashScreen: "+SecId);
        if(SecId == 0)
        {
            languageBtn.setVisibility(View.VISIBLE);
            spinKitView.setVisibility(View.INVISIBLE);
            languageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashScreen.this,LanguagesSelectionScreen.class);
                            startActivity(i);
                            finish();
                        }
                    },2000);

                }
            });
        }
        else {
            languageBtn.setVisibility(View.INVISIBLE);
            spinKitView.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this,SecurityNavActivity.class);
                    startActivity(i);
                    finish();
                }
            },4000);


        }

    }



}
