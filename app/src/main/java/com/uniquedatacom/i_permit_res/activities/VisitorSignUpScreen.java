package com.uniquedatacom.i_permit_res.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.adapters.RadioAdapter;
import com.uniquedatacom.i_permit_res.models.GetEmpNameListResponseModel;
import com.uniquedatacom.i_permit_res.models.GetNameListRequestModel;
import com.uniquedatacom.i_permit_res.models.LanguagesListResponseModel;
import com.uniquedatacom.i_permit_res.models.ResidentDetailsResponseModel;
import com.uniquedatacom.i_permit_res.models.VisitorSignUpRequestModel;
import com.uniquedatacom.i_permit_res.models.VisitorSignUpResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.uniquedatacom.i_permit_res.services.Utilities.Base64ToBitmap;
import static com.uniquedatacom.i_permit_res.services.Utilities.BitmapToBase64;

public class VisitorSignUpScreen extends Activity {
    private static final int REQUEST_CAPTURE_IMAGE = 1;
    private static final int RESULT_GALLERY = 2;
    private Button SubmitBtn;
    private TextInputEditText WhomtoEt,ReasonEt,ResidentEt,blockEt,VisitorNameEt,
            IdproofEt,IdproofNumberEt,MobileNumberEt,LocationEt,BodyTempEt;
    private TextInputLayout ReasonEtLayout,ResidentETLayout,BlockEtLayout,VisitorNameEtLayout,
            IdProofTypeEtLayout,IdProofNumberEtLayout,MobileNumeberEtLayout,LocationEtLayout,BodyTempEtLayout;
    private RadioGroup radiogroup;
    private de.hdodenhof.circleimageview.CircleImageView profile_image;
    private Uri PflImageUri;
    private String PflImageBase64 = "";
    private Subscription mSubscription;
    private String Mask = "Off";
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String TAG = "VisitorDetailScreen";
    private TextView titleTv,textView3,empdetailsTV,visitordetailsTV;
    private ImageView notificationIV;
    private Button backBtn;
    private Spinner empSpinner,spinner2;
    private String SelectedEmpName;
    Integer SelectedEID = 0;
    private String LangCode;
    String compID,companyName;
    String[] idProofType = { "Aadhar Card", "Pan Card", "Voter ID", "Driving License"};
    private String SelectedIdProof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_detail_screen);

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("CompanyDetails", Context.MODE_PRIVATE);
        compID = sharedpreferences.getString("CompanyID","N/A");
        companyName = sharedpreferences.getString("CompanyName","N/A");
        Log.i(TAG, "SetViews: "+compID+"-->"+companyName);
        SetViews();
        SetSubmitButton();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        TextViewTranslation(LangCode,titleTv);
        TextViewTranslation(LangCode,textView3);
        TextViewTranslation(LangCode,empdetailsTV);
        TextViewTranslation(LangCode,visitordetailsTV);
        InputLayoutTranslation(LangCode,ReasonEtLayout);
        InputLayoutTranslation(LangCode,ResidentETLayout);
        InputLayoutTranslation(LangCode,BlockEtLayout);
        InputLayoutTranslation(LangCode,VisitorNameEtLayout);
//        InputLayoutTranslation(LangCode,IdProofTypeEtLayout);
//        InputLayoutTranslation(LangCode,IdProofNumberEtLayout);
        InputLayoutTranslation(LangCode,MobileNumeberEtLayout);
        InputLayoutTranslation(LangCode,LocationEtLayout);
        InputLayoutTranslation(LangCode,BodyTempEtLayout);
        ButtonTranslation(LangCode,SubmitBtn);
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

//        WhomtoEt = findViewById(R.id.WhomtoEt);
        visitordetailsTV = findViewById(R.id.visitordetailsTV);
        textView3 = findViewById(R.id.textView3);
        empdetailsTV = findViewById(R.id.empdetailsTV);
        ReasonEt = findViewById(R.id.ReasonEt);
        ResidentEt = findViewById(R.id.ResidentEt);
        blockEt = findViewById(R.id.blockEt);
        VisitorNameEt = findViewById(R.id.VisitorNameEt);
//        IdproofEt = findViewById(R.id.IdproofEt);
//        IdproofNumberEt = findViewById(R.id.IdproofNumberEt);
        MobileNumberEt = findViewById(R.id.MobileNumberEt);
        LocationEt = findViewById(R.id.LocationEt);
        BodyTempEt = findViewById(R.id.BodyTempEt);
        radiogroup = findViewById(R.id.radiogroup);
        profile_image = findViewById(R.id.profile_image);
        empSpinner = findViewById(R.id.empSpinner);
        ReasonEtLayout = findViewById(R.id.ReasonEtLayout);
        ResidentETLayout = findViewById(R.id.ResidentETLayout);
        BlockEtLayout = findViewById(R.id.BlockEtLayout);
        VisitorNameEtLayout = findViewById(R.id.VisitorNameEtLayout);
//        IdProofTypeEtLayout = findViewById(R.id.IdProofTypeEtLayout);
//        IdProofNumberEtLayout = findViewById(R.id.IdProofNumberEtLayout);
        MobileNumeberEtLayout = findViewById(R.id.MobileNumeberEtLayout);
        LocationEtLayout = findViewById(R.id.LocationEtLayout);
        BodyTempEtLayout = findViewById(R.id.BodyTempEtLayout);

        GetSharedPrefs();
        SetRadioGroup();
        SetProfileImage();
//        CallEmpListAPI();
        GetResidentsList();
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
//        SetIDSpinner();
    }


//
//    private void SetIDSpinner()
//    {
//
//        //Creating the ArrayAdapter instance having the country list
//        ArrayAdapter aa = new ArrayAdapter(VisitorSignUpScreen.this,android.R.layout.simple_spinner_item,idProofType);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //Setting the ArrayAdapter data on the Spinner
//        spinner2 = findViewById(R.id.spinner2);
//        spinner2.setAdapter(aa);
//        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.i(TAG, "onItemSelected: "+idProofType[position]);
//                SelectedIdProof = idProofType[position];
//                /*sunday-0
//                monday-1
//                tuesday-2
//                wed-3
//                thu-4
//                fri-5
//                sat-6*/
////                if (week.equals("Sunday"))
////                {
////                    SelectedWeek = 0;
////                }
////                else  if (week.equals("Monday"))
////                {
////                    SelectedWeek = 1;
////                }
////                else  if (week.equals("Tuesday"))
////                {
////                    SelectedWeek = 2;
////                }
////                else  if (week.equals("Wednesday"))
////                {
////                    SelectedWeek = 3;
////                }
////                else  if (week.equals("Thursday"))
////                {
////                    SelectedWeek = 4;
////                }
////                else if (week.equals("Friday"))
////                {
////                    SelectedWeek = 5;
////                }
////                else if (week.equals("Saturday"))
////                {
////                    SelectedWeek = 6;
////                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//    }

    private void SetRadioGroup()
    {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.OnBtn) {
                    Mask = "On";
                } else if(checkedId == R.id.OffBtn) {
                    Mask = "Off";
                }
            }

        });
    }


    private void SetProfileImage() {
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SelectImageDialog();
                openCameraIntent();
            }
        });
    }



    private void SelectImageDialog() {
        final Dialog dialog = new Dialog(VisitorSignUpScreen.this);
        dialog.setContentView(R.layout.camera_gallery_alert);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TitleTv);
        ImageView camImg = dialog.findViewById(R.id.camImg);
        ImageView GalImg = dialog.findViewById(R.id.GallImg);
        camImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
                dialog.dismiss();
            }
        });
        GalImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
                dialog.dismiss();
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


    /**
     * This method is used to open gallery
     */
    private void OpenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_GALLERY);
    }

    /**
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    /**
     * @param bm
     * @return
     */
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_GALLERY:
                if (null != data) {
                    PflImageUri = data.getData();
                    Log.i("PflImageUri", String.valueOf(PflImageUri));
                    String ProfileImagePath = getPath(VisitorSignUpScreen.this, PflImageUri);
                    Log.d("PflImagePath", ProfileImagePath);
                    String filename = ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/") + 1);
                    // uploadTv.setText(filename+"");
                    String FrontFileName = ProfileImagePath.substring(ProfileImagePath.lastIndexOf("/") + 1);
                    Log.i("PflFileName", FrontFileName);
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(PflImageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String encodedImage = encodeImage(selectedImage);
                        Log.i("encodedImage", encodedImage);
                        Bitmap ImgBitmap = Base64ToBitmap(encodedImage);
                        profile_image.setImageBitmap(ImgBitmap);
                        PflImageBase64 = BitmapToBase64(ImgBitmap);
                        int maxLogSize = 1000;
                        for (int i = 0; i <= encodedImage.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i + 1) * maxLogSize;
                            end = end > encodedImage.length() ? encodedImage.length() : end;
                            Log.v("PflImageBase64", encodedImage.substring(start, end));
                        }
                        // CallEditUserInfoAPI();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case REQUEST_CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        profile_image.setImageBitmap(imageBitmap);
                    }
                }
        }
    }

    private boolean FormValidation()
    {
//        if (TextUtils.isEmpty(WhomtoEt.getText().toString())) {
//            WhomtoEt.setError(getString(R.string.PleaseEnterWhomToMeet));
//            WhomtoEt.requestFocus();
//            return false;
//        }
//        else
            if (TextUtils.isEmpty(ReasonEt.getText().toString())) {
            ReasonEt.setError(getString(R.string.PleaseEnterReason));
            ReasonEt.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(ResidentEt.getText().toString())) {
                ResidentEt.setError(getString(R.string.PleaseEnterFlatNo));
                ResidentEt.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(blockEt.getText().toString())) {
                blockEt.setError(getString(R.string.PleaseEnterBlockNo));
                blockEt.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(VisitorNameEt.getText().toString())) {
            VisitorNameEt.setError(getString(R.string.PleaseEnterVisitorName));
            VisitorNameEt.requestFocus();
            return false;
        }
//        else if (TextUtils.isEmpty(IdproofEt.getText().toString())) {
//            IdproofEt.setError(getString(R.string.PleaseEnterIDType));
//            IdproofEt.requestFocus();
//            return false;
//        }
//        else if (TextUtils.isEmpty(IdproofNumberEt.getText().toString())) {
//            IdproofNumberEt.setError(getString(R.string.PleaseEnterIDNumber));
//            IdproofNumberEt.requestFocus();
//            return false;
//        }
        else if (TextUtils.isEmpty(VisitorNameEt.getText().toString())) {
            VisitorNameEt.setError(getString(R.string.PleaseEnterVisitorName));
            VisitorNameEt.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(LocationEt.getText().toString())) {
            LocationEt.setError(getString(R.string.PleaseEnterLocation));
            LocationEt.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(BodyTempEt.getText().toString())) {
            BodyTempEt.setError(getString(R.string.PleaseEnterBodyTemp));
            BodyTempEt.requestFocus();
            return false;
        }
        else if (!isValidPhone() || MobileNumberEt.getText().toString().startsWith("0")) {
            MobileNumberEt.setError(getString(R.string.PleaseEnterValidNo));
            MobileNumberEt.requestFocus();
            return false;
        }
        else {
            CallVisitorSignUpApi();
        }
        return true;
    }



    /**
     * Valid mobile number or not
     *
     * @return
     */
    private boolean isValidPhone()
    {
        String target = MobileNumberEt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }


    private void SetSubmitButton()
    {
        SubmitBtn = findViewById(R.id.SubmitBtn);
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FormValidation();

            }
        });
    }



    /**
     * CallVisitorSignUpApi
     */
    private void CallVisitorSignUpApi()
    {
        JsonObject object = visitorSignUpObject();
        IPermitService service = ServiceFactory.createRetrofitService(VisitorSignUpScreen.this, IPermitService.class);
        mSubscription = service.VisitorSignUpResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VisitorSignUpResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                      //  Toast.makeText(VisitorDetailScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onError: "+e.getLocalizedMessage());
                        Intent i = new Intent(VisitorSignUpScreen.this, SecurityNavActivity.class);
                        int SecId = getIntent().getIntExtra("user_id",0);
                        i.putExtra("user_id",SecId);
                        startActivity(i);
                        finish();
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                                Toast.makeText(VisitorSignUpScreen.this, ((HttpException) e).response().errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(VisitorSignUpResponseModel mRespone)
                    {
                        Toast.makeText(VisitorSignUpScreen.this, mRespone.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mRespone.getStatus() == 1)
                        {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("VisitorSignUpDetails", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("visit_id", mRespone.getResult().get(0).getVisitId()); // Storing string
                            editor.putString("mobile_number", mRespone.getResult().get(0).getMobileNumber());
                            editor.putString("visitor_name", mRespone.getResult().get(0).getVisiterName());
                            editor.putString("user_pic", mRespone.getResult().get(0).getUserPic());
                            editor.putInt("sec_id", mRespone.getResult().get(0).getSecId());
                            editor.apply();
                            editor.commit();

                            Intent i = new Intent(VisitorSignUpScreen.this,SecurityNavActivity.class);
                            int SecId = getIntent().getIntExtra("user_id",0);
                            i.putExtra("user_id",SecId);
                            startActivity(i);
                            finish();

                        }
                        else {

                        }

                    }
                });
    }

    /**
     * Json object of visitorSignUpObject
     * @return
     */
    private JsonObject visitorSignUpObject()
    {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("Token", 0); // 0 - for private mode
//        String token = pref.getString("Dev_Token","N/A");
        int SecId = getIntent().getIntExtra("user_id",0);
        VisitorSignUpRequestModel requestModel = new VisitorSignUpRequestModel();
        requestModel.setWhomToMeet(SelectedEmpName);
        requestModel.setReasonForVisit(ReasonEt.getText().toString());
        requestModel.setMobileNumber(MobileNumberEt.getText().toString());
        requestModel.setFlatNumber(ResidentEt.getText().toString());
        requestModel.setBlockNumber(blockEt.getText().toString());
        requestModel.setVisiterName(VisitorNameEt.getText().toString());
        requestModel.setBodyTemperature(BodyTempEt.getText().toString());
        requestModel.setUserPic(PflImageBase64);
        requestModel.setSecId(SecId);
        requestModel.setEId(SelectedEID);
        requestModel.setVisitorLocation(LocationEt.getText().toString());
        if(Mask.equals("On"))
        {
            int maskValue = 0;
            requestModel.setMask(maskValue);
        }
        else if(Mask.equals("Off"))
        {
            int maskValue = 1;
            requestModel.setMask(maskValue);
        }
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



    /**
     * CallEmpListAPI
     */
    private void CallEmpListAPI()
    {
        JsonObject object = EmpListObject();
        IPermitService service = ServiceFactory.createRetrofitService(VisitorSignUpScreen.this, IPermitService.class);
        mSubscription = service.EmpNameList(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetEmpNameListResponseModel>() {
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
                    public void onNext(GetEmpNameListResponseModel mResponse)
                    {
//                        Toast.makeText(VisitorSignUpScreen.this, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 1)
                        {
                            if(mResponse.getResult().size() == 0)
                            {
                                CustomDialogWithOneBtn(VisitorSignUpScreen.this,getString(R.string.Success),"No Employees Found to Assign","Ok", new Runnable() {
                                    @Override
                                    public void run() {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        },1000);
                                    }
                                });
                                SubmitBtn.setEnabled(false);
                                SubmitBtn.setAlpha(0.5f);
                            }
                            else {
                                String[] spinnerArray = new String[mResponse.getResult().size()];
                                HashMap<Integer, String> spinnerMap = new HashMap<Integer,String>();
                                for (int i = 0; i < mResponse.getResult().size(); i++) {
                                    String name = mResponse.getResult().get(i).getName();
                                    int EID = mResponse.getResult().get(i).getEId();
                                    Log.i(TAG, "onNext: name: "+name+"-->EID:"+EID);
                                    spinnerMap.put(EID,name);
                                    spinnerArray[i] = name;
                                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(VisitorSignUpScreen.this,android.R.layout.simple_spinner_item, spinnerArray);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    empSpinner.setAdapter(adapter);
                                    String empName = empSpinner.getSelectedItem().toString();
                                    String id = spinnerMap.get(empName);
                                    Log.i(TAG, "onNext: "+empName+"-->"+id);
                                    empSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            Log.i(TAG, "onItemSelected: Emp"+empSpinner.getSelectedItem().toString());
                                            SelectedEmpName = empSpinner.getSelectedItem().toString();
                                            for (Map.Entry<Integer, String> entry : spinnerMap.entrySet()) {
                                                if (entry.getValue().equals(empSpinner.getSelectedItem().toString())) {
                                                    SelectedEID = entry.getKey();
                                                    Log.i(TAG, "onItemSelected: "+ SelectedEID);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                }
                            }


                        }
                        else {

                        }

                    }
                });
    }

    /**
     * Json object of EmpListObject()
     * @return
     */
    private JsonObject EmpListObject()
    {
        GetNameListRequestModel requestModel = new GetNameListRequestModel();
        requestModel.setCompanyId(compID);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    public void GetResidentsList()
    {
        IPermitService service = ServiceFactory.createRetrofitService(this, IPermitService.class);
        mSubscription = service.ResidentDetailsList(APIConstants.RESIDENTLIST)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResidentDetailsResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(VisitorSignUpScreen.this, "Server Down:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                    public void onNext(ResidentDetailsResponseModel mResponse) {
                        if(mResponse.getStatus() == 1)
                        {
                            if(mResponse.getResult().size() == 0)
                            {
                                CustomDialogWithOneBtn(VisitorSignUpScreen.this,getString(R.string.Success),"No Residents Found to Assign","Ok", new Runnable() {
                                    @Override
                                    public void run() {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        },1000);
                                    }
                                });
                                SubmitBtn.setEnabled(false);
                                SubmitBtn.setAlpha(0.5f);
                            }
                            else {
                                String[] spinnerArray = new String[mResponse.getResult().size()];
                                HashMap<Integer, String> spinnerMap = new HashMap<Integer,String>();
                                HashMap<String, String> nameMap = new HashMap<String,String>();
                                HashMap<String, String> blockMap = new HashMap<String,String>();
                                for (int i = 0; i < mResponse.getResult().size(); i++) {
                                    String name = mResponse.getResult().get(i).getResidentName();
                                    String block = mResponse.getResult().get(i).getBlockNumber();
                                    String FlatNo = mResponse.getResult().get(i).getFlatNumber();
                                    int EID = mResponse.getResult().get(i).getEId();
                                    Log.i(TAG, "onNext: name: "+name+"-->EID:"+EID);
                                    spinnerMap.put(EID,FlatNo);
                                    nameMap.put(FlatNo,name);
                                    blockMap.put(FlatNo,block);
                                    spinnerArray[i] = FlatNo;
                                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(VisitorSignUpScreen.this,android.R.layout.simple_spinner_item, spinnerArray);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    empSpinner.setAdapter(adapter);
                                    String empName = empSpinner.getSelectedItem().toString();
                                    String id = spinnerMap.get(empName);
                                    Log.i(TAG, "onNext: "+empName+"-->"+id);
                                    empSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            Log.i(TAG, "onItemSelected: Emp"+empSpinner.getSelectedItem().toString());
                                            SelectedEmpName = empSpinner.getSelectedItem().toString();
                                            for (Map.Entry<Integer, String> entry : spinnerMap.entrySet()) {
                                                if (entry.getValue().equals(empSpinner.getSelectedItem().toString())) {
                                                    SelectedEID = entry.getKey();
                                                    Log.i(TAG, "onItemSelected: "+ SelectedEID);
                                                }
                                            }
                                            for (Map.Entry<String, String> entry : nameMap.entrySet()) {
                                                if (entry.getKey().equals(empSpinner.getSelectedItem().toString())) {
                                                    String name = entry.getValue();
                                                    Log.i(TAG, "onItemSelected: name"+ name);
                                                    ResidentEt.setText(name+"");
                                                }
                                            }
                                            for (Map.Entry<String, String> entry : blockMap.entrySet()) {
                                                if (entry.getKey().equals(empSpinner.getSelectedItem().toString())) {
                                                    String block = entry.getValue();
                                                    Log.i(TAG, "onItemSelected: block"+ block);
                                                    blockEt.setText(block+"");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                }
                            }

                        }
                        else {

                        }

                    }
                });
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
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogwithonebtn);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TitleTv);
        title.setText(Title);
        TextView msg = (TextView) dialog.findViewById(R.id.DescTv);
        msg.setText(Msg);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        okBtn.setText(buttonNam1);
        TextViewTranslation(LangCode,title);
        TextViewTranslation(LangCode,msg);
        ButtonTranslation(LangCode,okBtn);
        // if decline button is clicked, close the custom dialog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                runnable.run();
            }
        });

    }
    /**************************END OF CustomDialogWithOneBtn*********************************/




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

//        Intent i = new Intent(VisitorDetailScreen.this,WelcomeScreen.class);
//        startActivity(i);
//        finish();
    }
}
