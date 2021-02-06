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
import com.uniquedatacom.i_permit_res.models.EmpLogsResponseModel;

import java.util.ArrayList;
import java.util.List;

public class EmpLogsAdapter extends RecyclerView.Adapter<EmpLogsAdapter.NotificationVH> implements Filterable {

    Context context;
    EmpLogsResponseModel mResponse;
    public List<EmpLogsResponseModel> empLogsResponseModelList = new ArrayList<>();
    private List<EmpLogsResponseModel.Result> employeeLoginResponseModelListFiltered;
    //     public interface visitors{
//        void showDialog(int pos, VisitorDetailsListResponseModel mResponse);
//    }
    public EmpLogsAdapter(Context context, EmpLogsResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
        this.employeeLoginResponseModelListFiltered = mResponse.getResult();
    }

    @NonNull
    @Override
    public EmpLogsAdapter.NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.visitors_list_row,parent,false);
        return new EmpLogsAdapter.NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull EmpLogsAdapter.NotificationVH holder, int position) {
        final String Name = employeeLoginResponseModelListFiltered.get(position).getVisitorName();
        final String from = employeeLoginResponseModelListFiltered.get(position).getVisitorLocation();
        final String inTime = employeeLoginResponseModelListFiltered.get(position).getCreatedTime();

        String date = inTime;
        System.out.println("Date: " + date.substring(0, date.indexOf(' ')));
        System.out.println("Time: " + date.substring(date.indexOf(' ') + 1));
        holder.nameTv.setText(Name);
        holder.FromTv.setText(from);
//        holder.inTimeTv.setText(inTime);
        holder.inTimeTv.setText(date.substring(date.indexOf(' ') + 1));


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

        if(employeeLoginResponseModelListFiltered!= null && employeeLoginResponseModelListFiltered.size() > 0)
        {
            size = employeeLoginResponseModelListFiltered.size();
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
                    employeeLoginResponseModelListFiltered = mResponse.getResult();
                } else {
                    List<EmpLogsResponseModel.Result> filteredList = new ArrayList<>();
                    for (EmpLogsResponseModel.Result row :  mResponse.getResult()) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getVisitorMobileNumber().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
//                            filteredList.add(row);
//                        }
                        if (row.getVisitorMobileNumber().contains(charSequence) || row.getVisitorName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    employeeLoginResponseModelListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = employeeLoginResponseModelListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                employeeLoginResponseModelListFiltered = (ArrayList<EmpLogsResponseModel.Result>) filterResults.values;
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

