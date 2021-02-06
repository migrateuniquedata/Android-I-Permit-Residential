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
import com.uniquedatacom.i_permit_res.adapters.AcceptAdapter;
import com.uniquedatacom.i_permit_res.adapters.SecAcceptAdapter;
import com.uniquedatacom.i_permit_res.models.EmpAcceptRequestModel;
import com.uniquedatacom.i_permit_res.models.EmpAcceptResponseModel;
import com.uniquedatacom.i_permit_res.models.SecAcceptRequestModel;
import com.uniquedatacom.i_permit_res.models.SecAcceptResponseModel;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApprovedFragment extends Fragment {
    Context context;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String TAG = "ApprovedFragment";
    private RecyclerView approvedRV;
    private Subscription mSubscription;
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
        View view = inflater.inflate(R.layout.approved_fragment,container,false);
        approvedRV = view.findViewById(R.id.approvedRV);
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
            getEmpApprovedListAPI();
        }

    }



    private void getEmpApprovedListAPI() {

        JsonObject object = ApprovedListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.EmpAcceptList(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmpAcceptResponseModel>() {
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
                    public void onNext(EmpAcceptResponseModel mResponse) {
                        AcceptAdapter acceptAdapter= new AcceptAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        approvedRV.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        approvedRV.setItemAnimator(new DefaultItemAnimator());
//                        approvedRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        approvedRV.setAdapter(acceptAdapter);

                    }

                });
    }



    /**
     * Json Object of ApprovedListObject
     *
     * @return
     */
    private JsonObject ApprovedListObject() {
        EmpAcceptRequestModel mRequest = new EmpAcceptRequestModel();
        mRequest.setEId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }




    private void getSecApprovedListAPI() {

        JsonObject object = SecApprovedListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.SecAcceptList(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SecAcceptResponseModel>() {
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
                    public void onNext(SecAcceptResponseModel mResponse) {
                        SecAcceptAdapter acceptAdapter= new SecAcceptAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        approvedRV.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        approvedRV.setItemAnimator(new DefaultItemAnimator());
//                        approvedRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        approvedRV.setAdapter(acceptAdapter);

                    }

                });
    }



    /**
     * Json Object of SecApprovedListObject
     *
     * @return
     */
    private JsonObject SecApprovedListObject() {
        SecAcceptRequestModel mRequest = new SecAcceptRequestModel();
        mRequest.setSecId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }



}
