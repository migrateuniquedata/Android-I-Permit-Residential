package com.uniquedatacom.i_permit_res.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.EmpProfileRequestModel;
import com.uniquedatacom.i_permit_res.models.EmpProfileResponseModel;
import com.uniquedatacom.i_permit_res.models.EmpProfileSettingsRequestModel;
import com.uniquedatacom.i_permit_res.models.EmpProfileSettingsResponseModel;
import com.uniquedatacom.i_permit_res.models.GetEmpProfileDetailsResponseModel;
import com.uniquedatacom.i_permit_res.models.LanguagesListResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.uniquedatacom.i_permit_res.services.Utilities.BitmapToBase64;
import static com.uniquedatacom.i_permit_res.services.Utilities.isNetworkAvailable;

public class ProfileSettings extends AppCompatActivity {

    private static final int REQUEST_CAPTURE_IMAGE = 1;
    private TextView titleTv;
    private ImageView notificationIV;
    private  de.hdodenhof.circleimageview.CircleImageView profile_image;
    private Button backBtn;
    private TextInputEditText NameEt,EmailEt,PhoneEt,FlatEt,BlockEt,DepIDEt;
    private TextInputLayout NameEtLayout,EmailEtLayout,PhoneEtLayout,FlatEtLayout,BlockEtLayout,DepEtLayout;
    private Spinner languageSpinner;
    private Button saveBtn;
    String[] languages = { "English","Telugu", "Hindi", "Urdu"};
    private Subscription mSubscription;
    private String TAG = "ProfileSettings";
    private String SelectedLanguage;
    private Uri PflImageUri = Uri.parse(" ");
    String ImgStr = "";
    int user_id = 0;
    private ProgressDialog mProgressDialog;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String UserType;
    private String LangCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        isNetworkAvailable(this);
        initProgressDialog();
        showProgressDialog();
        SetViews();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        SelectedLanguage = pref.getString("Language","English");
        LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+SelectedLanguage);
        TextViewTranslation(LangCode,titleTv);
        InputLayoutTranslation(LangCode,NameEtLayout);
        InputLayoutTranslation(LangCode,EmailEtLayout);
        InputLayoutTranslation(LangCode,PhoneEtLayout);
        InputLayoutTranslation(LangCode,FlatEtLayout);
        InputLayoutTranslation(LangCode,BlockEtLayout);
        InputLayoutTranslation(LangCode,DepEtLayout);
        ButtonTranslation(LangCode,saveBtn);
//        SetSpinner();
    }


    private void SetViews()
    {
        titleTv = findViewById(R.id.titleTv);
        notificationIV = findViewById(R.id.notificationIV);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GetSharedPrefs();
        NameEtLayout = findViewById(R.id.NameEtLayout);
        EmailEtLayout = findViewById(R.id.EmailEtLayout);
        PhoneEtLayout = findViewById(R.id.PhoneEtLayout);
        FlatEtLayout = findViewById(R.id.FlatEtLayout);
        DepEtLayout = findViewById(R.id.DepEtLayout);
        BlockEtLayout = findViewById(R.id.BlockEtLayout);
        NameEt = findViewById(R.id.NameEt);
        profile_image = findViewById(R.id.profile_image);
        SetProfileImage();
        EmailEt = findViewById(R.id.EmailEt);
        EmailEt.setEnabled(false);
        PhoneEt = findViewById(R.id.PhoneEt);
//        PhoneEt.setEnabled(false);
        FlatEt = findViewById(R.id.FlatEt);
        BlockEt = findViewById(R.id.BlockEt);
        DepIDEt = findViewById(R.id.DepIDEt);
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();

            }
        });
        GetLanguagesList();
//        CallEmpProfileAPI();
        CallGetEmpProfileAPI();
    }


    private void Validation()
    {
        if(FlatEt.getText().toString().equals(""))
        {
            FlatEt.setError("Please Enter your Flat Number");
        }
        else  if(NameEt.getText().toString().equals(""))
        {
            NameEt.setError("Please Enter your Name");
        }
        else if(BlockEt.getText().toString().equals(""))
        {
            BlockEt.setError("Please Enter your Block Number or Name");
        }
        else if(PhoneEt.getText().toString().equals(""))
        {
            PhoneEt.setError("Please Enter your Phone Number");
        }
        else if (!isValidPhone() || PhoneEt.getText().toString().startsWith("0")) {
            PhoneEt.setError(getString(R.string.PleaseEnterValidNo));
            PhoneEt.requestFocus();
        }
        else {
            CallEmpProfileSettingsAPI();
        }
    }



    /**
     * Valid mobile number or not
     *
     * @return
     */
    private boolean isValidPhone()
    {
        String target = PhoneEt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
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
                        Toast.makeText(ProfileSettings.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                        Log.i(TAG, "onNext: LanguagesListResponseModel"+mResponse);
                        String[] spinnerArray = new String[mResponse.getResult().size()];
                        HashMap<String,String> spinnerMap = new HashMap<String, String>();
                        ArrayList<String>  ar = new ArrayList<String>();
                        for (int i = 0; i < mResponse.getResult().size(); i++)
                        {
                            String LanguageName = mResponse.getResult().get(i).getName();
                            String LanguageCode = mResponse.getResult().get(i).getCode();
                            Log.i(TAG, "onNext: LanguageName: "+LanguageName+"-->"+LanguageCode);
                            spinnerMap.put(LanguageCode,LanguageName);
                            spinnerArray[i] = LanguageName;
                            ar.add(LanguageName);
                        }
                        SetSpinner(ar,spinnerMap);
                    }
                });
    }

    private void SetSpinner(ArrayList<String> ar,HashMap<String,String> spinnerMap )
    {
        Spinner spin = (Spinner) findViewById(R.id.languageSpinner);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ar);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
//        if(SelectedLanguage.equals("English"))
//        {
//           spin.setSelection(0);
//
//        }
//        else if(SelectedLanguage.equals("Telugu"))
//        {
//            spin.setSelection(1);
//        }
//        else if(SelectedLanguage.equals("Hindi"))
//        {
//            spin.setSelection(2);
//        }
//        else if(SelectedLanguage.equals("Urdu"))
//        {
//            spin.setSelection(3);
//        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedLanguage = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "onItemSelected:Language "+SelectedLanguage);
                String code = spinnerMap.get(SelectedLanguage);
                Log.i(TAG, "onItemSelected: Code"+code);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Language",SelectedLanguage);
                if(SelectedLanguage.equals("English"))
                {
                    LangCode = "en";
                }
                else if(SelectedLanguage.equals("Telugu"))
                {
                    LangCode = "te";
                }
                else if(SelectedLanguage.equals("Hindi"))
                {
                    LangCode = "hi";
                }
                else if(SelectedLanguage.equals("Urdu"))
                {
                    LangCode = "ur";
                }
                editor.putString("LangCode",LangCode);
                editor.apply();
                editor.commit();
                InputLayoutTranslation(LangCode,NameEtLayout);
                InputLayoutTranslation(LangCode,EmailEtLayout);
                InputLayoutTranslation(LangCode,PhoneEtLayout);
                InputLayoutTranslation(LangCode,FlatEtLayout);
                InputLayoutTranslation(LangCode,BlockEtLayout);
                InputLayoutTranslation(LangCode,DepEtLayout);
                ButtonTranslation(LangCode,saveBtn);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    private void SetProfileImage() {
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });
    }



    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        profile_image.setImageBitmap(imageBitmap);
                        ImgStr = BitmapToBase64(imageBitmap);
                        Log.i(TAG, "onActivityResult: ImgStr "+ImgStr);
                    }
                }
        }
    }



    /**
     * CallEmpProfileSettingsAPI
     */
    private void CallEmpProfileSettingsAPI()
    {
        JsonObject object = EmpProfileSettingsObject();
        IPermitService service = ServiceFactory.createRetrofitService(ProfileSettings.this, IPermitService.class);
        mSubscription = service.EmpProfileSettings(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpProfileSettingsResponseModel>() {
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
                    public void onNext(EmpProfileSettingsResponseModel mRespone)
                    {
//                        Toast.makeText(ProfileSettings.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            onBackPressed();
                        }
                        else {

                        }

                    }
                });
    }

    /**
     * Json object of EmpProfileSettingsObject()
     * @return
     */
    private JsonObject EmpProfileSettingsObject()
    {
        EmpProfileSettingsRequestModel requestModel = new EmpProfileSettingsRequestModel();
        requestModel.setEId(user_id);
        requestModel.setBlock_number(BlockEt.getText().toString());
        requestModel.setLanguage(SelectedLanguage);
        requestModel.setFlat_number(FlatEt.getText().toString());
        requestModel.setName(NameEt.getText().toString());
        requestModel.setUserPic(ImgStr);
        requestModel.setMobile_number(PhoneEt.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



    /**
     * CallEmpProfileAPI
     */
    private void CallEmpProfileAPI()
    {
        JsonObject object = EmpProfileObject();
        IPermitService service = ServiceFactory.createRetrofitService(ProfileSettings.this, IPermitService.class);
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
                            String CompId = mRespone.getResult().get(0).getCommunityId();
                            Log.i(TAG, "onNext: EMPDetails"+UserPic+"-->"+Name+"-->"+FlatNo+"-->"+BlockNo+"-->"+CompId);
                            LoadImageFromUrl(ProfileSettings.this, APIConstants.IMAGE_URL + UserPic, profile_image);
                            NameEt.setText(""+Name);
                            BlockEt.setText(FlatNo+"");
                            DepIDEt.setText(BlockNo);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressDialog();
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




    /**
     * CallEmpProfileAPI
     */
    private void CallGetEmpProfileAPI()
    {
        JsonObject object = GetEmpProfileObject();
        IPermitService service = ServiceFactory.createRetrofitService(ProfileSettings.this, IPermitService.class);
        mSubscription = service.GetEmpProfileDetails(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetEmpProfileDetailsResponseModel>() {
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
                    public void onNext(GetEmpProfileDetailsResponseModel mRespone)
                    {
//                        Toast.makeText(getActivity(), mRespone.getMessage(), Toast.LENGTH_SHORT).show();

                        if(mRespone.getStatus() == 1)
                        {
                            String UserPic =  mRespone.getResult().get(0).getUserPic();
                            String Name =  mRespone.getResult().get(0).getName();
                            String FlatNo =  mRespone.getResult().get(0).getFlatNumber();
                            String BlockNo =  mRespone.getResult().get(0).getBlockNumber();
                            String EmailId = mRespone.getResult().get(0).getEmailId();
                            String Language = mRespone.getResult().get(0).getLanguage();
                            String Mobile = mRespone.getResult().get(0).getMobileNumber();
                            int Eid = mRespone.getResult().get(0).getEId();
                            Log.i(TAG, "onNext: EMPDetails"+Eid+UserPic+"-->"+Name+"-->"+FlatNo+"-->"+BlockNo+"-->"+EmailId);
                            LoadImageFromUrl(ProfileSettings.this, APIConstants.IMAGE_URL + UserPic, profile_image);
                            NameEt.setText(""+Name);
                            BlockEt.setText(BlockNo+"");
                            EmailEt.setText(EmailId);
                            FlatEt.setText(FlatNo);
                            PhoneEt.setText(Mobile);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressDialog();
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
    private JsonObject GetEmpProfileObject()
    {
        EmpProfileRequestModel requestModel = new EmpProfileRequestModel();
        requestModel.setEId(user_id);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }




    // to intialize the Progress Dialog
    public void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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




    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView) {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
