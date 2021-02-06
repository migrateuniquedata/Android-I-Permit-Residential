package com.uniquedatacom.i_permit_res.activities.ui.shome;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.squareup.picasso.Picasso;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.activities.NotificationsListScreen;
import com.uniquedatacom.i_permit_res.adapters.EmpLogsAdapter;
import com.uniquedatacom.i_permit_res.adapters.SecLogsAdapter;
import com.uniquedatacom.i_permit_res.adapters.VisitorsListAdapter;
import com.uniquedatacom.i_permit_res.models.EmpLogsRequestModel;
import com.uniquedatacom.i_permit_res.models.EmpLogsResponseModel;
import com.uniquedatacom.i_permit_res.models.ExitRequestModel;
import com.uniquedatacom.i_permit_res.models.ExitResponseModel;
import com.uniquedatacom.i_permit_res.models.SecLogsRequestModel;
import com.uniquedatacom.i_permit_res.models.SecLogsResponseModel;
import com.uniquedatacom.i_permit_res.models.VerifyVisitorOTPRequestModel;
import com.uniquedatacom.i_permit_res.models.VerifyVisitorOTPResponseModel;
import com.uniquedatacom.i_permit_res.models.VisitorDetailsListRequestModel;
import com.uniquedatacom.i_permit_res.models.VisitorDetailsListResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uniquedatacom.i_permit_res.utils.MyTouchListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SHomeFragment extends Fragment implements SecLogsAdapter.OnItemClicked {
    private Subscription mSubscription;
    private RecyclerView recyclerView;
    private VisitorsListAdapter visitorsListAdapter;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile, Name, Email, UserPic;
    private String TAG = "SHomeFragment";
    private TextView titleTv, nameTv, FromTv, inTimeTv;
    private ImageView notificationIV;
    private Button backBtn;
    private String UserType;
    private String SelectedLanguage, LangCode;
    Dialog OTPDialog;
    Context context;
    Dialog VisitorDetailsDialog;
    private Handler handler;
    View root;
    private SearchView searchView;
    SecLogsAdapter secLogsAdapter;
    EmpLogsAdapter empLogsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
        UserType = preferences.getString("UserType", "N/A");
        Log.i(TAG, "GetSharedPrefs: UserType: " + UserType);
        SetViews(root);
        SharedPreferences pref = getActivity().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        SelectedLanguage = pref.getString("Language", "English");
        LangCode = pref.getString("LangCode", "en");
        Log.i(TAG, "onCreate: " + SelectedLanguage);
        TextViewTranslation(LangCode, titleTv);
        TextViewTranslation(LangCode, nameTv);
        TextViewTranslation(LangCode, inTimeTv);
        TextViewTranslation(LangCode, FromTv);
        return root;
    }

    private void SetViews(View root) {
        searchView = root.findViewById(R.id.search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i(TAG, "onQueryTextChange: " + s);
                if (UserType.equals("Security")) {
                    if(secLogsAdapter!=null)
                    {
                        secLogsAdapter.getFilter().filter(s);
                    }

                } else {
                    if(empLogsAdapter!=null)
                    {
                        empLogsAdapter.getFilter().filter(s);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.i(TAG, "onQueryTextChange: " + s);
                if (UserType.equals("Security")) {
                    if(secLogsAdapter!=null)
                    {
                        secLogsAdapter.getFilter().filter(s);
                    }

                } else {
                    if(secLogsAdapter!=null)
                    {
                        empLogsAdapter.getFilter().filter(s);
                    }

                }

//                CallSearchVisitorAPI(s);
                return false;
            }
        });
        nameTv = root.findViewById(R.id.nameTv);
        inTimeTv = root.findViewById(R.id.inTimeTv);
        FromTv = root.findViewById(R.id.FromTv);
        titleTv = root.findViewById(R.id.titleTv);
        notificationIV = root.findViewById(R.id.notificationIV);
        notificationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NotificationsListScreen.class);
                startActivity(i);
            }
        });
        backBtn = root.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing App")
                        .setMessage("Are you sure to Exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeApplication();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        GetSharedPrefs(root);

        if (UserType.equals("Security")) {
            GetSecLogs(root);
        } else {
            GetEmpLogs(root);
        }
//        GetVisitorsListAPI(root);
        GetLogsTimer();

    }


    /**************************START OF GetLogsTimer()*********************************/
    private void GetLogsTimer() {
        handler = new Handler();
        handler.postDelayed(mRunnable, 60000);
    }

    private Runnable mRunnable = new Runnable() {

        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            Log.e("Notifications Handlers", "Calls");
            if (UserType.equals("Security")) {
                GetSecLogs(root);
            } else if (UserType.equals("Employee")) {
                GetEmpLogs(root);
            }

            handler.postDelayed(mRunnable, 30000);
        }
    };
    /**************************END OF GetNotificationsTimer()*********************************/


    /**
     * To close the Application
     */
    public void closeApplication() {
        getActivity().finish();
        getActivity().moveTaskToBack(true);
        try {
            handler.removeCallbacks(mRunnable);
            handler.removeCallbacksAndMessages(null);
        } catch (Exception e) {

        }

    }


    private void GetSharedPrefs(View root) {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("LoginPref", 0);
        user_id = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number", DEFAULT);
        Name = pref.getString("name", DEFAULT);
        Email = pref.getString("email_id", DEFAULT);
        UserPic = pref.getString("user_pic", DEFAULT);
        Log.i(TAG, "GetSharedPrefs: " + user_id);

    }


    private void TextViewTranslation(String languageCode, TextView button) {
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


    /**
     * Json Object of blockListObject
     *
     * @return
     */
    private JsonObject VisitorListObject() {
        VisitorDetailsListRequestModel mRequest = new VisitorDetailsListRequestModel();
        mRequest.setSecId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    private void DetailsDialog(int position, VisitorDetailsListResponseModel mResponse) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.visitor_details_alert);
        dialog.setCanceledOnTouchOutside(true);

        ImageView ProfileIV = dialog.findViewById(R.id.ProfileIV);
        LoadImageFromUrl(getActivity(), APIConstants.IMAGE_URL + mResponse.getResult().get(position).getUserPic(), ProfileIV);
        TextView intimeTv = dialog.findViewById(R.id.intimeTv);
        intimeTv.setText(mResponse.getResult().get(position).getInTime());
        TextView outTimeTv = dialog.findViewById(R.id.outTimeTv);
        outTimeTv.setText(mResponse.getResult().get(position).getOutTime());
        TextView nameTv = dialog.findViewById(R.id.nameTv);
        nameTv.setText(mResponse.getResult().get(position).getName());

        TextView fromTv = dialog.findViewById(R.id.fromTv);
        fromTv.setText(mResponse.getResult().get(position).getFrom());

        TextView mobileTv = dialog.findViewById(R.id.mobileTv);
        mobileTv.setText(mResponse.getResult().get(position).getMobileNumber());

        TextView durationTv = dialog.findViewById(R.id.durationTv);
        durationTv.setText(mResponse.getResult().get(position).getDuration());

        Button exit = dialog.findViewById(R.id.ExitBtn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dialog.show();
    }


    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView) {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }


    private void GetEmpLogs(View root) {

        JsonObject object = GetEmpLogsObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.EmpLogs(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpLogsResponseModel>() {
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
                    public void onNext(EmpLogsResponseModel mResponse) {
                        recyclerView = root.findViewById(R.id.visitorsRv);
                        empLogsAdapter = new EmpLogsAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//                        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        recyclerView.setAdapter(empLogsAdapter);
                        recyclerView.addOnItemTouchListener(new MyTouchListener(getActivity(),
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
                                        Log.i(TAG, "onClick: " + position);
//                                        DetailsDialog(position,mResponse);
                                        if (mResponse.getStatus() == 1) {
                                            if (mResponse.getResult()!=null && mResponse.getResult().size() != 0)
                                            {
                                                String name = mResponse.getResult().get(position).getVisitorName();
//                                                user staus = 0 --> visitor
//                                                user staus = 1--> preapproved
                                                int userStatus = mResponse.getResult().get(position).getStatus();
                                                String mobileNumber = "";
                                                if(userStatus == 1)
                                                {
                                                     mobileNumber = mResponse.getResult().get(position).getVisitorMobileNumber();
                                                }
                                                else{
                                                     mobileNumber = mResponse.getResult().get(position).getMobileNumber();
                                                }
                                                String userPicImgUrl = mResponse.getResult().get(position).getUserPic();
                                                String createdTime = mResponse.getResult().get(position).getCreatedTime();
                                                String outgoingtime = mResponse.getResult().get(position).getOutgoingtime();
                                                String location = mResponse.getResult().get(position).getVisitorLocation();
                                                String duration = mResponse.getResult().get(position).getDuration();
                                                int visit_id = mResponse.getResult().get(position).getVisitId();
                                                int UserStatus = mResponse.getResult().get(position).getStatus();
                                                boolean verify_status = mResponse.getResult().get(position).getVerify_status();
                                                String Temp = mResponse.getResult().get(position).getBody_temperature();
                                                String inTime = createdTime;
                                                System.out.println("inTime Date: " + inTime.substring(0, inTime.indexOf(' ')));
                                                System.out.println("inTime Time: " + inTime.substring(inTime.indexOf(' ') + 1));
                                                String intime =  inTime.substring(inTime.indexOf(' ') + 1);
                                                String outTime = outgoingtime;
                                                String outtime = "";
                                                if(outTime.equals(""))
                                                {
                                                    outtime = outTime;
                                                }
                                                else {
                                                    System.out.println("outTime Date: " + outTime.substring(0, outTime.indexOf(' ')));
                                                    System.out.println("outTime Time: " + outTime.substring(outTime.indexOf(' ') + 1));
                                                    outtime =  outTime.substring(outTime.indexOf(' ') + 1);
                                                }

                                                LogDetailsDialog(userPicImgUrl, intime, outtime, name, location, mobileNumber, duration, visit_id, UserStatus, verify_status,Temp);

                                            }
                                        } else {

                                        }

                                    }
                                }));
                    }

                });
    }


    /**
     * Json Object of blockListObject
     *
     * @return
     */
    private JsonObject GetEmpLogsObject() {
        EmpLogsRequestModel mRequest = new EmpLogsRequestModel();
        mRequest.setEId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    private void GetSecLogs(View root)
    {
        JsonObject object = GetSecLogsObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.SecLogs(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecLogsResponseModel>() {
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
                    public void onNext(SecLogsResponseModel mResponse) {
                        recyclerView = root.findViewById(R.id.visitorsRv);
                        secLogsAdapter = new SecLogsAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//                        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        recyclerView.setAdapter(secLogsAdapter);
                        recyclerView.addOnItemTouchListener(new MyTouchListener(getActivity(),
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
                                        Log.i(TAG, "onClick: " + position);
                                        if (mResponse.getStatus() == 1)
                                        {
                                            if (mResponse.getResult() != null && mResponse.getResult().size() != 0) {
                                                String name = mResponse.getResult().get(position).getVisitorName();
                                                int userStatus = mResponse.getResult().get(position).getStatus();
                                                String mobileNumber = "";
                                                if (userStatus == 1) {
                                                    mobileNumber = mResponse.getResult().get(position).getVisitorMobileNumber();
                                                } else {
                                                    mobileNumber = mResponse.getResult().get(position).getMobileNumber();
                                                }
                                                String userPicImgUrl = mResponse.getResult().get(position).getUserPic();
                                                String createdTime = mResponse.getResult().get(position).getCreatedTime();
                                                String outgoingtime = mResponse.getResult().get(position).getOutgoingtime();
                                                String location = mResponse.getResult().get(position).getVisitorLocation();
                                                String duration = mResponse.getResult().get(position).getDuration();
                                                int visit_id = mResponse.getResult().get(position).getVisitId();
                                                int UserStatus = mResponse.getResult().get(position).getStatus();
                                                boolean verify_status = mResponse.getResult().get(position).getVerify_status();
                                                String Temp = mResponse.getResult().get(position).getBody_temperature();
                                                String inTime = createdTime;
                                                System.out.println("inTime Date: " + inTime.substring(0, inTime.indexOf(' ')));
                                                System.out.println("inTime Time: " + inTime.substring(inTime.indexOf(' ') + 1));
                                                String intime = inTime.substring(inTime.indexOf(' ') + 1);
                                                String outTime = outgoingtime;
                                                String outtime = "";
                                                if (outTime.equals("")) {
                                                    outtime = outTime;
                                                } else {
                                                    System.out.println("outTime Date: " + outTime.substring(0, outTime.indexOf(' ')));
                                                    System.out.println("outTime Time: " + outTime.substring(outTime.indexOf(' ') + 1));
                                                    outtime = outTime.substring(outTime.indexOf(' ') + 1);
                                                }
                                                LogDetailsDialog(userPicImgUrl, intime, outtime, name, location, mobileNumber, duration, visit_id, UserStatus, verify_status, Temp);

                                            }
                                        }
                                    }
                                }));
                    }
                });
    }


    /**
     * Json Object of blockListObject
     *
     * @return
     */
    private JsonObject GetSecLogsObject() {
        SecLogsRequestModel mRequest = new SecLogsRequestModel();
        mRequest.setSecId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }



    private void LogDetailsDialog(String imgUrl, String intime, String outTime, String name, String location, String mobile, String duration, int visit_id, int UserStatus, boolean verify_status,String Temp)
    {
        VisitorDetailsDialog = new Dialog(getActivity());
        if(VisitorDetailsDialog.isShowing())
        {
            VisitorDetailsDialog.dismiss();
        }
        else {
            VisitorDetailsDialog.setContentView(R.layout.logs_alert);
            VisitorDetailsDialog.setCanceledOnTouchOutside(true);
            VisitorDetailsDialog.show();
            ImageView ProfileIV = VisitorDetailsDialog.findViewById(R.id.ProfileIV);
            LoadImageFromUrl(getActivity(), APIConstants.IMAGE_URL + imgUrl, ProfileIV);
            Button cancelBtn = VisitorDetailsDialog.findViewById(R.id.cancelBtn);
//                    cancelBtn.setOnTouchListener(new View.OnTouchListener() {
//                        @SuppressLint("ClickableViewAccessibility")
//                        @Override
//                        public boolean onTouch(View view, MotionEvent motionEvent) {
//                            VisitorDetailsDialog.dismiss();
//                            return false;
//                        }
//                    });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VisitorDetailsDialog.dismiss();
                }
            });
            TextView tempTv = VisitorDetailsDialog.findViewById(R.id.tempTv);
//        String convertedTime = ConvertTime(intime);
            if (Temp.equals("")) {
                tempTv.setText("");
            } else {
                tempTv.setText("Temp - " + Temp);
            }


            TextView intimeT = VisitorDetailsDialog.findViewById(R.id.intimeTv);
//        String convertedTime = ConvertTime(intime);
            intimeT.setText("In Time - " + intime);

            TextView outTimeTv = VisitorDetailsDialog.findViewById(R.id.outTimeTv);

            if (outTime.equals("")) {
                outTimeTv.setText("");
            } else {
                outTimeTv.setText("Out Time - " + outTime);
            }

            TextView nameTv = VisitorDetailsDialog.findViewById(R.id.nameTv);
            nameTv.setText("Name - " + name);

            TextView fromTv = VisitorDetailsDialog.findViewById(R.id.fromTv);
            fromTv.setText("From - " + location);

            TextView mobileTv = VisitorDetailsDialog.findViewById(R.id.mobileTv);
            if (mobile.equals("")) {
                mobileTv.setText("Mobile Number - N/A");
            } else {
                mobileTv.setText("Mobile Number - " + mobile);
            }

            TextView durationTv = VisitorDetailsDialog.findViewById(R.id.durationTv);
            if (duration.equals("")) {
                durationTv.setText("Duration of Stay - N/A");
            } else {
                durationTv.setText("Duration of Stay -" + duration);
            }

            Button exit = VisitorDetailsDialog.findViewById(R.id.ExitBtn);
            //status
            //visitor =0
            //prebooking =1
            Button otp = VisitorDetailsDialog.findViewById(R.id.otp_button);
            if (UserType.equals("Security")) {
//            otp.setVisibility(View.VISIBLE);
                if (UserStatus == 1) {
                    if (verify_status == true)
                    {
                        otp.setVisibility(View.INVISIBLE);
//                    exit.setVisibility(View.VISIBLE);
                        if (outTime.equals("")) {
                            exit.setVisibility(View.VISIBLE);
                            outTimeTv.setVisibility(View.INVISIBLE);
                        } else {
                            exit.setVisibility(View.INVISIBLE);
                            outTimeTv.setVisibility(View.VISIBLE);
                        }
                        exit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CallExitAPI(visit_id, outTimeTv, exit, UserStatus);
                            }
                        });

                    } else {
                        otp.setVisibility(View.VISIBLE);
                        exit.setVisibility(View.INVISIBLE);

                    }

                } else {
                    otp.setVisibility(View.INVISIBLE);
                    exit.setVisibility(View.VISIBLE);
                    if (outTime.equals("")) {
                        exit.setVisibility(View.VISIBLE);
                        outTimeTv.setVisibility(View.INVISIBLE);
                        otp.setVisibility(View.INVISIBLE);
                    } else {
                        exit.setVisibility(View.INVISIBLE);
                        outTimeTv.setVisibility(View.VISIBLE);
                    }
                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CallExitAPI(visit_id, outTimeTv, exit, UserStatus);
                        }
                    });
                }
                otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EnterOTPDialog(visit_id);
                    }
                });
//                    if (outTime.equals("")) {
//                        exit.setVisibility(View.VISIBLE);
//                        outTimeTv.setVisibility(View.INVISIBLE);
//                    } else {
//                        exit.setVisibility(View.INVISIBLE);
//                        outTimeTv.setVisibility(View.VISIBLE);
//                    }
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CallExitAPI(visit_id, outTimeTv, exit, UserStatus);
                    }
                });

            } else {
                otp.setVisibility(View.INVISIBLE);
                exit.setVisibility(View.INVISIBLE);
            }
        }
    }


    private void EnterOTPDialog(int visit_id) {
//        if(!OTPDialog.isShowing())
//        {
        OTPDialog = new Dialog(getActivity());
        OTPDialog.setContentView(R.layout.enter_otp_layout);
        OTPDialog.setCanceledOnTouchOutside(true);
        OTPDialog.show();
        EditText enterOtpEt = (EditText) OTPDialog.findViewById(R.id.enterOtpEt);
        Button button = (Button) OTPDialog.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call verify otp
                CallVerifyVisitorAPI(visit_id, enterOtpEt.getText().toString(), button);
            }
        });
//        }

    }

    private String ConvertTime(String startTime) {
//        String startTime = "2013-02-27 21:06:30";
        String[] parts = startTime.split(" ");
        Log.i(TAG, "ConvertTime: " + parts[0] + "-->" + parts[1]);

//        DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
//        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");
        SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat outSDF = new SimpleDateFormat("dd-mm-yyyy");

        if (parts[0] != null) {
            String outDate = "";
            try {
                Date date = inSDF.parse(parts[0]);
                outDate = outSDF.format(date);
                Log.i(TAG, "ConvertTime:outDate" + outDate);
            } catch (ParseException ex) {
            }
        }
        return startTime;
    }


    private void CallExitAPI(int visitId, TextView outTimeTv, Button exit, int userStatus) {

        JsonObject object = ExitObject(visitId,userStatus);
        IPermitService service = ServiceFactory.createRetrofitService(getActivity().getApplicationContext(), IPermitService.class);
        mSubscription = service.ExitResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ExitResponseModel>() {
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
                    public void onNext(ExitResponseModel mResponse) {
                        if (mResponse.getStatus() == 1) {
                            String outTime = mResponse.getResult().get(0).getOutgoingtime();
                            int visit_id = mResponse.getResult().get(0).getVisitId();
                            outTimeTv.setVisibility(View.VISIBLE);
                            outTimeTv.setText("out Time: " + outTime);
                            exit.setVisibility(View.INVISIBLE);
                            if (UserType.equals("Security")) {
                                GetSecLogs(root);
                            } else {
                                GetEmpLogs(root);
                            }
                        } else {

                        }
                    }

                });
    }


    /**
     * Json Object of ExitObject
     *
     * @return
     */
    private JsonObject ExitObject(int visitId,int status) {
        ExitRequestModel mRequest = new ExitRequestModel();
        //visitor =0
        //prebooking =1
        if(status == 0)
        {
            mRequest.setVisitId(visitId);
        }
        else  if(status == 1)
        {
            mRequest.setmId(visitId);
        }

        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    private void CallVerifyVisitorAPI(int visitId, String otp, Button ok) {

        JsonObject object = VerifyVisitorObject(visitId, otp);
        IPermitService service = ServiceFactory.createRetrofitService(getActivity().getApplicationContext(), IPermitService.class);
        mSubscription = service.VerifyVisitorOTPResponse(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VerifyVisitorOTPResponseModel>() {
                    @Override
                    public void onCompleted()
                    {

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
                    public void onNext(VerifyVisitorOTPResponseModel mResponse) {
                        Toast.makeText(getActivity(), mResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (mResponse.getStatus() == 1) {
                            if (VisitorDetailsDialog.isShowing()) {
                                VisitorDetailsDialog.dismiss();
                            }

                            if (OTPDialog.isShowing()) {
                                OTPDialog.dismiss();
                            }
                            if (UserType.equals("Security")) {
                                GetSecLogs(root);
                            } else {
                                GetEmpLogs(root);
                            }
                        } else {

                        }
                    }

                });
    }


    /**
     * Json Object of VerifyVisitorObject
     *
     * @return
     */
    private JsonObject VerifyVisitorObject(int visitId, String OTP) {
        VerifyVisitorOTPRequestModel mRequest = new VerifyVisitorOTPRequestModel();
        mRequest.setId(visitId);
        mRequest.setOtp(OTP);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    /*
       This method is used to set alpha for button on click and release
      */
    private void onTouchOfButton(Button btn)
    {
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        v.setAlpha(0.3f);
                        VisitorDetailsDialog.dismiss();
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setAlpha(1f);
                    default :
                        v.setAlpha(1f);
                }
                return false;
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Log.i(TAG, "onItemClick: Security log click");
    }


//
//    private void CallSearchVisitorAPI(String number) {
//
//        JsonObject object = SearchVisitorObject(number);
//        IPermitService service = ServiceFactory.createRetrofitService(getActivity().getApplicationContext(), IPermitService.class);
//        mSubscription = service.SearchResponse(object)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<SearchResponseModel>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (e instanceof HttpException) {
//                            ((HttpException) e).code();
//                            ((HttpException) e).message();
//                            ((HttpException) e).response().errorBody();
//                            try {
//                                ((HttpException) e).response().errorBody().string();
//                            } catch (IOException e1) {
//                                e1.printStackTrace();
//                            }
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onNext(SearchResponseModel mResponse) {
//                        if (mResponse.getStatus() == 1) {
//                            Toast.makeText(getActivity(), mResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                            recyclerView = root.findViewById(R.id.visitorsRv);
//                            SearchAdapter empLogsAdapter = new SearchAdapter(getActivity(), mResponse);
//                            RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
//                            recyclerView.setLayoutManager(mlayoutManager);
//                            mlayoutManager.requestLayout();
//                            recyclerView.setItemAnimator(new DefaultItemAnimator());
////                        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
//                            recyclerView.setAdapter(empLogsAdapter);
//                            recyclerView.addOnItemTouchListener(new MyTouchListener(getActivity(),
//                                    recyclerView,
//                                    new MyTouchListener.OnTouchActionListener() {
//                                        @Override
//                                        public void onLeftSwipe(View view, int position) {
////code as per your need
//                                        }
//
//                                        @Override
//                                        public void onRightSwipe(View view, int position) {
////code as per your need
//                                        }
//
//                                        @Override
//                                        public void onClick(View view, int position) {
////code as per your need
//                                            Log.i(TAG, "onClick: " + position);
////                                        DetailsDialog(position,mResponse);
//                                            if (mResponse.getStatus() == 1) {
//                                                if (mResponse.getResults().size() != 0) {
//                                                    String name = mResponse.getResults().get(0).getVisitorName();
//                                                    String mobileNumber = mResponse.getResults().get(0).getVisitorMobileNumber();
////                                                    String userPicImgUrl = mResponse.getResults().get(0).get
//                                                    String createdTime = mResponse.getResults().get(0).getDate();
//                                                    String outgoingtime = mResponse.getResults().get(0).getSelectTime();
//                                                    String location = mResponse.getResults().get(0).getVisitorLocation();
//                                                    int visit_id = mResponse.getResults().get(0).getId();
////                                                    int UserStatus = mResponse.getResults().get(0).get
////                                                    boolean verify_status = mResponse.getResults().get(0).getVerify_status();
////
////                                                    LogDetailsDialog(userPicImgUrl, createdTime, outgoingtime, name, location, mobileNumber, duration, visit_id, UserStatus, verify_status);
//
//                                                }
//                                            } else {
//
//                                            }
//
//                                        }
//                                    }));
//                        } else {
//
//                        }
//                    }
//
//                });
//
//    }
//
//    /**
//     * Json Object of SearchVisitorObject
//     *
//     * @return
//     */
//    private JsonObject SearchVisitorObject(String number) {
//        SearchRequestModel mRequest = new SearchRequestModel();
//        mRequest.setNumber(number);
//        return new Gson().toJsonTree(mRequest).getAsJsonObject();
//    }
}
