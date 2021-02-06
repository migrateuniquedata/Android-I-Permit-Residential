package com.uniquedatacom.i_permit_res.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.adapters.ActivityVisitorsAdapter;
import com.uniquedatacom.i_permit_res.adapters.ActivityVisitorsSecAdapter;
import com.uniquedatacom.i_permit_res.models.ActivityVisitorsEmpResponseModel;
import com.uniquedatacom.i_permit_res.models.ActivityVisitorsEmployeeRequestModel;
import com.uniquedatacom.i_permit_res.models.ActivityVisitorsSecRequestModel;
import com.uniquedatacom.i_permit_res.models.ActivityVisitorsSecResponseModel;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VisitorsListFragment extends Fragment
{
    Context context;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String TAG = "VisitorsListFragment";
    private String UserType;
    private Subscription mSubscription;
    private RecyclerView visitorsRV;

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
        View view = inflater.inflate(R.layout.visitors_list_fragment,container,false);
        visitorsRV = view.findViewById(R.id.visitorsRV);
        GetSharedPrefs(view);
        return view;
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

        SharedPreferences preferences = getActivity().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
        UserType = preferences.getString("UserType","N/A");
        Log.i(TAG, "GetSharedPrefs: UserType: "+UserType);
        if(UserType.equals("Security"))
        {
            getActivityVisitorsSecListAPI();
        }
        else if(UserType.equals("Employee"))
        {
            getActivityVisitorsEmpListAPI();
        }

    }





    private void getActivityVisitorsEmpListAPI() {

        JsonObject object = ActivityVisitorsEmpListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.ActivityVisitorsEmp(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ActivityVisitorsEmpResponseModel>() {
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
                    public void onNext(ActivityVisitorsEmpResponseModel mResponse) {
                        ActivityVisitorsAdapter acceptAdapter= new ActivityVisitorsAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        visitorsRV.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        visitorsRV.setItemAnimator(new DefaultItemAnimator());
//                        approvedRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        visitorsRV.setAdapter(acceptAdapter);

                    }

                });
    }



    /**
     * Json Object of ApprovedListObject
     *
     * @return
     */
    private JsonObject ActivityVisitorsEmpListObject() {
        ActivityVisitorsEmployeeRequestModel mRequest = new ActivityVisitorsEmployeeRequestModel();
        mRequest.setEId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }



    private void getActivityVisitorsSecListAPI() {

        JsonObject object = ActivityVisitorsSecListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.ActivityVisitorsSec(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ActivityVisitorsSecResponseModel>() {
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
                    public void onNext(ActivityVisitorsSecResponseModel mResponse) {
                        ActivityVisitorsSecAdapter acceptAdapter= new ActivityVisitorsSecAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        visitorsRV.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        visitorsRV.setItemAnimator(new DefaultItemAnimator());
//                        approvedRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        visitorsRV.setAdapter(acceptAdapter);

                    }

                });
    }



    /**
     * Json Object of ActivityVisitorsSecListObject
     *
     * @return
     */
    private JsonObject ActivityVisitorsSecListObject() {
        ActivityVisitorsSecRequestModel mRequest = new ActivityVisitorsSecRequestModel();
        mRequest.setSecId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }



}
