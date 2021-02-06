package com.uniquedatacom.i_permit_res.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.VisitorDetailsListResponseModel;
import com.squareup.picasso.Picasso;
import com.uniquedatacom.i_permit_res.services.APIConstants;

import java.util.ArrayList;
import java.util.List;

public class VisitorsListAdapter extends RecyclerView.Adapter<VisitorsListAdapter.NotificationVH> {

    Context context;
    VisitorDetailsListResponseModel mResponse;
    public List<VisitorDetailsListResponseModel> visitorsResponseModelList = new ArrayList<>();

//     public interface visitors{
//        void showDialog(int pos, VisitorDetailsListResponseModel mResponse);
//    }
    public VisitorsListAdapter(Context context, VisitorDetailsListResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;

    }

    @NonNull
    @Override
    public NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.visitors_list_row,parent,false);
        return new NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationVH holder, int position) {
        final String Name = mResponse.getResult().get(position).getName();
        final String from = mResponse.getResult().get(position).getFrom();
        final String inTime = mResponse.getResult().get(position).getInTime();

        holder.nameTv.setText(Name);
        holder.FromTv.setText(from);
        holder.inTimeTv.setText(inTime);
        holder.infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DetailsDialog(v,position,mResponse);
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.visitor_details_alert);
                dialog.setTitle("Position" + position);
                dialog.setCancelable(true);
//                TextView name =(TextView)dialog.findViewById(R.id.name);
//                TextView address=(TextView)dialog.findViewById(R.id.address);
//                ImageView icon = (ImageView) dialog.findViewById(R.id.image);
//                setDataToView(name,address,icon,position);
                dialog.show();
            }
        });

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

        if(mResponse.getResult() != null && mResponse.getResult().size() > 0)
        {
            size = mResponse.getResult().size();
        }
        return size;

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


    private void DetailsDialog(View v, int position, VisitorDetailsListResponseModel mResponse)
    {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.visitor_details_alert);
        dialog.setCanceledOnTouchOutside(true);

        ImageView ProfileIV = dialog.findViewById(R.id.ProfileIV);
        LoadImageFromUrl(context, APIConstants.IMAGE_URL+ this.mResponse.getResult().get(position).getUserPic(),ProfileIV);
        TextView intimeTv = dialog.findViewById(R.id.intimeTv);
        intimeTv.setText(mResponse.getResult().get(position).getInTime());
        TextView outTimeTv = dialog.findViewById(R.id.outTimeTv);
        outTimeTv.setText(mResponse.getResult().get(position).getOutTime());
        TextView nameTv = dialog.findViewById(R.id.nameTv);
        nameTv.setText(mResponse.getResult().get(position).getName());

        TextView fromTv = dialog.findViewById(R.id.fromTv);
        fromTv.setText(mResponse.getResult().get(position).getFrom());

        TextView mobileTv = dialog.findViewById(R.id.mobileTv);
        mobileTv.setText(mResponse.getResult().get(position).getMobileNumber());

        TextView durationTv = dialog.findViewById(R.id.durationTv);
        durationTv.setText(mResponse.getResult().get(position).getDuration());

        dialog.show();
    }
}
