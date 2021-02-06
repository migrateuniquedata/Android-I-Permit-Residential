package com.uniquedatacom.i_permit_res.activities.ui.community;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.uniquedatacom.i_permit_res.R;
import com.uniquedatacom.i_permit_res.activities.LanguagesSelectionScreen;
import com.uniquedatacom.i_permit_res.activities.SecurityNavActivity;
import com.uniquedatacom.i_permit_res.services.APIConstants;


public class CommunityFragment extends Fragment
{
    String TAG = "Logout";
    private int user_id;
    private String DEFAULT = "N/A";
    private String Mobile,Name,Email,UserPic;
    private String LangCode;
    private String UserType;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_community, container, false);
        GetSharedPrefs();
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("UserLoginType", 0); // 0 - for private mode
        UserType = preferences.getString("UserType", "N/A");
        Log.i(TAG, "GetSharedPrefs: UserType: " + UserType);
        SharedPreferences pref = getActivity().getSharedPreferences("LanguageSelected", 0); // 0 - for private mode
        String Language = pref.getString("Language","English");
        LangCode = pref.getString("LangCode","en");
        Log.i(TAG, "onCreate: "+Language);
        SetLogoutDialog();
        return root;
    }


    private void GetSharedPrefs()
    {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("LoginPref", 0);
        user_id = pref.getInt("user_id", 0);
        Mobile = pref.getString("mobile_number",DEFAULT);
        Name = pref.getString("name",DEFAULT);
        Email = pref.getString("email_id",DEFAULT);
        UserPic = pref.getString("user_pic",DEFAULT);
        Log.i(TAG, "GetSharedPrefs: "+user_id);
    }


    /**
     *
     * @param context
     * @param Title
     * @param Msg
     * @param ButtonName
     * @param runnable
     * @param secondButtonName
     * @param secondRunnable
     */
    public void DialogWithTwoButtons(Context context, String Title, String Msg, String ButtonName, final Runnable runnable, String secondButtonName, final Runnable secondRunnable)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_with_2buttons);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView title = (TextView) dialog.findViewById(R.id.TitleTv);
        title.setText(Title);
        if(title.getText().toString().equals(""))
        {
            title.setBackgroundColor(Color.WHITE);
        }
        TextView msg = (TextView) dialog.findViewById(R.id.DescTv);
        msg.setText(Msg);
        TextViewTranslation(LangCode,msg);
        TextViewTranslation(LangCode,title);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        okBtn.setText(ButtonName);
        ButtonTranslation(LangCode,okBtn);
        // if ok button is clicked, close the custom dialog
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.run();
                dialog.dismiss();
            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        cancelBtn.setText(secondButtonName);
        ButtonTranslation(LangCode,cancelBtn);
        // if decline button is clicked, close the custom dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondRunnable.run();
                dialog.dismiss();
            }
        });
    }


    private void SetLogoutDialog()
    {
        DialogWithTwoButtons(getActivity(), getString(R.string.Logout), getString(R.string.AreYouSure), getString(R.string.ok), new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(getActivity(), LanguagesSelectionScreen.class);
//                intent1.putExtra("UserType",UserType);
                startActivity(intent1);
                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
                settings.edit().remove("user_id").commit();
//                SharedPreferences Language = getActivity().getApplicationContext().getSharedPreferences("LanguageSelected", Context.MODE_PRIVATE);
//                Language.edit().remove("Language").commit();
            }
        }, getString(R.string.Cancel), new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getActivity(), SecurityNavActivity.class);
                startActivity(i);
            }
        });
    }


    private void TextViewTranslation(String languageCode, TextView button)
    {
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(APIConstants.LANGUAGE_API_KEY)
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(button.getText().toString(),
                                Translate.TranslateOption.targetLanguage(languageCode));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (button != null) {
                            button.setText(translation.getTranslatedText());
                        }

                    }
                });
                return null;
            }
        }.execute();
    }




    private void ButtonTranslation(String languageCode, Button button)
    {
        final Handler handler = new Handler();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(APIConstants.LANGUAGE_API_KEY)
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(button.getText().toString(),
                                Translate.TranslateOption.targetLanguage(languageCode));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (button != null) {
                            button.setText(translation.getTranslatedText());
                        }

                    }
                });
                return null;
            }
        }.execute();
    }

}
