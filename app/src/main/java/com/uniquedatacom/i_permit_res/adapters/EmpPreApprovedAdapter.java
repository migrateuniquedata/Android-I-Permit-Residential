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
import com.uniquedatacom.i_permit_res.models.EmpPreApprovedResponseModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class EmpPreApprovedAdapter extends RecyclerView.Adapter<EmpPreApprovedAdapter.NotificationVH> implements Filterable
{
    // public List<NotificationModel> NotificationModelArrayList;
    Context context;
    EmpPreApprovedResponseModel mResponse;
    public List<EmpPreApprovedResponseModel> preApprovedListResponseModelArrayList= new ArrayList<>();
    private Subscription mSubscription;
    private String TAG = "EmpPreApprovedAdapter";
    private List<EmpPreApprovedResponseModel.Result> preapprovedFilteredList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public EmpPreApprovedAdapter(Context context, EmpPreApprovedResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
        this.preapprovedFilteredList = mResponse.getResult();
        Log.i(TAG, "EmpPreApprovedAdapter: mresponse"+mResponse);
    }

    @NonNull
    @Override
    public EmpPreApprovedAdapter.NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_approved,parent,false);
        return new EmpPreApprovedAdapter.NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull EmpPreApprovedAdapter.NotificationVH holder, int position) {
        String date1 = mResponse.getResult().get(position).getDate();
        holder.dateTV.setText("Date: "+date1);
        String visitorName = mResponse.getResult().get(position).getVisitorName();
        holder.nameTV.setText("Visitor Name: "+visitorName);
        String location = mResponse.getResult().get(position).getVisitorLocation();
        holder.fromTV.setText("From: "+location);
        String fromTime = mResponse.getResult().get(position).getInTime();
        holder.intimeTV.setText("In Time: "+fromTime);
        String mobile = mResponse.getResult().get(position).getVisitorMobileNumber();
        holder.mobilenumberTV.setText("Mobile Number: "+mobile);
        String duration = mResponse.getResult().get(position).getDuration();
        String inTime1 = fromTime;
        System.out.println("inTime Date: " + inTime1.substring(0, inTime1.indexOf(' ')));
        System.out.println("inTime Time: " + inTime1.substring(inTime1.indexOf(' ') + 1));
        String date =  inTime1.substring(0, inTime1.indexOf(' '));
        String intime =  inTime1.substring(inTime1.indexOf(' ') + 1);
        holder.intimeTV.setText("In-Time: "+intime);
        holder.dateTV.setText("Date: "+date);
//        holder.durationTV.setText("Duration: "+duration);
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
        String outTime = mResponse.getResult().get(position).getOutgoingtime();
        if(outTime.equals(""))
        {
            holder.outtimeTV.setVisibility(View.INVISIBLE);
        }
        else {
            holder.outtimeTV.setText("Out-time: " + outTime);
            holder.outtimeTV.setVisibility(View.VISIBLE);
        }
        String temp = mResponse.getResult().get(position).getBody_temperature();
        if(temp.equals(""))
        {
            holder.TempTv.setVisibility(View.INVISIBLE);
        }
        else {
            holder.TempTv.setText("Temp: " + temp);
            holder.TempTv.setVisibility(View.VISIBLE);
        }

//        if(outTime.equals(""))
//        {
//            holder.outtimeTV.setVisibility(View.INVISIBLE);
//
//        }
//        else {
//            System.out.println("inTime Date: " + outTime.substring(0, outTime.indexOf(' ')));
//            System.out.println("inTime Time: " + outTime.substring(outTime.indexOf(' ') + 1));
//            String date2 = outTime.substring(0, outTime.indexOf(' '));
//            String outTiem1 = outTime.substring(outTime.indexOf(' ') + 1);
//            holder.outtimeTV.setText("Out-time: " + outTiem1);
//            holder.outtimeTV.setVisibility(View.VISIBLE);
//            holder.insideorleftTv.setVisibility(View.VISIBLE);
//            holder.insideorleftTv.setText("LEFT");
//            holder.insideorleftTv.setBackgroundResource(R.drawable.gray_btn_bg);
//        }
        boolean verify_status = mResponse.getResult().get(position).isVerify_status();
        if(verify_status == false)
        {
            holder.insideorleftTv.setVisibility(View.INVISIBLE);
        }
        else {
            holder.insideorleftTv.setVisibility(View.VISIBLE);
            holder.insideorleftTv.setText("INSIDE");
            holder.insideorleftTv.setBackgroundResource(R.drawable.green_button_bg);
        }


        if(outTime.equals(""))
        {
            holder.outtimeTV.setVisibility(View.INVISIBLE);
            holder.insideorleftTv.setVisibility(View.VISIBLE);
            holder.insideorleftTv.setText("INSIDE");
            holder.insideorleftTv.setBackgroundResource(R.drawable.green_button_bg);
        }
        else {
//            String outTime = outtime;
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
    }


    @Override
    public int getItemCount()
    {
        int size = 0;

        if(preapprovedFilteredList != null && preapprovedFilteredList.size() > 0)
        {
            size = preapprovedFilteredList.size();
        }
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
                    List<EmpPreApprovedResponseModel.Result> filteredList = new ArrayList<>();
                    for (EmpPreApprovedResponseModel.Result row :  mResponse.getResult()) {

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
                preapprovedFilteredList = (ArrayList<EmpPreApprovedResponseModel.Result>) filterResults.values;
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
            profileimageIV=itemView.findViewById(R.id.profileimageIV);
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
            preapprovedTV=itemView.findViewById(R.id.preapprovedTV);
            preapprovedTV.setText("Pre-Approved");
            preapprovedTV.setTextColor(Color.parseColor("#00b33c"));
        }
    }


}

