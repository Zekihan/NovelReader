package com.zekihan.novelreader.activities.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zekihan.datatype.Setting;
import com.zekihan.novelreader.R;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private static final String TAG = "Settings";

    private Context mContext;
    private TextView puntoB;
    private TextView sample;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        mContext = rootView.getContext();

        Setting setting = Setting.getSettingFromFile(mContext);
        final String[] theme = {setting.getTheme()};
        final int[] punto = {setting.getPunto()};

        Button light = rootView.findViewById(R.id.light);
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme[0] = "white";
            }
        });
        Button dark = rootView.findViewById(R.id.dark);
        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme[0] = "dark";
            }
        });

        puntoB = rootView.findViewById(R.id.puntoB);
        sample = rootView.findViewById(R.id.sample);
        sample.setTextSize(punto[0]);
        puntoB.setText(String.valueOf(punto[0]));
        puntoB.setTextSize(punto[0]);
        Button puntoPlus = rootView.findViewById(R.id.puntoPlus);
        puntoPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pp = punto[0]++;
                puntoB.setTextSize(pp + 1);
                sample.setTextSize(pp + 1);
                puntoB.setText(String.valueOf(pp + 1));
            }
        });
        Button puntoMinus = rootView.findViewById(R.id.puntoMinus);
        puntoMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pp = punto[0]--;
                puntoB.setTextSize(pp - 1);
                sample.setTextSize(pp - 1);
                puntoB.setText(String.valueOf(pp - 1));
            }
        });

        Button btn = rootView.findViewById(R.id.saveChanges);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("theme", theme[0]);
                editor.putInt("punt", punto[0]);
                editor.apply();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Save Changes");
                builder.setMessage("Some of the changes needs the app to restart");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = mContext.getPackageManager()
                                .getLaunchIntentForPackage(mContext.getPackageName());
                        Objects.requireNonNull(i).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "Some changes won't be applied until restarting!", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return rootView;
    }
}
