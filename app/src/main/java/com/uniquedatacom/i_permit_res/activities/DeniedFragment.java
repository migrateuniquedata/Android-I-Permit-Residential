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
import com.uniquedatacom.i_permit_res.adapters.DeniedAdapter;
import com.uniquedatacom.i_permit_res.models.EmpDeniedListResponseModel;
import com.uniquedatacom.i_permit_res.models.EmpDeniedRequestModel;
import com.uniquedatacom.i_permit_res.models.SecDeniedRequestModel;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeniedFragment extends Fragment {
    Context context;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String TAG = "DeniedFragment";
    private RecyclerView deniedRv;
    private Subscription mSubscription;
    private DeniedAdapter DeniedAdapter;
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
        View view = inflater.inflate(R.layout.denied_fragment,container,false);
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
        deniedRv = root.findViewById(R.id.deniedRV);
        SharedPreferences preferences = getActivity().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
        UserType = preferences.getString("UserType","N/A");
        Log.i(TAG, "GetSharedPrefs: UserType: "+UserType);
        if(UserType.equals("Security"))
        {
            getSecDeniedListAPI();
        }
        else if(UserType.equals("Employee"))
        {
            getEmpDeniedListAPI();
        }

    }



    private void getEmpDeniedListAPI() {

        JsonObject object = DeniedListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.EmpDeniedList(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpDeniedListResponseModel>() {
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
                    public void onNext(EmpDeniedListResponseModel mResponse) {
                        DeniedAdapter = new DeniedAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        deniedRv.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        deniedRv.setItemAnimator(new DefaultItemAnimator());
//                        deniedRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        deniedRv.setAdapter(DeniedAdapter);

                    }

                });
    }



    /**
     * Json Object of DeniedListObject
     *
     * @return
     */
    private JsonObject DeniedListObject() {
        EmpDeniedRequestModel mRequest = new EmpDeniedRequestModel();
        mRequest.setEId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    /**
     * Security denied list
     */
    private void getSecDeniedListAPI() {

        JsonObject object = SecDeniedListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.SecDeniedList(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpDeniedListResponseModel>() {
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
                    public void onNext(EmpDeniedListResponseModel mResponse) {
                        DeniedAdapter = new DeniedAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        deniedRv.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        deniedRv.setItemAnimator(new DefaultItemAnimator());
//                        deniedRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        deniedRv.setAdapter(DeniedAdapter);

                    }

                });
    }



    /**
     * Json Object of DeniedListObject
     *
     * @return
     */
    private JsonObject SecDeniedListObject() {
        SecDeniedRequestModel mRequest = new SecDeniedRequestModel();
        mRequest.setSecId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }



}
