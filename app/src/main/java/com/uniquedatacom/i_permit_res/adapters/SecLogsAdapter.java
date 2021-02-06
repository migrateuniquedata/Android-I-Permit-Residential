package com.uniquedatacom.i_permit_res.adapters;

import android.content.Context;
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
import com.uniquedatacom.i_permit_res.models.SecLogsResponseModel;

import java.util.ArrayList;
import java.util.List;

public class SecLogsAdapter extends RecyclerView.Adapter<SecLogsAdapter.NotificationVH> implements Filterable {

    Context context;
    SecLogsResponseModel mResponse;
    public List<SecLogsResponseModel> empLogsResponseModelList = new ArrayList<>();
    private List<SecLogsResponseModel.Result> secLogsFilteredList;
    //declare interface
    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public SecLogsAdapter(Context context, SecLogsResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
        this.secLogsFilteredList = mResponse.getResult();
    }

    @NonNull
    @Override
    public SecLogsAdapter.NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.visitors_list_row,parent,false);
        return new SecLogsAdapter.NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull SecLogsAdapter.NotificationVH holder, int position) {
        final String Name = secLogsFilteredList.get(position).getVisitorName();
        final String from = secLogsFilteredList.get(position).getVisitorLocation();
        final String inTime = secLogsFilteredList.get(position).getCreatedTime();

        holder.nameTv.setText(Name);
        holder.FromTv.setText(from);
        holder.inTimeTv.setText(inTime);
        String date = inTime;
        System.out.println("Date: " + date.substring(0, date.indexOf(' ')));
        System.out.println("Time: " + date.substring(date.indexOf(' ') + 1));
        holder.nameTv.setText(Name);
        holder.FromTv.setText(from);
//        holder.inTimeTv.setText(inTime);
        holder.inTimeTv.setText(date.substring(date.indexOf(' ') + 1));

//        holder.infoIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClick.onItemClick(position);
//            }
//        });

    }




    /**
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView)
    {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }


    @Override
    public int getItemCount()
    {
        int size = 0;

        if(secLogsFilteredList != null && secLogsFilteredList.size() > 0)
        {
            size = secLogsFilteredList.size();
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
                    secLogsFilteredList = mResponse.getResult();
                } else {
                    List<SecLogsResponseModel.Result> filteredList = new ArrayList<>();
                    for (SecLogsResponseModel.Result row :  mResponse.getResult()) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getVisitorMobileNumber().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
//                            filteredList.add(row);
//                        }
                        if (row.getVisitorMobileNumber().contains(charSequence) || row.getVisitorName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    secLogsFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = secLogsFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                secLogsFilteredList = (ArrayList<SecLogsResponseModel.Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class NotificationVH extends RecyclerView.ViewHolder {
        public TextView nameTv,FromTv,inTimeTv;
        public ImageView infoIV;

        public NotificationVH(@NonNull View itemView) {
            super(itemView);
            nameTv=itemView.findViewById(R.id.nameTv);
            FromTv=itemView.findViewById(R.id.FromTv);
            inTimeTv=itemView.findViewById(R.id.inTimeTv);
            infoIV=itemView.findViewById(R.id.infoIV);
        }
    }

}

