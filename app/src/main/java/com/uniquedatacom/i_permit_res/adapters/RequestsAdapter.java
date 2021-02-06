package com.uniquedatacom.i_permit_res.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.activities.SecurityNavActivity;
import com.uniquedatacom.i_permit_res.models.VisitorStatusRequestModel;
import com.uniquedatacom.i_permit_res.models.VisitorStatusResponseModel;
import com.uniquedatacom.i_permit_res.models.VisitorsRequestsListResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;
import com.uniquedatacom.i_permit_res.services.IPermitService;
import com.uniquedatacom.i_permit_res.services.ServiceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.NotificationVH> {
    // public List<NotificationModel> NotificationModelArrayList;
    Context context;
    VisitorsRequestsListResponseModel mResponse;
    public List<VisitorsRequestsListResponseModel> requestsResponseModelList = new ArrayList<>();
    private Subscription mSubscription;
    private String TAG = "RequestsAdapter";

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public RequestsAdapter(Context context, VisitorsRequestsListResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
    }

    @NonNull
    @Override
    public RequestsAdapter.NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_layout_row,parent,false);
        return new RequestsAdapter.NotificationVH(itemView);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.NotificationVH holder, int position) {
        if(mResponse.getResult().size() == 0)
        {
//            Toast.makeText(context, "No Records Found..!", Toast.LENGTH_SHORT).show();
        }
        else {
            final String userPic = mResponse.getResult().get(position).getUserPic();
            LoadImageFromUrl(context, APIConstants.IMAGE_URL + userPic, holder.visitorIV);
            final String VisitorName = mResponse.getResult().get(position).getVisitorName();
            holder.nameTV.setText("Visitor Name : "+VisitorName);
            final String location = mResponse.getResult().get(position).getVisitorLocation();
            holder.fromTV.setText("From : "+location);
            final String mobileNumber = mResponse.getResult().get(position).getMobileNumber();
            holder.mobilenumberTV.setText("Mobile Number : "+mobileNumber);
            final String inTime = mResponse.getResult().get(position).getInTime();
            holder.durationtv.setText("In-Time : "+inTime);
            final int visit_id = mResponse.getResult().get(position).getVisit_id();
            SharedPreferences pref = context.getSharedPreferences("VisitorSignUpDetails", 0); // 0 - for private mode
            int SecId = pref.getInt("sec_id", 0);
//        int visit_id = pref.getInt("visit_id", 0);
            String mobile_number = pref.getString("mobile_number","N/A");
            String visitor_name = pref.getString("visitor_name", "N/A");
            Log.i(TAG, "onBindViewHolder: SecId: "+SecId+"\n"+mobile_number+"\n"+visitor_name);
            // approved-0,pre_approved-1,denied-2
            holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateVisitorStatusAPI(0,visit_id);
                    Toast.makeText(context,"Request Accepted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context,SecurityNavActivity.class);
                    context.startActivity(i);
                }
            });
            holder.denyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateVisitorStatusAPI(2,visit_id);
                    Toast.makeText(context,"Request Denied", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context,SecurityNavActivity.class);
                    context.startActivity(i);
                }
            });
            holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    @Override
    public int getItemCount()
    {
        int size = -1;
        if(mResponse.getResult().size() == 0)
        {
//            Toast.makeText(context, "No Records Found..!", Toast.LENGTH_SHORT).show();
        }
        else {

        }

        if(mResponse.getResult() != null && mResponse.getResult().size() > 0)
        {
            size = mResponse.getResult().size();
        }
        return size;

    }
    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView) {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }



    public class NotificationVH extends RecyclerView.ViewHolder {
        public TextView nameTV,fromTV,mobilenumberTV,durationtv,requestedTV;
        public de.hdodenhof.circleimageview.CircleImageView visitorIV;
        public Button acceptBtn,denyBtn;
        public ImageView cancelBtn;

        public NotificationVH(@NonNull View itemView) {
            super(itemView);
            visitorIV=itemView.findViewById(R.id.visitorIV);
            nameTV=itemView.findViewById(R.id.nameTV);
            fromTV=itemView.findViewById(R.id.fromTV);
            mobilenumberTV=itemView.findViewById(R.id.mobilenumberTV);
            durationtv=itemView.findViewById(R.id.durationtv);
            requestedTV=itemView.findViewById(R.id.requestedTV);
            acceptBtn=itemView.findViewById(R.id.acceptBtn);
            denyBtn=itemView.findViewById(R.id.denyBtn);
            cancelBtn=itemView.findViewById(R.id.cancelBtn);
        }
    }





    private void UpdateVisitorStatusAPI(int status,int visit_id) {

        JsonObject object = UpdateStatusObject(status,visit_id);
        IPermitService service = ServiceFactory.createRetrofitService(context, IPermitService.class);
        mSubscription = service.VisitorStatus(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VisitorStatusResponseModel>() {
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
                    public void onNext(VisitorStatusResponseModel mResponse)
                    {
                        if(mResponse.getStatus() == 1)
                        {
                            Intent intent = new Intent(context, SecurityNavActivity.class);
                            context.startActivity(intent);
                        }
                        else {

                        }
                    }

                });
    }



    /**
     * Json Object of UpdateStatusObject
     *
     * @return
     * @param status
     * @param visit_id
     */
    private JsonObject UpdateStatusObject(int status, int visit_id) {
        VisitorStatusRequestModel mRequest = new VisitorStatusRequestModel();
        mRequest.setVisitId(visit_id);
        mRequest.setStatus(status);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }



}
