package com.uniquedatacom.i_permit_res.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.services.APIConstants;

public class PermitTypeScreen extends AppCompatActivity {
    private static final String TAG = "PermitTypeScreen";
    private ImageView cabIV,deliveryIV,guestIV;
    private TextView titleTv,expectingPermitTv,cabTv,DeliveryTv,guestTv;
    private ImageView notificationIV;
    private Button backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permit_type_screen);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        SetViews();
//        TextViewTranslation(LangCode,companyNameTv);
        TextViewTranslation(LangCode,titleTv);
        TextViewTranslation(LangCode,expectingPermitTv);
        TextViewTranslation(LangCode,cabTv);
        TextViewTranslation(LangCode,DeliveryTv);
        TextViewTranslation(LangCode,guestTv);

    }

    private void SetViews()
    {
        titleTv = findViewById(R.id.titleTv);
        expectingPermitTv = findViewById(R.id.expectingPermitTv);
        cabTv = findViewById(R.id.cabTv);
        DeliveryTv= findViewById(R.id.DeliveryTv);
        guestTv = findViewById(R.id.guestTv);
        notificationIV = findViewById(R.id.notificationIV);
        notificationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PermitTypeScreen.this, NotificationsListScreen.class);
                startActivity(i);
            }
        });
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cabIV = findViewById(R.id.cabIV);
        deliveryIV = findViewById(R.id.deliveryIV);
        guestIV = findViewById(R.id.guestIV);
        cabIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PermitTypeScreen.this,OncePermitScreen.class);
                i.putExtra("SelectedType",getString(R.string.CAB));
                startActivity(i);
            }
        });

        deliveryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PermitTypeScreen.this,OncePermitScreen.class);
                i.putExtra("SelectedType",getString(R.string.DELIVERY));
                startActivity(i);
            }
        });

        guestIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PermitTypeScreen.this,OncePermitScreen.class);
                i.putExtra("SelectedType",getString(R.string.GUEST));
                startActivity(i);
            }
        });

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
        super.onBackPressed();
    }
}
