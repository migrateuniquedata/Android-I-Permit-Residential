package com.uniquedatacom.i_permit_res.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.uniquedatacom.i_permit_res.models.SecAcceptResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;

import java.util.ArrayList;
import java.util.List;

public class SecAcceptAdapter extends RecyclerView.Adapter<SecAcceptAdapter.NotificationVH> implements Filterable {
    // public List<NotificationModel> NotificationModelArrayList;
    Context context;
    SecAcceptResponseModel mResponse;
    public List<SecAcceptResponseModel> notificationResponseModelList = new ArrayList<>();
    private List<SecAcceptResponseModel.Result> approvedFilteredList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public SecAcceptAdapter(Context context, SecAcceptResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
        this.approvedFilteredList = mResponse.getResult();
    }

    @NonNull
    @Override
    public SecAcceptAdapter.NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_approved_layout,parent,false);
        return new SecAcceptAdapter.NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull SecAcceptAdapter.NotificationVH holder, int position) {
        String inTime = approvedFilteredList.get(position).getInTime();
        String mobileNumber = approvedFilteredList.get(position).getMobileNumber();
        String userPic = approvedFilteredList.get(position).getUserPic();
        String duration = approvedFilteredList.get(position).getDuration();
        LoadImageFromUrl(context, APIConstants.IMAGE_URL+userPic,holder.profileimageIV);
        String location = approvedFilteredList.get(position).getVisitorLocation();
        String name = approvedFilteredList.get(position).getVisitorName();
        String inTime1 = inTime;
        System.out.println("inTime Date: " + inTime1.substring(0, inTime1.indexOf(' ')));
        System.out.println("inTime Time: " + inTime1.substring(inTime1.indexOf(' ') + 1));
        String date =  inTime1.substring(0, inTime1.indexOf(' '));
        String intime =  inTime1.substring(inTime1.indexOf(' ') + 1);
        holder.intimeTV.setText("In-Time: "+intime);
        holder.dateTV.setText("Date: "+date+"");
        holder.mobilenumberTV.setText("Mobile Number: "+mobileNumber);
        holder.fromTV.setText("From: "+location);
        holder.nameTV.setText("Name: "+name);
        holder.durationTV.setText("Duration: "+duration);
        String temp = approvedFilteredList.get(position).getBodyTemperature();
        if(temp.equals(""))
        {
            holder.TempTv.setVisibility(View.INVISIBLE);
        }
        else {
            holder.TempTv.setText("Temp: " + temp);
            holder.TempTv.setVisibility(View.VISIBLE);
        }
        String outtime = approvedFilteredList.get(position).getOutgoingtime();
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

        String whomToMeet = approvedFilteredList.get(position).getWhomToMeet();
        if(whomToMeet.equals(""))
        {
            holder.whomtomeetTV.setVisibility(View.INVISIBLE);
        }
        else {
            holder.whomtomeetTV.setText("Whom to meet: " + whomToMeet);
            holder.whomtomeetTV.setVisibility(View.VISIBLE);
        }
        int mask = approvedFilteredList.get(position).getMask();
        if(mask ==0)
        {
            holder.maskTv.setText("Mask : Yes");
        }
        else {
            holder.maskTv.setText("Mask : No");
        }
    }


    @Override
    public int getItemCount()
    {
        int size = 0;

        if(approvedFilteredList != null && approvedFilteredList.size() > 0)
        {
            size = approvedFilteredList.size();
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
                    approvedFilteredList = mResponse.getResult();
                } else {
                    List<SecAcceptResponseModel.Result> filteredList = new ArrayList<>();
                    for (SecAcceptResponseModel.Result row :  mResponse.getResult()) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getVisitorMobileNumber().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
//                            filteredList.add(row);
//                        }
                        if (row.getMobileNumber().contains(charSequence) || row.getVisitorName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    approvedFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = approvedFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                approvedFilteredList = (ArrayList<SecAcceptResponseModel.Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }






    public class NotificationVH extends RecyclerView.ViewHolder {
        public TextView dateTV,intimeTV,outtimeTV,nameTV,fromTV,mobilenumberTV,durationTV,whomtomeetTV,preapprovedTV,TempTv,insideorleftTv,maskTv;
        public de.hdodenhof.circleimageview.CircleImageView profileimageIV;

        public NotificationVH(@NonNull View itemView) {
            super(itemView);
            profileimageIV=itemView.findViewById(R.id.profileimageIV);
            dateTV=itemView.findViewById(R.id.dateTV);
            maskTv=itemView.findViewById(R.id.maskTV);
            nameTV=itemView.findViewById(R.id.nameTV);
            fromTV=itemView.findViewById(R.id.fromTV);
            mobilenumberTV=itemView.findViewById(R.id.mobilenumberTV);
            durationTV=itemView.findViewById(R.id.durationTV);
            whomtomeetTV=itemView.findViewById(R.id.whomtomeetTV);
            outtimeTV=itemView.findViewById(R.id.outtimeTV);
            intimeTV=itemView.findViewById(R.id.intimeTV);
            TempTv=itemView.findViewById(R.id.TempTv);
            insideorleftTv=itemView.findViewById(R.id.insideorleftTv);
            preapprovedTV=itemView.findViewById(R.id.preapprovedTV);
            preapprovedTV.setText("Approved");
            preapprovedTV.setTextColor(Color.parseColor("#00b33c"));
        }
    }


    /**
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView) {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }

}
