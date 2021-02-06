package com.uniquedatacom.i_permit_res.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.GetNameListRequestModel;
import com.uniquedatacom.i_permit_res.models.GetSecNameListResponseModel;
import com.uniquedatacom.i_permit_res.models.ValidateTimeRequestModel;
import com.uniquedatacom.i_permit_res.models.ValidateTimeResponseModel;
import com.uniquedatacom.i_permit_res.models.VisitorPreBookingRequestModel;
import com.uniquedatacom.i_permit_res.models.VisitorPreBookingResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OnceFragment extends Fragment {
    Context context;
    Button submitBtn;
    TextView textView2;
    TextInputLayout VisitorNameEtLayout,vehicleNoEtLayout,LocEtLayout,MobileEtLayout;
    TextInputEditText VisistorNameEt,VehicleNoEt,LocationEt,MobileET;
    EditText dateEt,FromTimeEt,ToTimeEt;
    private TextInputLayout FromTimeETLayout,ToTimeETLayout;
    private Subscription mSubscription;
    private String TAG = "OnceFragment";
    private com.toptoche.searchablespinnerlibrary.SearchableSpinner secSpinner;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String SelectedType;
    Integer SelectedSecId;
    String compID,companyName;


    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.once_fragment,container,false);
        SetViews(view);
        SelectedType = getActivity().getIntent().getStringExtra("SelectedType");
        Log.i(TAG, "onCreateView: SelectedType: "+SelectedType);
        if(SelectedType.equals(getString(R.string.CAB)))
        {
            SelectedType = "cab";
        }
        else  if(SelectedType.equals(getString(R.string.DELIVERY)))
        {
            SelectedType = "delivery";
        }
        else  if(SelectedType.equals(getString(R.string.GUEST)))
        {
            SelectedType = "guest";
        }
        return view;
    }


    private void SetViews(View view)
    {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("CompanyDetails", Context.MODE_PRIVATE);
        compID = sharedpreferences.getString("CompanyID","N/A");
        companyName = sharedpreferences.getString("CompanyName","N/A");
        Log.i(TAG, "SetViews: "+compID+"-->"+companyName);

        submitBtn = view.findViewById(R.id.submitBtn);
        GetSharedPrefs(view);
        textView2 = view.findViewById(R.id.textView2);
        VisitorNameEtLayout = view.findViewById(R.id.VisitorNameEtLayout);
        LocEtLayout = view.findViewById(R.id.LocEtLayout);
        vehicleNoEtLayout = view.findViewById(R.id.vehicleNoEtLayout);
        MobileEtLayout = view.findViewById(R.id.MobileEtLayout);
        VisistorNameEt = view.findViewById(R.id.VisistorNameEt);
        VehicleNoEt = view.findViewById(R.id.VehicleNoEt);
        LocationEt = view.findViewById(R.id.LocationEt);
        MobileET = view.findViewById(R.id.MobileET);
        dateEt = view.findViewById(R.id.dateEt);
        dateEt = view.findViewById(R.id.dateEt);
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDatePickerDialog(dateEt);
            }
        });
        FromTimeEt = view.findViewById(R.id.FromTimeEt);
//        FromTimeETLayout = view.findViewById(R.id.FromTimeETLayout);
//        ToTimeETLayout = view.findViewById(R.id.ToTimeETLayout);
        ToTimeEt = view.findViewById(R.id.ToTimeEt);
//        FromTimeETLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TimePicker(FromTimeEt);
//            }
//        });
//        ToTimeETLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TimePicker(ToTimeEt);
//            }
//        });

        FromTimeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker(FromTimeEt);
            }
        });
        ToTimeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker(ToTimeEt);
            }
        });
        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
        secSpinner = view.findViewById(R.id.secSpinner);
        secSpinner.setTitle("Select Security Name");

        SharedPreferences pref = getActivity().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        TextViewTranslation(LangCode,textView2);
        InputLayoutTranslation(LangCode,VisitorNameEtLayout);
        InputLayoutTranslation(LangCode,LocEtLayout);
        InputLayoutTranslation(LangCode,vehicleNoEtLayout);
        InputLayoutTranslation(LangCode,MobileEtLayout);
        ETTranslation(LangCode,FromTimeEt);
        ETTranslation(LangCode,ToTimeEt);
        ETTranslation(LangCode,dateEt);
        ButtonTranslation(LangCode,submitBtn);
        CallSecListAPI();
    }




    private void GetSharedPrefs(View root)
    {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("LoginPref", 0);
        user_id = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Name = pref.getString("name",DEFAULT);
        Email = pref.getString("email_id",DEFAULT);
        UserPic = pref.getString("user_pic",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: "+user_id);

    }





    private void Validation()
    {
        if(VisistorNameEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter Visitor Name", Toast.LENGTH_SHORT).show();
//            VisistorNameEt.setError("Please Enter Visitor Name");
//            VisistorNameEt.requestFocus();
        }
        else  if(LocationEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter Location Name", Toast.LENGTH_SHORT).show();
//            LocationEt.setError("Please Enter Location Name");
//            LocationEt.requestFocus();
        }
        else if(MobileET.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
        }
        else if(VehicleNoEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter Vehicle Number", Toast.LENGTH_SHORT).show();
        }
        else  if(dateEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter Date", Toast.LENGTH_SHORT).show();
//            dateEt.setError("Please Enter Date");
//            dateEt.requestFocus();
        }
        else  if(FromTimeEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter From Time", Toast.LENGTH_SHORT).show();
//            FromTimeEt.setError("Please Enter From Time");
//            FromTimeEt.requestFocus();
        }
        else  if(ToTimeEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter To Time", Toast.LENGTH_SHORT).show();
//            ToTimeEt.setError("Please Enter To Time");
//            ToTimeEt.requestFocus();
        }
        else {
            submitBtn.setEnabled(false);
            submitBtn.setAlpha(0.5f);
            VisitorPreBookingAPI();
        }
    }



    private void TimeValidation()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        try {
            Date inTime = sdf.parse(FromTimeEt.getText().toString());
            Date outTime = sdf.parse(ToTimeEt.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    boolean isTimeAfter(Date startTime, Date endTime) {
        if (endTime.before(startTime)) { //Same way you can check with after() method also.
            return false;
        } else {
            return true;
        }
    }



    private String TimePicker(EditText fromTimeEt)
    {
        int mHour, mMinute;
        final String[] pickedTime = new String[1];
        // Get Current Time
        final Calendar[] c = {Calendar.getInstance()};
        mHour = c[0].get(Calendar.HOUR_OF_DAY);
        mMinute = c[0].get(Calendar.MINUTE);
        // int AMPM = c.get(Calendar.AM);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        pickedTime[0] = hourOfDay + ":" + minute;
                        fromTimeEt.setText(pickedTime[0]);
                       if(FromTimeEt.getText().toString().equals(""))
                       {

                       }
                       else if(ToTimeEt.getText().toString().equals(""))
                       {

                       }
                       else {
                           TimeValidateAPI();
                       }
//
//                        int hour = hourOfDay;
//                        int minutes = minute;
//                        String timeSet = "";
//                        if (hour > 12) {
//                            hour -= 12;
//                            timeSet = "PM";
//                        } else if (hour == 0) {
//                            hour += 12;
//                            timeSet = "AM";
//                        } else if (hour == 12){
//                            timeSet = "PM";
//                        }else{
//                            timeSet = "AM";
//                        }
//
//                        String min = "";
//                        if (minutes < 10)
//                            min = "0" + minutes ;
//                        else
//                            min = String.valueOf(minutes);
//
//                        // Append in a StringBuilder
//                        String aTime = new StringBuilder().append(hour).append(':')
//                                .append(min ).append(" ").append(timeSet).toString();
//                        fromTimeEt.setText(aTime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
        Log.i(TAG, "TimePicker: "+pickedTime[0]);
        return pickedTime[0];
    }



    private void SetDatePickerDialog(EditText editText)
    {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        editText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        editText.setText(year+"/" + (monthOfYear + 1) + "/" +dayOfMonth);
                        editText.setSelection(editText.getText().length());

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }




    /**
     * CallSecListAPI
     */
    private void CallSecListAPI()
    {
        JsonObject object = EmpListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.SecNameList(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetSecNameListResponseModel>() {
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
                    public void onNext(GetSecNameListResponseModel mResponse)
                    {
//                        Toast.makeText(getActivity(), mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(mResponse.getStatus() == 1)
                        {
                            String[] spinnerArray = new String[mResponse.getResult().size()];
                            HashMap<Integer, String> spinnerMap = new HashMap<Integer,String>();
                            for (int i = 0; i < mResponse.getResult().size(); i++) {
                                String name = mResponse.getResult().get(i).getName();
                                int SecID = mResponse.getResult().get(i).getSecId();
                                Log.i(TAG, "onNext: name: "+name+"-->SecID:"+SecID);
                                spinnerMap.put(SecID,name);
                                spinnerArray[i] = name;
                                ArrayAdapter<String> adapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, spinnerArray);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                secSpinner.setAdapter(adapter);
                                String empName = secSpinner.getSelectedItem().toString();
                                String id = spinnerMap.get(empName);
                                Log.i(TAG, "onNext: "+empName+"-->"+id);
                                secSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        Log.i(TAG, "onItemSelected: Emp"+secSpinner.getSelectedItem().toString());
                                        for (Map.Entry<Integer, String> entry : spinnerMap.entrySet()) {
                                            if (entry.getValue().equals(secSpinner.getSelectedItem().toString())) {
                                                Integer SelectedCompID = entry.getKey();
                                                SelectedSecId = SelectedCompID;
                                                Log.i(TAG, "onItemSelected: "+SelectedCompID);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
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




    private void VisitorPreBookingAPI() {

        JsonObject object = VisitorPreBookingObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.VisitorPreBooking(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VisitorPreBookingResponseModel>() {
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
                    public void onNext(VisitorPreBookingResponseModel mResponse) {
                        submitBtn.setEnabled(true);
                        submitBtn.setAlpha(1.0f);
                        Intent i = new Intent(context,RequestSentScreen.class);
                        context.startActivity(i);
                    }

                });
    }



    /**
     * Json Object of VisitorPreBookingObject
     *
     * @return
     */
    private JsonObject VisitorPreBookingObject() {
        VisitorPreBookingRequestModel requestModel = new VisitorPreBookingRequestModel();
        requestModel.setCompanyId(compID);
        requestModel.setEId(user_id);
        requestModel.setVehicleType(SelectedType);
        requestModel.setVisitorType(0);
//        requestModel.setSecId(SelectedSecId);
        requestModel.setDate(dateEt.getText().toString());
        requestModel.setVehicleNumber(VehicleNoEt.getText().toString());
        requestModel.setSelectTime(FromTimeEt.getText().toString() + "-" + ToTimeEt.getText().toString());
        requestModel.setVisitorName(VisistorNameEt.getText().toString());
        requestModel.setVisitorLocation(LocationEt.getText().toString());
        requestModel.setVisitor_mobile_number(MobileET.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



    private void TimeValidateAPI()
    {
        JsonObject object = TimeValidateObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.ValidateTimeResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ValidateTimeResponseModel>() {
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
                    public void onNext(ValidateTimeResponseModel mResponse) {

                        if(mResponse.getStatus() == 0)
                        {
                            Toast.makeText(context, mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            ToTimeEt.setText("");
                        }
                        else {

                        }
                    }

                });
    }



    /**
     * Json Object of TimeValidateObject
     *
     * @return
     */
    private JsonObject TimeValidateObject() {
        ValidateTimeRequestModel requestModel = new ValidateTimeRequestModel();
        requestModel.setSelectTime(FromTimeEt.getText().toString() + "-" + ToTimeEt.getText().toString());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
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


}
