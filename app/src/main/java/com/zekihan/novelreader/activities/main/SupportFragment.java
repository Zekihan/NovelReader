package com.zekihan.novelreader.activities.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.zekihan.novelreader.R;

public class SupportFragment extends Fragment {

    @NonNull
    String TAG = "SupportFragment";
    private Context mContext;
    private RewardedVideoAd mRewardedVideoAd;

    public SupportFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_support, container, false);

        mContext = rootView.getContext();

        final Button button = rootView.findViewById(R.id.button);
        button.setText(R.string.watch);

        final Button button2 = rootView.findViewById(R.id.button2);
        button2.setText(R.string.help_me);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:azmanzekihan@gmail.com?subject=" + "Novel Reader Support" + "&body=" + System.lineSeparator()
                        + System.lineSeparator() + System.lineSeparator() + System.lineSeparator() + System.lineSeparator()
                        + "Don't touch the lines below,they are required for us to make faster and more accurate fixes" + System.lineSeparator()
                        + "System Os Version :" + System.getProperty("os.version") + System.lineSeparator()
                        + "SDK :" + Build.VERSION.SDK_INT + System.lineSeparator()
                        + "Device :" + android.os.Build.DEVICE + System.lineSeparator()
                        + "Model :" + android.os.Build.MODEL + System.lineSeparator()
                        + "BRAND :" + Build.BRAND + System.lineSeparator()
                        + "TIME :" + Build.TIME + System.lineSeparator()
                );
                intent.setData(data);
                startActivity(intent);
            }
        });

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
        mRewardedVideoAd.loadAd(getResources().getString(R.string.rewarded_video_ad_unit_id), new AdRequest.Builder().build());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
            }

            @Override
            public void onRewardedVideoAdOpened() {
            }

            @Override
            public void onRewardedVideoStarted() {
            }

            @Override
            public void onRewardedVideoAdClosed() {
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(mContext, "Appreciating the support", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                mRewardedVideoAd.loadAd(getResources().getString(R.string.rewarded_video_ad_unit_id), new AdRequest.Builder().build());
            }

            @Override
            public void onRewardedVideoCompleted() {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRewardedVideoAd.show();
            }
        });

        return rootView;
    }
}
