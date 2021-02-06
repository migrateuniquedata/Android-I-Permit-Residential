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
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PermitFragment extends Fragment {
    Context context;
    Button submitBtn;
    TextView textView2;
    private String TAG = "OnceFragment";
    private com.toptoche.searchablespinnerlibrary.SearchableSpinner secSpinner;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String SelectedType;
    TextInputEditText VisistorNameEt,VehicleNoEt,LocationEt,MobileET;
    EditText dateEt,FromTimeEt,ToTimeEt;
    TextInputLayout VisitorNameEtLayout,vehicleNoEtLayout,LocEtLayout,MobileEtLayout;
    private Subscription mSubscription;
    Integer SelectedSecId;
    Spinner weekSpinner;
    String[] weeks = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday","Friday","Saturday"};
    int SelectedWeek;
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
        View view = inflater.inflate(R.layout.permit_fragment,container,false);
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
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("CompanyDetails", Context.MODE_PRIVATE);
        compID = sharedpreferences.getString("CompanyID","N/A");
        companyName = sharedpreferences.getString("CompanyName","N/A");
        Log.i(TAG, "SetViews: "+compID+"-->"+companyName);
        SetViews(view);

        SharedPreferences pref = getActivity().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        String LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        TextViewTranslation(LangCode,textView2);
        InputLayoutTranslation(LangCode,VisitorNameEtLayout);
        InputLayoutTranslation(LangCode,LocEtLayout);
        InputLayoutTranslation(LangCode,vehicleNoEtLayout);
        ETTranslation(LangCode,FromTimeEt);
        ETTranslation(LangCode,ToTimeEt);
        ETTranslation(LangCode,dateEt);
        ButtonTranslation(LangCode,submitBtn);
        return view;
    }



    private void SetViews(View view)
    {
        weekSpinner = view.findViewById(R.id.weekSpinner);
        SetWeekSpinner();
        submitBtn = view.findViewById(R.id.submitBtn);
        GetSharedPrefs(view);
        textView2 = view.findViewById(R.id.textView2);
        VisitorNameEtLayout = view.findViewById(R.id.VisitorNameEtLayout);
        LocEtLayout = view.findViewById(R.id.LocEtLayout);
        vehicleNoEtLayout = view.findViewById(R.id.vehicleNoEtLayout);
        VisistorNameEt = view.findViewById(R.id.VisistorNameEt);
        VehicleNoEt = view.findViewById(R.id.VehicleNoEt);
        LocationEt = view.findViewById(R.id.LocationEt);
        MobileEtLayout = view.findViewById(R.id.MobileEtLayout);
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


    private void SetWeekSpinner()
    {

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,weeks);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        weekSpinner.setAdapter(aa);
        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: "+weeks[position]);
                String week = weeks[position];
                /*sunday-0
                monday-1
                tuesday-2
                wed-3
                thu-4
                fri-5
                sat-6*/
                if (week.equals("Sunday"))
                {
                    SelectedWeek = 0;
                }
                else  if (week.equals("Monday"))
                {
                    SelectedWeek = 1;
                }
                else  if (week.equals("Tuesday"))
                {
                    SelectedWeek = 2;
                }
                else  if (week.equals("Wednesday"))
                {
                    SelectedWeek = 3;
                }
                else  if (week.equals("Thursday"))
                {
                    SelectedWeek = 4;
                }
                else if (week.equals("Friday"))
                {
                    SelectedWeek = 5;
                }
                else if (week.equals("Saturday"))
                {
                    SelectedWeek = 6;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void Validation()
    {
        if(VisistorNameEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter Visitor Name", Toast.LENGTH_SHORT).show();
        }
        else  if(LocationEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter Location Name", Toast.LENGTH_SHORT).show();
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
        }
        else  if(FromTimeEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter From Time", Toast.LENGTH_SHORT).show();
        }
        else  if(ToTimeEt.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please Enter To Time", Toast.LENGTH_SHORT).show();
        }
        else {
            submitBtn.setEnabled(false);
            submitBtn.setAlpha(0.5f);
            VisitorPreBookingAPI();
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
//                        String FromTime = pickedTime[0].substring(0, pickedTime[0].indexOf(' '));
//                        String FromTimeNxt = pickedTime[0] .substring(pickedTime[0] .indexOf(' ') + 1);
//                        if(FromTime.equals("0"))
//                        {
//                            fromTimeEt.setText("24:"+FromTimeNxt);
//                        }
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
                    public void onNext(ValidateTimeResponseModel mResponse)
                    {
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



    /**
     * CallSecListAPI
     */
    private void CallSecListAPI()
    {
        JsonObject object = SecListObject();
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
    private JsonObject SecListObject()
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
        requestModel.setVisitorType(1);
//        requestModel.setSecId(SelectedSecId);
        requestModel.setDate(dateEt.getText().toString());
        requestModel.setVehicleNumber(VehicleNoEt.getText().toString());
        requestModel.setSelectTime(FromTimeEt.getText().toString() + "-" + ToTimeEt.getText().toString());
        requestModel.setVisitorName(VisistorNameEt.getText().toString());
        requestModel.setVisitorLocation(LocationEt.getText().toString());
        requestModel.setSelectWeek(SelectedWeek);
        requestModel.setVisitor_mobile_number(MobileET.getText().toString());
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
