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
import com.uniquedatacom.i_permit_res.models.ActivityVisitorsEmpResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class ActivityVisitorsAdapter extends RecyclerView.Adapter<ActivityVisitorsAdapter.NotificationVH> implements Filterable {
    // public List<NotificationModel> NotificationModelArrayList;
    Context context;
    ActivityVisitorsEmpResponseModel mResponse;
    public List<ActivityVisitorsEmpResponseModel> preApprovedListResponseModelArrayList = new ArrayList<>();
    private Subscription mSubscription;
    private String TAG = "ActivityVisitorsAdapter";
    private List<ActivityVisitorsEmpResponseModel.Result> visitorsFilteredList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ActivityVisitorsAdapter(Context context, ActivityVisitorsEmpResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
        this.visitorsFilteredList = mResponse.getResult();
        Log.i(TAG, "ActivityVisitorsAdapter: mresponse" + mResponse);
    }

    @NonNull
    @Override
    public ActivityVisitorsAdapter.NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_approved_layout, parent, false);
        return new ActivityVisitorsAdapter.NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ActivityVisitorsAdapter.NotificationVH holder, int position) {
        String Intime = visitorsFilteredList.get(position).getInTime();
//        try {
//            String originalString ="2020/09/14";
//            Date simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(originalString);
//            String newstr = new SimpleDateFormat("dd/MM/yyyy, H:mm:ss").format(simpleDate);
//            String onlyDate = new SimpleDateFormat("dd/MM/yyyy").format(simpleDate);
//            String onlyInTime = new SimpleDateFormat("H:mm:ss").format(simpleDate);
//            System.out.println("\n"+newstr+"\n");
//            System.out.println("\n"+onlyDate+"\n");
//            System.out.println("\n"+onlyInTime+"\n");
//            holder.dateTV.setText("Date: " + onlyDate);
//            holder.intimeTV.setText("In Time: " + onlyInTime);
//        }
//        catch (ParseException e) {
//            //Handle exception here
//            e.printStackTrace();
//        }
        String inTime1 = Intime;
        System.out.println("inTime Date: " + inTime1.substring(0, inTime1.indexOf(' ')));
        System.out.println("inTime Time: " + inTime1.substring(inTime1.indexOf(' ') + 1));
        String date =  inTime1.substring(0, inTime1.indexOf(' '));
        String intime =  inTime1.substring(inTime1.indexOf(' ') + 1);
        holder.intimeTV.setText("In-Time: "+intime);
        holder.dateTV.setText("Date: "+date+"");
        String visitorName = visitorsFilteredList.get(position).getVisitorName();
        holder.nameTV.setText("Visitor Name: " + visitorName);
        String location = visitorsFilteredList.get(position).getVisitorLocation();
        holder.fromTV.setText("From: " + location);
        String visitorMobileNumber = visitorsFilteredList.get(position).getVisitorMobileNumber();
        String MobileNumber = visitorsFilteredList.get(position).getMobileNumber();
        if(visitorMobileNumber.equals(""))
        {
            holder.mobilenumberTV.setText("Mobile Number: " + MobileNumber);
        }
        else {
            holder.mobilenumberTV.setText("Mobile Number: " + visitorMobileNumber);
        }

        String duration = visitorsFilteredList.get(position).getDuration();
        if(duration.equals(""))
        {
            holder.durationTV.setVisibility(View.GONE);
        }
        else {
            holder.durationTV.setText("Duration: " + duration);
            holder.durationTV.setVisibility(View.VISIBLE);
        }
//        String outtime = visitorsFilteredList.get(position).getOutgoingtime();
//        if(outtime.equals(""))
//        {
//            holder.outtimeTV.setVisibility(View.INVISIBLE);
//        }
//        else {
//            String outTime = outtime;
//            System.out.println("inTime Date: " + outTime.substring(0, outTime.indexOf(' ')));
//            System.out.println("inTime Time: " + outTime.substring(outTime.indexOf(' ') + 1));
//            String date1 =  outTime.substring(0, outTime.indexOf(' '));
//            String outTiem1 =  outTime.substring(outTime.indexOf(' ') + 1);
//            holder.outtimeTV.setText("Out-time: " + outTiem1);
//            holder.outtimeTV.setVisibility(View.VISIBLE);
//        }

        String outtime = visitorsFilteredList.get(position).getOutgoingtime();
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
            String date1 = outTime.substring(0, outTime.indexOf(' '));
            String outTiem1 = outTime.substring(outTime.indexOf(' ') + 1);
            holder.outtimeTV.setText("Out-time: " + outTiem1);
            holder.outtimeTV.setVisibility(View.VISIBLE);
            holder.insideorleftTv.setVisibility(View.VISIBLE);
            holder.insideorleftTv.setText("LEFT");
            holder.insideorleftTv.setBackgroundResource(R.drawable.gray_btn_bg);
        }

        String whomToMeet = visitorsFilteredList.get(position).getWhomToMeet();
        if(whomToMeet.equals(""))
        {
            holder.whomtomeetTV.setVisibility(View.INVISIBLE);
        }
        else {
            holder.whomtomeetTV.setText("Whom to meet: " + whomToMeet);
            holder.whomtomeetTV.setVisibility(View.VISIBLE);
        }
        String user_pic = visitorsFilteredList.get(position).getUserPic();
        LoadImageFromUrl(context, APIConstants.IMAGE_URL+user_pic,holder.profileimageIV);
        String temp = visitorsFilteredList.get(position).getBody_temperature();
        if(temp.equals(""))
        {
            holder.TempTv.setVisibility(View.INVISIBLE);
        }
        else {
            holder.TempTv.setText("Temp: " + temp);
            holder.TempTv.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount()
    {
        int size = 0;
        if(visitorsFilteredList != null && visitorsFilteredList.size() > 0)
        {
            size = visitorsFilteredList.size();
            Log.i(TAG, "getItemCount: "+size);
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
                    visitorsFilteredList = mResponse.getResult();
                } else {
                    List<ActivityVisitorsEmpResponseModel.Result> filteredList = new ArrayList<>();
                    for (ActivityVisitorsEmpResponseModel.Result row :  mResponse.getResult()) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getVisitorMobileNumber().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
//                            filteredList.add(row);
//                        }
                        if (row.getVisitorMobileNumber().contains(charSequence) || row.getVisitorName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    visitorsFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = visitorsFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                visitorsFilteredList = (ArrayList<ActivityVisitorsEmpResponseModel.Result>) filterResults.values;
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
        public TextView dateTV, intimeTV, outtimeTV, nameTV, fromTV, mobilenumberTV, durationTV, whomtomeetTV,preapprovedTV,TempTv,insideorleftTv;
        public de.hdodenhof.circleimageview.CircleImageView profileimageIV;

        public NotificationVH(@NonNull View itemView) {
            super(itemView);
            profileimageIV = itemView.findViewById(R.id.profileimageIV);
            dateTV = itemView.findViewById(R.id.dateTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            fromTV = itemView.findViewById(R.id.fromTV);
            mobilenumberTV = itemView.findViewById(R.id.mobilenumberTV);
            durationTV = itemView.findViewById(R.id.durationTV);
            whomtomeetTV = itemView.findViewById(R.id.whomtomeetTV);
            whomtomeetTV.setVisibility(View.GONE);
            outtimeTV = itemView.findViewById(R.id.outtimeTV);
            intimeTV = itemView.findViewById(R.id.intimeTV);
            TempTv=itemView.findViewById(R.id.TempTv);
            insideorleftTv=itemView.findViewById(R.id.insideorleftTv);
            preapprovedTV = itemView.findViewById(R.id.preapprovedTV);
            preapprovedTV.setText("Visitors");
            preapprovedTV.setTextColor(Color.parseColor("#002699"));
        }
    }

}