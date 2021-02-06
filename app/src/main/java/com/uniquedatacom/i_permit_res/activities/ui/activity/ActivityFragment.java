package com.uniquedatacom.i_permit_res.activities.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.activities.ApprovedFragment;
import com.uniquedatacom.i_permit_res.activities.DeniedFragment;
import com.uniquedatacom.i_permit_res.activities.NotificationsListScreen;
import com.uniquedatacom.i_permit_res.activities.OncePermitScreen;
import com.uniquedatacom.i_permit_res.activities.PreApprovedFragment;
import com.uniquedatacom.i_permit_res.adapters.ActivityViewPagerAdapter;
import com.uniquedatacom.i_permit_res.services.APIConstants;

public class ActivityFragment extends Fragment {

    private ActivityViewModel dashboardViewModel;
    private static final String TAG = OncePermitScreen.class.getSimpleName();
    TabLayout tabLayout = null;
    ViewPager mViewPager;
    ActivityViewPagerAdapter viewPagerAdapter;
    private TextView titleTv;
    private ImageView notificationIV,permitTypeIV;
    private Button backBtn;
    private String LangCode;
    private String TabStr;;
    private String UserType;
    String TabStr4;
    Translation translation4;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(ActivityViewModel.class);
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        // Setting ViewPagerroot for each Tabs

        setViewFromLayout(view);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.container);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        SharedPreferences pref = getActivity().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        TextViewTranslation(LangCode,titleTv);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabTranslation(LangCode,tabLayout);

        }
//        tabLayout.getTabAt(0).getText().toString();

        return view;
    }



    private void setViewFromLayout(View root)
    {
        titleTv = root.findViewById(R.id.titleTv);

        notificationIV = root.findViewById(R.id.notificationIV);
        notificationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NotificationsListScreen.class);
                startActivity(i);
            }
        });
        permitTypeIV = root.findViewById(R.id.permitTypeIV);

        backBtn = root.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        tabLayout = root.findViewById(R.id.tabs);
        mViewPager = root.findViewById(R.id.container);

    }

//    private void setUpTabs()
//    {
//        viewPagerAdapter = new ActivityViewPagerAdapter(getChildFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        tabLayout.setupWithViewPager(mViewPager);
//        mViewPager.setAdapter(viewPagerAdapter);
//    }




    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        ActivityViewPagerAdapter adapter = new ActivityViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ApprovedFragment(), "Approved");
        adapter.addFragment(new DeniedFragment(), "Denied");
        adapter.addFragment(new PreApprovedFragment(), "PreApproved");
//        adapter.addFragment(new VisitorsListFragment(), "Visitors");
        SharedPreferences preferences = getActivity().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
        UserType = preferences.getString("UserType","N/A");
        Log.i(TAG, "GetSharedPrefs: UserType: "+UserType);
        if(UserType.equals("Security"))
        {

        }
        else if(UserType.equals("Employee"))
        {
//            adapter.addFragment(new QueueFragment(), "Queue");
        }

        viewPager.setAdapter(adapter);

    }




    private void TabTranslation(String languageCode, TabLayout text)
    {
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(APIConstants.LANGUAGE_API_KEY)
                        .build();
                Translate translate = options.getService();
                for (int i = 0; i < text.getTabCount(); i++) {
                    TabStr =  text.getTabAt(i).getText().toString();
                }
                String TabStr1 =  text.getTabAt(0).getText().toString();
                String TabStr2 =  text.getTabAt(1).getText().toString();
                String TabStr3 =  text.getTabAt(2).getText().toString();
                if(UserType.equals("Security"))
                {

                }
                else if(UserType.equals("Employee"))
                {
//                     TabStr4 =  text.getTabAt(3).getText().toString();
                }


                final Translation translation1 =
                        translate.translate(TabStr1,
                                Translate.TranslateOption.targetLanguage(languageCode));
                final Translation translation2 =
                        translate.translate(TabStr2,
                                Translate.TranslateOption.targetLanguage(languageCode));
                final Translation translation3 =
                        translate.translate(TabStr3,
                                Translate.TranslateOption.targetLanguage(languageCode));

                if(UserType.equals("Security"))
                {

                }
                else if(UserType.equals("Employee"))
                {
//                    translation4 =
//                            translate.translate(TabStr4,
//                                    Translate.TranslateOption.targetLanguage(languageCode));
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (text != null) {
//                            for (int i = 0; i < text.getTabCount(); i++) {
//                                tabLayout.getTabAt(i).setText(translation.getTranslatedText());
//                            }
                            tabLayout.getTabAt(0).setText(translation1.getTranslatedText());
                            tabLayout.getTabAt(1).setText(translation2.getTranslatedText());
                            tabLayout.getTabAt(2).setText(translation3.getTranslatedText());
                            if(UserType.equals("Security"))
                            {

                            }
                            else if(UserType.equals("Employee"))
                            {
//                                tabLayout.getTabAt(3).setText(translation4.getTranslatedText());
                            }

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