package com.uniquedatacom.i_permit_res.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.GetSecurityPreApprovedListResponseModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class PreApprovedAdapter extends RecyclerView.Adapter<PreApprovedAdapter.NotificationVH> implements Filterable {
    // public List<NotificationModel> NotificationModelArrayList;
    Context context;
    GetSecurityPreApprovedListResponseModel mResponse;
    public List<GetSecurityPreApprovedListResponseModel> preApprovedListResponseModelArrayList= new ArrayList<>();
    private Subscription mSubscription;
    private String TAG = "PreApprovedAdapter";
    private List<GetSecurityPreApprovedListResponseModel.Result> preapprovedFilteredList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public PreApprovedAdapter(Context context, GetSecurityPreApprovedListResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
        this.preapprovedFilteredList = mResponse.getResult();
        Log.i(TAG, "PreApprovedAdapter: mresponse"+mResponse);
    }

    @NonNull
    @Override
    public PreApprovedAdapter.NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_approved,parent,false);
        return new PreApprovedAdapter.NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationVH holder, int position) {
        String date1 = preapprovedFilteredList.get(position).getDate();
        holder.dateTV.setText("Date: "+date1);
        String inTime1 = preapprovedFilteredList.get(position).getInTime();
        System.out.println("inTime Date: " + inTime1.substring(0, inTime1.indexOf(' ')));
        System.out.println("inTime Time: " + inTime1.substring(inTime1.indexOf(' ') + 1));
        String date =  inTime1.substring(0, inTime1.indexOf(' '));
        String intime =  inTime1.substring(inTime1.indexOf(' ') + 1);
        holder.intimeTV.setText("In-Time: "+intime);
        holder.dateTV.setText("Date: "+date+"");

        String visitorName = preapprovedFilteredList.get(position).getVisitorName();
        holder.nameTV.setText("Visitor Name: "+visitorName);
        String location = preapprovedFilteredList.get(position).getVisitorLocation();
        holder.fromTV.setText("From: "+location);
//       String fromTime = preapprovedFilteredList.get(position).getSelectTime();
//       holder.intimeTV.setText("In Time: "+fromTime);
//        String inTime = preapprovedFilteredList.get(position).getInTime();
//        holder.intimeTV.setText("In Time: "+inTime);
        String mobile = mResponse.getResult().get(position).getVisitorMobileNumber();
        holder.mobilenumberTV.setText("Mobile Number: "+mobile);
        String duration = mResponse.getResult().get(position).getDuration();
        if(mobile.equals(""))
        {
//            holder.mobilenumberTV.setText("Mobile Number: " + mobile);
        }
        else {
            holder.mobilenumberTV.setText("Mobile Number: " + mobile);
        }

        if(duration.equals(""))
        {
            holder.durationTV.setVisibility(View.GONE);
        }
        else {
            holder.durationTV.setText("Duration: " + duration);
            holder.durationTV.setVisibility(View.VISIBLE);
        }
//        String outTime = mResponse.getResult().get(position).getOutgoingtime();
//        if(outTime.equals(""))
//        {
//            holder.outtimeTV.setVisibility(View.INVISIBLE);
//        }
//        else {
//            holder.outtimeTV.setText("Out-time: " + outTime);
//            holder.outtimeTV.setVisibility(View.VISIBLE);
//        }
        String temp = mResponse.getResult().get(position).getBodyTemperature();
        if(temp.equals(""))
        {
            holder.TempTv.setVisibility(View.INVISIBLE);
        }
        else {
            holder.TempTv.setText("Temp: " + temp);
            holder.TempTv.setVisibility(View.VISIBLE);
        }
        String outtime = mResponse.getResult().get(position).getOutgoingtime();

        if(outtime.equals(""))
        {
            holder.outtimeTV.setVisibility(View.INVISIBLE);
            holder.insideorleftTv.setVisibility(View.VISIBLE);
            holder.insideorleftTv.setText("INSIDE");
            holder.insideorleftTv.setBackgroundResource(R.drawable.green_button_bg);
        }
        else {
            String outTime = outtime;
            System.out.println("inTime Date: " + outTime.substring(0, outTime.indexOf(' ')));
            System.out.println("inTime Time: " + outTime.substring(outTime.indexOf(' ') + 1));
            String date2 = outTime.substring(0, outTime.indexOf(' '));
            String outTiem1 = outTime.substring(outTime.indexOf(' ') + 1);
            holder.outtimeTV.setText("Out-time: " + outTiem1);
            holder.outtimeTV.setVisibility(View.VISIBLE);
            holder.insideorleftTv.setVisibility(View.VISIBLE);
            holder.insideorleftTv.setText("LEFT");
            holder.insideorleftTv.setBackgroundResource(R.drawable.gray_btn_bg);
        }
        Log.i(TAG, "onBindViewHolder: sagagduidgaugsadugaigdsai");
    }


    @Override
    public int getItemCount()
    {
        int size = 0;

        if(preapprovedFilteredList != null && preapprovedFilteredList.size() > 0)
        {
            size = preapprovedFilteredList.size();
        }
        Log.i(TAG, "PreApproved getItemCount: "+size);
        return size;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    preapprovedFilteredList = mResponse.getResult();
                } else {
                    List<GetSecurityPreApprovedListResponseModel.Result> filteredList = new ArrayList<>();
                    for (GetSecurityPreApprovedListResponseModel.Result row :  mResponse.getResult()) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getVisitorMobileNumber().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
//                            filteredList.add(row);
//                        }
                        if (row.getVisitorMobileNumber().contains(charSequence) || row.getVisitorName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    preapprovedFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = preapprovedFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                preapprovedFilteredList = (ArrayList<GetSecurityPreApprovedListResponseModel.Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
        public TextView dateTV,intimeTV,outtimeTV,nameTV,fromTV,mobilenumberTV,durationTV,whomtomeetTV,preapprovedTV,TempTv,insideorleftTv;
        public de.hdodenhof.circleimageview.CircleImageView profileimageIV;

        public NotificationVH(@NonNull View itemView) {
            super(itemView);
            dateTV=itemView.findViewById(R.id.dateTV);
            nameTV=itemView.findViewById(R.id.nameTV);
            fromTV=itemView.findViewById(R.id.fromTV);
            mobilenumberTV=itemView.findViewById(R.id.mobilenumberTV);
            durationTV=itemView.findViewById(R.id.durationTV);
            whomtomeetTV=itemView.findViewById(R.id.whomtomeetTV);
            whomtomeetTV.setVisibility(View.GONE);
            outtimeTV=itemView.findViewById(R.id.outtimeTV);
            intimeTV=itemView.findViewById(R.id.intimeTV);
            TempTv=itemView.findViewById(R.id.TempTv);
            insideorleftTv=itemView.findViewById(R.id.insideorleftTv);
            preapprovedTV = itemView.findViewById(R.id.preapprovedTV);
            preapprovedTV.setText("Pre-Approved");
            preapprovedTV.setTextColor(Color.parseColor("#00b33c"));
        }
    }


}
