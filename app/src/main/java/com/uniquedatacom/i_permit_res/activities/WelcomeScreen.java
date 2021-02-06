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

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.services.APIConstants;

public class WelcomeScreen extends Activity
{
    private Button loginBtn,signupBtn;
    public String TAG = WelcomeScreen.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        SetViewsFromLayout();

        Translation(LangCode,loginBtn);
        Translation(LangCode,signupBtn);
    }

    /**
     *  SetViewsFromLayout
     */
    private void SetViewsFromLayout()
    {
        SetSignUpBtn();
        SetLoginBtn();
    }


    /**
     * SetSignUpBtn
     */
    private void SetSignUpBtn()
    {
        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeScreen.this,UserOptionsScreen.class);
                i.putExtra("EntryTo","Register");
                startActivity(i);
            }
        });
    }

    /**
     * SetLoginBtn
     */
    private void SetLoginBtn()
    {
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeScreen.this,UserOptionsScreen.class);
                i.putExtra("EntryTo","Login");
                startActivity(i);
            }
        });
    }

    private void Translation(String languageCode, Button button)
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

        Intent i = new Intent(WelcomeScreen.this,LanguagesSelectionScreen.class);
        startActivity(i);
        finish();
    }

}
