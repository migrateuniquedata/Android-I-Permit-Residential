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
import com.uniquedatacom.i_permit_res.adapters.RequestsAdapter;
import com.uniquedatacom.i_permit_res.models.VisitorsRequestsListRequestModel;
import com.uniquedatacom.i_permit_res.models.VisitorsRequestsListResponseModel;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QueueFragment extends Fragment
{
    Context context;
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String TAG = "VisitorsListFragment";
    private String UserType;
    private Subscription mSubscription;
    private RecyclerView requestsRv;
    private RequestsAdapter requestsAdapter;

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
        requestsRv = view.findViewById(R.id.visitorsRV);
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
//            getActivityVisitorsSecListAPI();
        }
        else if(UserType.equals("Employee"))
        {
            getEmpNotificationListAPI();
        }

    }

    private void getEmpNotificationListAPI() {

        JsonObject object = NotificationListObject();
        IPermitService service = ServiceFactory.createRetrofitService(getActivity(), IPermitService.class);
        mSubscription = service.ReQuestsList(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VisitorsRequestsListResponseModel>() {
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
                    public void onNext(VisitorsRequestsListResponseModel mResponse) {
                        requestsAdapter = new RequestsAdapter(getActivity(), mResponse);
                        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
                        requestsRv.setLayoutManager(mlayoutManager);
                        mlayoutManager.requestLayout();
                        requestsRv.setItemAnimator(new DefaultItemAnimator());
//                        requestsRv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                        requestsRv.setAdapter(requestsAdapter);

                    }

                });
    }

    /**
     * Json Object of blockListObject
     *
     * @return
     */
    private JsonObject NotificationListObject() {
        VisitorsRequestsListRequestModel mRequest = new VisitorsRequestsListRequestModel();
        mRequest.setEId(user_id);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

}
