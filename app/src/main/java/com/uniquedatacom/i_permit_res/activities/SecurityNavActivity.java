package com.uniquedatacom.i_permit_res.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uniquedatacom.i_permit_res.models.VersionRequestModel;
import com.uniquedatacom.i_permit_res.models.VersionResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SecurityNavActivity extends AppCompatActivity {
    private ActionBar toolbar;
    private ImageView add_button;
    private String UserType;
    private String TAG = SecurityNavActivity.class.getSimpleName();
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    String LangCode;
    private Dialog versionDialog;
    private Subscription mSubscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_nav);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_shome, R.id.navigation_activity, R.id.navigation_sprofile)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        GetSharedPrefs();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        VersionAPI();
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

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
        UserType = preferences.getString("UserType","N/A");
        Log.i(TAG, "GetSharedPrefs: UserType: "+UserType);
        SetAddButton();
    }

    private void SetAddButton()
    {
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equals("Security"))
                {
                    Intent i = new Intent(SecurityNavActivity.this, VisitorSignUpScreen.class);
                    i.putExtra("user_id",user_id);
                    startActivity(i);
                }
                else if(UserType.equals("Employee"))
                {
                    Intent i = new Intent(SecurityNavActivity.this, PermitTypeScreen.class);
                    i.putExtra("user_id",user_id);
                    startActivity(i);
                }

            }
        });
    }



    @Override
    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Closing App")
//                .setMessage("Are you sure to Exit?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        closeApplication();
//                    }
//
//                })
//                .setNegativeButton("No", null)
//                .show();
        SetCloseAppDialog();
    }

    private void SetCloseAppDialog()
    {
        DialogWithTwoButtons(SecurityNavActivity.this, getString(R.string.ClosingApp), getString(R.string.SUreToExit), getString(R.string.ok), new Runnable() {
            @Override
            public void run() {
                closeApplication();
            }
        }, getString(R.string.Cancel), new Runnable() {
            @Override
            public void run() {

            }
        });
    }



    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param ButtonName
     * @param runnable
     * @param secondButtonName
     * @param secondRunnable
     */
    public void DialogWithTwoButtons(Context context, String Title, String Msg, String ButtonName, final Runnable runnable, String secondButtonName, final Runnable secondRunnable)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_with_2buttons);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TitleTv);
        title.setText(Title);
        if(title.getText().toString().equals(""))
        {
            title.setBackgroundColor(Color.WHITE);
        }
        TextView msg = (TextView) dialog.findViewById(R.id.DescTv);
        msg.setText(Msg);
        TextViewTranslation(LangCode,msg);
        TextViewTranslation(LangCode,title);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        okBtn.setText(ButtonName);
        ButtonTranslation(LangCode,okBtn);
        // if ok button is clicked, close the custom dialog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.run();
                dialog.dismiss();
            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setText(secondButtonName);
        ButtonTranslation(LangCode,cancelBtn);
        // if decline button is clicked, close the custom dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondRunnable.run();
                dialog.dismiss();
            }
        });
    }


    /**
     * To close the Application
     */
    public void closeApplication()
    {
        finish();
        moveTaskToBack(true);
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



    private void VersionAPI() {

        JsonObject object = VersionObject();
        IPermitService service = ServiceFactory.createRetrofitService(getApplicationContext(), IPermitService.class);
        mSubscription = service.VersionUpdate(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VersionResponseModel>() {
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
                    public void onNext(VersionResponseModel mResponse) {
                        if(mResponse.getStatus() == 0)
                        {
                            CustomDialogWithOneBtn(SecurityNavActivity.this,getString(R.string.Update),getString(R.string.UpdateMsg),"Ok", new Runnable() {
                                @Override
                                public void run() {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            });
                        }
                        else {

                        }
                    }

                });
    }



    /**
     * Json Object of VersionObject
     *
     * @return
     */
    private JsonObject VersionObject() {
        VersionRequestModel mRequest = new VersionRequestModel();
        mRequest.setVersion(getVersionCode());
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }



    private String getVersionCode()
    {
        PackageManager manager = getApplicationContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        int number = info.versionCode;
        Log.i(TAG, "getVersionCode: "+version);
        return version;
    }

    /**************************START OF CustomDialogWithOneBtn*********************************/
    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param buttonNam1
     * @param runnable
     */
    public void CustomDialogWithOneBtn(Context context, String Title, String Msg, String buttonNam1, Runnable runnable)
    {
        versionDialog = new Dialog(context);
        versionDialog.setContentView(R.layout.dialogwithonebtn);
        versionDialog.setCanceledOnTouchOutside(false);
        versionDialog.show();
        TextView title = (TextView) versionDialog.findViewById(R.id.TitleTv);
        title.setText(Title);
        TextView msg = (TextView) versionDialog.findViewById(R.id.DescTv);
        msg.setText(Msg);
        Button okBtn = (Button) versionDialog.findViewById(R.id.okBtn);
        okBtn.setText(buttonNam1);
        // if decline button is clicked, close the custom dialog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versionDialog.dismiss();
                runnable.run();
            }
        });

    }
    /**************************END OF CustomDialogWithOneBtn*********************************/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(versionDialog!=null)
        {
            if(versionDialog.isShowing())
            {
                versionDialog.dismiss();
            }
        }
    }
}
