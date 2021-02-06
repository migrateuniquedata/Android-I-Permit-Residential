package com.uniquedatacom.i_permit_res.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.activities.RequestsScreen;
import com.uniquedatacom.i_permit_res.models.EmpNotificationsResponseModel;

import java.util.ArrayList;
import java.util.List;

public class EmployeeNotificationAdapter extends RecyclerView.Adapter<EmployeeNotificationAdapter.NotificationVH> {
    private static final String TAG = "EmployeeNotificationAdapter";
    // public List<NotificationModel> NotificationModelArrayList;
    Context context;
    EmpNotificationsResponseModel mResponse;
    public List<EmpNotificationsResponseModel> notificationResponseModelList = new ArrayList<>();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public EmployeeNotificationAdapter(Context context, EmpNotificationsResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
    }

    @NonNull
    @Override
    public NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list,parent,false);
        return new NotificationVH(itemView);

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull NotificationVH holder, int position) {
        final String messageBody = mResponse.getResult().get(position).getMessageBody();
        holder.notificationTv.setText(messageBody);
        final String time = mResponse.getResult().get(position).getTime();
        String[] parts = time.split("\\s");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
        Log.i(TAG, "onBindViewHolder: "+part1 + "-->"+part2);
        holder.notificationTime.setText(part2+"");

        holder.notificationCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageBody.contains("left"))
                {

                }
                else if(messageBody.contains("entered"))
                {

                }
                else {
                    Intent i = new Intent(context, RequestsScreen.class);
                    context.startActivity(i);
                }

            }
        });
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
        public TextView notificationTv,notificationTime;
        public CardView notificationCV;

        public NotificationVH(@NonNull View itemView) {
            super(itemView);
            notificationTv=itemView.findViewById(R.id.notificationTv);
            notificationTime=itemView.findViewById(R.id.notificationTime);
            notificationCV=itemView.findViewById(R.id.notificationCV);
        }
    }
}
