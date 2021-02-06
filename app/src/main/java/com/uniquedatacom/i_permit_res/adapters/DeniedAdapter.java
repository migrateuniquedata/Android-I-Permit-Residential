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
import com.uniquedatacom.i_permit_res.models.EmpDeniedListResponseModel;
import com.uniquedatacom.i_permit_res.services.APIConstants;

import java.util.ArrayList;
import java.util.List;

public class DeniedAdapter extends RecyclerView.Adapter<DeniedAdapter.NotificationVH>  implements Filterable {
    // public List<NotificationModel> NotificationModelArrayList;
    Context context;
    EmpDeniedListResponseModel mResponse;
    public List<EmpDeniedListResponseModel> notificationResponseModelList = new ArrayList<>();
    private List<EmpDeniedListResponseModel.Result> deniedFilteredList;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public DeniedAdapter(Context context, EmpDeniedListResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
        this.deniedFilteredList = mResponse.getResult();
    }

    @NonNull
    @Override
    public NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_approved_layout,parent,false);
        return new NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationVH holder, int position) {
        final String inTime = deniedFilteredList.get(position).getInTime();
        final String mobileNumber = deniedFilteredList.get(position).getMobileNumber();
        final String location = deniedFilteredList.get(position).getVisitorLocation();
        final String name = deniedFilteredList.get(position).getVisitorName();
        final String userPic = deniedFilteredList.get(position).getUserPic();
        holder.dateTV.setText("Date: "+inTime);
        holder.intimeTV.setText("InTime: "+inTime);
        holder.mobilenumberTV.setText("Mobile Number: "+mobileNumber);
        holder.fromTV.setText("Location: "+location);
        holder.nameTV.setText("Name: "+name);
        LoadImageFromUrl(context, APIConstants.IMAGE_URL+userPic,holder.profileimageIV);
        String inTime1 = inTime;
        System.out.println("inTime Date: " + inTime1.substring(0, inTime1.indexOf(' ')));
        System.out.println("inTime Time: " + inTime1.substring(inTime1.indexOf(' ') + 1));
        String date =  inTime1.substring(0, inTime1.indexOf(' '));
        String intime =  inTime1.substring(inTime1.indexOf(' ') + 1);
        holder.intimeTV.setText("In-Time: "+intime);
        holder.dateTV.setText("Date: "+date+"");
        String whomToMeet = deniedFilteredList.get(position).getWhomToMeet();
        if(whomToMeet.equals(""))
        {
            holder.whomtomeetTV.setVisibility(View.INVISIBLE);
        }
        else {
            holder.whomtomeetTV.setText("Whom to meet: " + whomToMeet);
            holder.whomtomeetTV.setVisibility(View.VISIBLE);
        }
        String temp = deniedFilteredList.get(position).getBodyTemperature();
        if(temp.equals(""))
        {
            holder.TempTv.setVisibility(View.INVISIBLE);
        }
        else {
            holder.TempTv.setText("Temp: " + temp);
            holder.TempTv.setVisibility(View.VISIBLE);
        }
        int mask = deniedFilteredList.get(position).getMask();
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

        if(deniedFilteredList != null && deniedFilteredList.size() > 0)
        {
            size = deniedFilteredList.size();
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
                    deniedFilteredList = mResponse.getResult();
                } else {
                    List<EmpDeniedListResponseModel.Result> filteredList = new ArrayList<>();
                    for (EmpDeniedListResponseModel.Result row :  mResponse.getResult()) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getVisitorMobileNumber().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
//                            filteredList.add(row);
//                        }
                        if (row.getMobileNumber().contains(charSequence) || row.getVisitorName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    deniedFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = deniedFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                deniedFilteredList = (ArrayList<EmpDeniedListResponseModel.Result>) filterResults.values;
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
            durationTV.setVisibility(View.GONE);
            whomtomeetTV=itemView.findViewById(R.id.whomtomeetTV);
            outtimeTV=itemView.findViewById(R.id.outtimeTV);
            outtimeTV.setVisibility(View.INVISIBLE);
            intimeTV=itemView.findViewById(R.id.intimeTV);
            TempTv=itemView.findViewById(R.id.TempTv);
            insideorleftTv=itemView.findViewById(R.id.insideorleftTv);
            insideorleftTv.setVisibility(View.INVISIBLE);
            preapprovedTV=itemView.findViewById(R.id.preapprovedTV);
            preapprovedTV.setText("Denied");
            preapprovedTV.setTextColor(Color.parseColor("#cc3300"));
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
