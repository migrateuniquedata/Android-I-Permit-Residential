package com.uniquedatacom.i_permit_res.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.adapters.ActivityViewPagerAdapter;
import com.uniquedatacom.i_permit_res.services.APIConstants;

public class ActivityScreen extends AppCompatActivity {
    private ActivityViewPagerAdapter viewPagerAdapter;
    private static final String TAG = OncePermitScreen.class.getSimpleName();
    TabLayout tabLayout = null;
    ViewPager mViewPager;
    private TextView titleTv;
    private ImageView notificationIV,permitTypeIV;
    private Button backBtn;
    private String LangCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        setViewFromLayout();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        TextViewTranslation(LangCode,titleTv);
    }


    private void setViewFromLayout()
    {
        titleTv = findViewById(R.id.titleTv);
        notificationIV =findViewById(R.id.notificationIV);
        permitTypeIV = findViewById(R.id.permitTypeIV);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.container);
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