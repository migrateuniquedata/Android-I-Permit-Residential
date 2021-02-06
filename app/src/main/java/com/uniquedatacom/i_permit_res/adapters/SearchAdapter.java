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

import com.squareup.picasso.Picasso;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.SearchResponseModel;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.NotificationVH> {

    Context context;
    SearchResponseModel mResponse;
    public List<SearchResponseModel> searchResponseModelArrayList = new ArrayList<>();


    public SearchAdapter(Context context, SearchResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;

    }

    @NonNull
    @Override
    public SearchAdapter.NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.visitors_list_row,parent,false);
        return new SearchAdapter.NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.NotificationVH holder, int position) {
        final String Name = mResponse.getResults().get(position).getVisitorName();
        final String from = mResponse.getResults().get(position).getVisitorLocation();
        final String inTime = mResponse.getResults().get(position).getDate();

        holder.nameTv.setText(Name);
        holder.FromTv.setText(from);
        holder.inTimeTv.setText(inTime);
        holder.infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.visitor_details_alert);
                dialog.setTitle("Position" + position);
                dialog.setCancelable(true);
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
    public int getItemCount() {
        return mResponse.getResults().size();
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


