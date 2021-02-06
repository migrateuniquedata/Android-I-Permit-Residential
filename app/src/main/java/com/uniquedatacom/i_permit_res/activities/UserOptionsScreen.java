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

public class UserOptionsScreen extends Activity {
    private static final String TAG = "UserOptionsScreen";
    private Button EmpBtn,securityBtn;
    private String EntryTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options_screen);
        EntryTo = getIntent().getStringExtra("EntryTo");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        SetViewsFromLayout();

        Translation(LangCode,EmpBtn);
        Translation(LangCode,securityBtn);
    }

    /**
     *  SetViewsFromLayout
     */
    private void SetViewsFromLayout()
    {
        SetEmpBtn();
        SetSecuritynBtn();
    }

    private void SetSecuritynBtn()
    {
        securityBtn = findViewById(R.id.securityBtn);
        if(EntryTo.equals("Register"))
        {
            securityBtn.setVisibility(View.INVISIBLE);
        }else {
            securityBtn.setVisibility(View.VISIBLE);
        }
        securityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EntryTo.equals("Register"))
                {
                    Intent i = new Intent(UserOptionsScreen.this,RegisterScreen.class);
                    i.putExtra("UserType","Security");
                    startActivity(i);
                }else {
                    Intent i = new Intent(UserOptionsScreen.this,LoginScreen.class);
                    i.putExtra("UserType","Security");
                    startActivity(i);
                }

            }
        });
    }

    private void SetEmpBtn()
    {
        EmpBtn = findViewById(R.id.EmpBtn);
        EmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EntryTo.equals("Register"))
                {
                    Intent i = new Intent(UserOptionsScreen.this,RegisterScreen.class);
                    i.putExtra("UserType","Employee");
                    startActivity(i);

                }else {
                    Intent i = new Intent(UserOptionsScreen.this,LoginScreen.class);
                    i.putExtra("UserType","Employee");
                    startActivity(i);

                }

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



}