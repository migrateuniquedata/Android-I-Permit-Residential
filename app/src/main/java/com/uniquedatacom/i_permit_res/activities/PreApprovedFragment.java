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
import com.uniquedatacom.i_permit_res.adapters.EmpPreApprovedAdapter;
import com.uniquedatacom.i_permit_res.adapters.PreApprovedAdapter;
import com.uniquedatacom.i_permit_res.models.EmpPreApprovedRequestModel;
import com.uniquedatacom.i_permit_res.models.EmpPreApprovedResponseModel;
import com.uniquedatacom.i_permit_res.models.GetSecurityPreApprovedListRequestModel;
import com.uniquedatacom.i_permit_res.models.GetSecurityPreApprovedListResponseModel;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PreApprovedFragment extends Fragment {

    Context context;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String TAG = "PreApprovedFragment";
    private RecyclerView preApprovedRv;
    private Subscription mSubscription;
    private PreApprovedAdapter PreApprovedAdapter;
    private String UserType;


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
        View view = inflater.inflate(R.layout.preapproved_fragment,container,false);
        preApprovedRv = view.findViewById(R.id.preApprovedRV);
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
            getSecApprovedListAPI();
        }
        else if(UserType.equals("Employee"))
        {
            getEmpPreApprovedListAPI();
        }
    }




    private void getSecApprovedListAPI() {

        JsonObject object = SecPreApprovedListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.GetSecurityPreApprovedList(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetSecurityPreApprovedListResponseModel>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            Log.i(TAG, "onError: "+e.getMessage());
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(GetSecurityPreApprovedListResponseModel mResponse) {
                        Log.i(TAG, "onNext: GetSecurityPreApprovedListResponseModel"+mResponse);
                        PreApprovedAdapter acceptAdapter= new PreApprovedAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        preApprovedRv.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        preApprovedRv.setItemAnimator(new DefaultItemAnimator());
//                        approvedRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        preApprovedRv.setAdapter(acceptAdapter);

                    }

                });
    }



    /**
     * Json Object of ApprovedListObject
     *
     * @return
     */
    private JsonObject SecPreApprovedListObject() {
        GetSecurityPreApprovedListRequestModel mRequest = new GetSecurityPreApprovedListRequestModel();
        mRequest.setSecId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }





    private void getEmpPreApprovedListAPI() {

        JsonObject object = EmpPreApprovedListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.EmpPreApprovedList(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpPreApprovedResponseModel>() {
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
                    public void onNext(EmpPreApprovedResponseModel mResponse) {
                        EmpPreApprovedAdapter acceptAdapter= new EmpPreApprovedAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        preApprovedRv.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        preApprovedRv.setItemAnimator(new DefaultItemAnimator());
//                        approvedRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        preApprovedRv.setAdapter(acceptAdapter);

                    }

                });
    }



    /**
     * Json Object of ApprovedListObject
     *
     * @return
     */
    private JsonObject EmpPreApprovedListObject() {
        EmpPreApprovedRequestModel mRequest = new EmpPreApprovedRequestModel();
        mRequest.setEId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


}
