package com.uniquedatacom.i_permit_res.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.adapters.RadioAdapter;
import com.uniquedatacom.i_permit_res.models.LanguagesListResponseModel;
import com.uniquedatacom.i_permit_res.services.*;
import com.uniquedatacom.i_permit_res.utils.MyTouchListener;

import java.io.IOException;
import java.util.*;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LanguagesSelectionScreen extends Activity {
    private RecyclerView recyclerView;
    private List<String> languagesList = new ArrayList<>();
    private Button nextBtn;
    private RadioGroup languageRG;
    private Subscription mSubscription;
    private String TAG = "LanguagesSelectionScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages_screen);
        recyclerView = findViewById(R.id.recyclerView);
        GetLanguagesList();
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setEnabled(false);
        nextBtn.setAlpha(0.5f);
    }

    public void GetLanguagesList()
    {
        IPermitService service = ServiceFactory.createRetrofitService(this, IPermitService.class);
        mSubscription = service.LanguagesList(APIConstants.LANGUAGES_LIST)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LanguagesListResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LanguagesSelectionScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onNext(LanguagesListResponseModel mResponse) {
                        Log.i(TAG, "onNext: CustomerHomeOptionsModel" + mResponse);
                        String[] spinnerArray = new String[mResponse.getResult().size()];
                        HashMap<String, String> spinnerMap = new HashMap<String, String>();
                        for (int i = 0; i < mResponse.getResult().size(); i++) {
                            String LanguageName = mResponse.getResult().get(i).getName();
                            String LanguageCode = mResponse.getResult().get(i).getCode();
                            Log.i(TAG, "onNext: LanguageName: " + LanguageName + "-->" + LanguageCode);
                            spinnerMap.put(LanguageCode, LanguageName);
                            spinnerArray[i] = LanguageName;
                        }
                        RadioAdapter acceptAdapter = new RadioAdapter(LanguagesSelectionScreen.this, mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(LanguagesSelectionScreen.this);
                        recyclerView.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(acceptAdapter);
                        recyclerView.addOnItemTouchListener(new MyTouchListener(LanguagesSelectionScreen.this,
                                recyclerView,
                                new MyTouchListener.OnTouchActionListener() {
                                    @Override
                                    public void onLeftSwipe(View view, int position) {
//code as per your need
                                    }

                                    @Override
                                    public void onRightSwipe(View view, int position) {
//code as per your need
                                    }

                                    @Override
                                    public void onClick(View view, int position) {
//code as per your need
                                        String langCode = mResponse.getResult().get(position).getCode();
                                        String name = mResponse.getResult().get(position).getName();
                                        Log.i(TAG, "onClick: "+langCode);
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("Language",name);
                                        editor.putString("LangCode",langCode);
                                        editor.apply();
                                        editor.commit();
                                        nextBtn = findViewById(R.id.nextBtn);
                                        nextBtn.setEnabled(true);
                                        nextBtn.setAlpha(1.0f);
                                        nextBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(LanguagesSelectionScreen.this, WelcomeScreen.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                    }
                                }));
                    }

                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        closeApplication();
    }

    /**
     * To close the Application
     */
    public void closeApplication()
    {
        finish();
        moveTaskToBack(true);
    }

}
