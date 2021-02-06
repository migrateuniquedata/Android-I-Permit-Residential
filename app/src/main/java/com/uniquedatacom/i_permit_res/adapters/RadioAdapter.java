package com.uniquedatacom.i_permit_res.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.models.LanguagesListResponseModel;


public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {
    public int mSelectedItem = -1;
    LanguagesListResponseModel mResponse;
    private Context mContext;
    private String TAG = "RadioAdapter";

    public RadioAdapter(Context context, LanguagesListResponseModel responseModel) {
        mContext = context;
        mResponse = responseModel;
    }

    @Override
    public void onBindViewHolder(RadioAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.mRadio.setChecked(i == mSelectedItem);
        viewHolder.mRadio.setText(mResponse.getResult().get(i).getName());
        Log.i(TAG, "onBindViewHolder: "+mResponse.getResult().get(i).getName());
//        viewHolder.mRadio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewHolder.mRadio.setChecked(true);
//                Log.i(TAG, "onClick: "+mResponse.getResult().get(i).getName());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mResponse.getResult().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.languages_row, viewGroup, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public RadioButton mRadio;
//        public TextView mText;

        public ViewHolder(final View inflate) {
            super(inflate);
//            mText = (TextView) inflate.findViewById(R.id.text);
            mRadio = (RadioButton) inflate.findViewById(R.id.radioBtn);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    Log.i(TAG, "onClick: "+mSelectedItem + "-->"+mRadio.getText().toString());
                    SharedPreferences pref = mContext.getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Language",mResponse.getResult().get(mSelectedItem).getName());
                    editor.putString("LangCode",mResponse.getResult().get(mSelectedItem).getCode());
                    editor.apply();
                    editor.commit();
                    notifyDataSetChanged();
                }
            };
//            itemView.setOnClickListener(clickListener);
//            mRadio.setOnClickListener(clickListener);
        }
    }
}