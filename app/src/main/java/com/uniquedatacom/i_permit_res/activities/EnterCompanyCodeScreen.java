package com.uniquedatacom.i_permit_res.activities;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.*;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.*;
import com.uniquedatacom.i_permit_res.services.*;
import java.io.IOException;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EnterCompanyCodeScreen extends AppCompatActivity {
    EditText CompanycodeET;
    Button submitBtn;
    TextView versionTv;
    private Subscription mSubscription;
    private ProgressDialog mProgressDialog;
    private String TAG = "EnterCompanyCodeScreen";
    private String language_code = "TE";
    private Dialog versionDialog;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_company_code_screen);
        initProgressDialog();
        VersionAPI();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginPref", 0);
        int SecId = pref.getInt("user_id", 0);
        Log.i(TAG, "EnterCompanyCodeScreen: "+SecId);
        if(SecId == 0)
        {
            CompanycodeET = findViewById(R.id.CompanycodeET);
            submitBtn = findViewById(R.id.submitBtn);
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CompanycodeET.equals(""))
                    {
                        Toast.makeText(EnterCompanyCodeScreen.this, "Please Enter the company code", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        showProgressDialog();
                        CompanyIDValidateAPI();
                    }
                }
            });
        }
        else {
            Intent i = new Intent(EnterCompanyCodeScreen.this,SplashScreen.class);
            startActivity(i);
            finish();
        }
        versionTv = findViewById(R.id.versionTv);
        String version = getVersionCode();
        versionTv.setText("Powered By: Unique Datacom Pvt Ltd.\n Version - "+version);
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

    // to intialize the Progress Dialog
    private void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    // to start the Progress Dialog
    public void showProgressDialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null)
                        initProgressDialog();
                    if (!mProgressDialog.isShowing())
                        mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // to hide the Progress Dialog
    public void hideProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mProgressDialog = null;
                }
            }
        });
    }

    private void CompanyIDValidateAPI() {

        JsonObject object = CompanyIDValidateObject();
        IPermitService service = ServiceFactory.createRetrofitService(getApplicationContext(), IPermitService.class);
        mSubscription = service.CompanyIdValidate(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CompanyIDValidateResponseModel>() {
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
                    public void onNext(CompanyIDValidateResponseModel mResponse) {
                        hideProgressDialog();
                       if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(EnterCompanyCodeScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        else {
                           SharedPreferences sharedpreferences = getSharedPreferences("CompanyDetails", Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedpreferences.edit();
                           editor.putString("CompanyName", mResponse.getResult().get(0).getCompanyName());
                           editor.putString("CompanyID", mResponse.getResult().get(0).getCompanyId());
                           editor.apply();
                           editor.commit();
                           Toast.makeText(EnterCompanyCodeScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                           Intent i = new Intent(EnterCompanyCodeScreen.this,SplashScreen.class);
                           startActivity(i);
                       }
                    }

                });
    }



    /**
     * Json Object of CompanyIDValidateObject
     *
     * @return
     */
    private JsonObject CompanyIDValidateObject() {
        CompanyIDValidateRequestModel mRequest = new CompanyIDValidateRequestModel();
        mRequest.setCompanyId(CompanycodeET.getText().toString());
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
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


    public void UpdateApp()
    {
        // Create custom dialog object
        final Dialog dialog = new Dialog(EnterCompanyCodeScreen.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialogwithonebtn);
        // dialog.setCancelable(false);
        TextView titleTv = (TextView) dialog.findViewById(R.id.TitleTv);
        titleTv.setText("Update");
        TextView MsgTv = (TextView) dialog.findViewById(R.id.DescTv);
        MsgTv.setText("Please update to the newer version to get new updates");
        dialog.show();
        Button OkButton = (Button) dialog.findViewById(R.id.okBtn);
        OkButton.setText("Update");
        // if decline button is clicked, close the custom dialog
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
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
                        hideProgressDialog();
                        if(mResponse.getStatus() == 0)
                        {
                            CustomDialogWithOneBtn(EnterCompanyCodeScreen.this,getString(R.string.Update),getString(R.string.UpdateMsg),"Ok", new Runnable() {
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