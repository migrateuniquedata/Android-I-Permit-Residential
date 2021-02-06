package com.uniquedatacom.i_permit_res.activities.ui.profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.activities.LanguagesSelectionScreen;
import com.uniquedatacom.i_permit_res.activities.ProfileSettings;
import com.uniquedatacom.i_permit_res.models.EmpProfileRequestModel;
import com.uniquedatacom.i_permit_res.models.EmpProfileResponseModel;
import com.uniquedatacom.i_permit_res.models.SecurityProfileRequestModel;
import com.uniquedatacom.i_permit_res.models.SecurityProfileResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.uniquedatacom.i_permit_res.services.Utilities.isNetworkAvailable;

public class SProfileFragment extends Fragment
{
    private String LangCode;
    private TextView profilesettingsTV,contactusTV,logoutTV,personnameTV,empidTV,departmentTV,versionTv;
    private ImageView profileimageIV,profilemoveIV,logoutIcon,contactIcon;
    private Subscription mSubscription;
    private String TAG = "SProfileFragment";
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String UserType;
    private ProgressDialog mProgressDialog;
    private View divider3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sprofile, container, false);
        SetViews(root);
        isNetworkAvailable(getActivity());
//        initProgressDialog();
//        showProgressDialog();
        SharedPreferences pref = getActivity().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        TextViewTranslation(LangCode,profilesettingsTV);
        TextViewTranslation(LangCode,contactusTV);
        TextViewTranslation(LangCode,logoutTV);
//        TextViewTranslation(LangCode,personnameTV);
//        TextViewTranslation(LangCode,empidTV);
//        TextViewTranslation(LangCode,departmentTV);
        return root;
    }

    private void SetViews(View root)
    {
        divider3 = root.findViewById(R.id.divider3);
        versionTv = root.findViewById(R.id.versionTv);
        String version = getVersionCode();
        versionTv.setText("Powered By: Unique Datacom Pvt Ltd.\n Version - "+version);
        profilesettingsTV = root.findViewById(R.id.profilesettingsTV);
        profilesettingsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfileSettings.class);
                startActivity(i);
            }
        });
        contactusTV = root.findViewById(R.id.contactusTV);
        contactusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        logoutTV = root.findViewById(R.id.logoutTV);
        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetLogoutDialog();
            }
        });
        GetSharedPrefs();
        personnameTV = root.findViewById(R.id.personnameTV);
        empidTV = root.findViewById(R.id.empidTV);
        departmentTV = root.findViewById(R.id.departmentTV);
        profileimageIV = root.findViewById(R.id.profileimageIV);
        profileimageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equals("Security"))
                {
                }
                else {
                    Intent i = new Intent(getActivity(), ProfileSettings.class);
                    i.putExtra("user_id",user_id);
                    startActivity(i);
                }

            }
        });
        profilemoveIV = root.findViewById(R.id.profilemoveIV);
        profilemoveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfileSettings.class);
                i.putExtra("user_id",user_id);
                startActivity(i);
            }
        });

        logoutIcon = root.findViewById(R.id.logoutIcon);
        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetLogoutDialog();
            }
        });
        contactIcon = root.findViewById(R.id.contactIcon);
        contactIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if(UserType.equals("Security"))
        {
            profilesettingsTV.setVisibility(View.GONE);
            profilemoveIV.setVisibility(View.GONE);
            divider3.setVisibility(View.GONE);
        }
        else {
            profilesettingsTV.setVisibility(View.VISIBLE);
            profilemoveIV.setVisibility(View.VISIBLE);
            divider3.setVisibility(View.VISIBLE);
        }

    }




    private String getVersionCode()
    {
        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        int number = info.versionCode;
        Log.i(TAG, "getVersionCode: "+version + number);
        return version;
    }

    // to intialize the Progress Dialog
    public void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
        }
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(true);
    }

    // to start the Progress Dialog
    public  void showProgressDialog()
    {

        if (!mProgressDialog.isShowing())
            mProgressDialog.show();

    }

    // to hide the Progress Dialog
    public  void hideProgressDialog()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }



    private void SetLogoutDialog()
    {
        DialogWithTwoButtons(getActivity(), getString(R.string.Logout), getString(R.string.AreYouSure), getString(R.string.ok), new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(getActivity(), LanguagesSelectionScreen.class);
//                intent1.putExtra("UserType",UserType);
                startActivity(intent1);
                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
                settings.edit().remove("user_id").commit();
            }
        }, getString(R.string.Cancel), new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void GetSharedPrefs()
    {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("LoginPref", 0);
        user_id = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Name = pref.getString("name",DEFAULT);
        Email = pref.getString("email_id",DEFAULT);
        UserPic = pref.getString("user_pic",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: "+user_id);

        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
        UserType = preferences.getString("UserType","N/A");
        Log.i(TAG, "GetSharedPrefs: UserType: "+UserType);
        if(UserType.equals("Security"))
        {
            CallSecurityProfileAPI();
        }
        else {
            CallEmpProfileAPI();
        }
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
     * CallSecurityProfileAPI
     */
    private void CallSecurityProfileAPI()
    {
        JsonObject object = SecurityProfileObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.SecurityProfileDetails(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecurityProfileResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  Toast.makeText(VisitorDetailScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onError: "+e.getLocalizedMessage());
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
//                                Toast.makeText(VisitorSignUpScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(SecurityProfileResponseModel mRespone)
                    {

                        if(mRespone.getStatus() == 1)
                        {
                           String UserPic =  mRespone.getResult().get(0).getUserPic();
                           String Name =  mRespone.getResult().get(0).getName();
                           String EMPCODE =  mRespone.getResult().get(0).getEmpCode();
                           String Department =  mRespone.getResult().get(0).getDepartment();
                            Log.i(TAG, "onNext: SecDetails"+UserPic+"-->"+Name+"-->"+EMPCODE+"-->"+Department);
                            LoadImageFromUrl(getActivity(), APIConstants.IMAGE_URL + UserPic, profileimageIV);
                            personnameTV.setText(Name+"");
                            empidTV.setVisibility(View.GONE);
                            departmentTV.setVisibility(View.INVISIBLE);
//                            empidTV.setText(EMPCODE+"");
//                            departmentTV.setText(Department);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    hideProgressDialog();
                                }
                            },1000);

                        }
                        else {

                        }

                    }
                });
    }

    /**
     * Json object of SecurityProfileObject()
     * @return
     */
    private JsonObject SecurityProfileObject()
    {
        SecurityProfileRequestModel requestModel = new SecurityProfileRequestModel();
        requestModel.setSecId(user_id);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView) {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }



    /**
     * CallEmpProfileAPI
     */
    private void CallEmpProfileAPI()
    {
        JsonObject object = EmpProfileObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.EmpProfileDetails(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpProfileResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  Toast.makeText(VisitorDetailScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onError: "+e.getLocalizedMessage());
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
//                                Toast.makeText(VisitorSignUpScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(EmpProfileResponseModel mRespone)
                    {
//                        Toast.makeText(getActivity(), mRespone.getMessage(), Toast.LENGTH_SHORT).show();

                        if(mRespone.getStatus() == 1)
                        {
                            String UserPic =  mRespone.getResult().get(0).getUserPic();
                            String Name =  mRespone.getResult().get(0).getName();
                            String FlatNo =  mRespone.getResult().get(0).getFlatNumber();
                            String BlockNo =  mRespone.getResult().get(0).getBlockNumber();
                            Log.i(TAG, "onNext: EMPDetails"+UserPic+"-->"+Name+"-->"+BlockNo+"-->"+FlatNo);
                            personnameTV.setText(Name+"");
                            empidTV.setText(FlatNo+"");
                            departmentTV.setText(BlockNo+"");
                            LoadImageFromUrl(getActivity(), APIConstants.IMAGE_URL + UserPic, profileimageIV);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    hideProgressDialog();
                                }
                            },1000);
                        }
                        else {

                        }

                    }
                });
    }

    /**
     * Json object of EmpProfileObject()
     * @return
     */
    private JsonObject EmpProfileObject()
    {
        EmpProfileRequestModel requestModel = new EmpProfileRequestModel();
        requestModel.setEId(user_id);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
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

}