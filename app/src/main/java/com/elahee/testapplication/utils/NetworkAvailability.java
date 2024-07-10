package com.btracsolutions.yesparking.utils;

import static android.content.Context.RECEIVER_EXPORTED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;

/**
 * Created by Abdullah Al Mamun on 10/28/2018.
 */

public class NetworkAvailability {

    /* TODO: CHANGE THE ACTION NAME TO BE RELEVANT TO YOUR PROJECT */
    public static final String NETWORK_AVAILABILITY_ACTION = "com.foo.NETWORK_AVAILABILITY_ACTION";

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    private static NetworkAvailability instance;

    private NetworkAvailability() {
    }

    public static NetworkAvailability getInstance(){
        if(instance == null){
            instance = new NetworkAvailability();
        }
        return instance;
    }



    public static boolean isAvailable(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void registerNetworkAvailability(final Context context, BroadcastReceiver networkAvailabilityReceiver) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(networkAvailabilityReceiver, new IntentFilter(NETWORK_AVAILABILITY_ACTION), RECEIVER_EXPORTED);
        }else{
            context.registerReceiver(networkAvailabilityReceiver, new IntentFilter(NETWORK_AVAILABILITY_ACTION));

        }


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            context.registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } else{
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    context.sendBroadcast(getNetworkAvailabilityIntent(true,context));
                }

                @Override
                public void onLost(Network network) {
                    context.sendBroadcast(getNetworkAvailabilityIntent(false,context));
                }
            };
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
            if(isAvailable(context)){
                context.sendBroadcast(getNetworkAvailabilityIntent(true,context));
            } else{
                context.sendBroadcast(getNetworkAvailabilityIntent(false,context));
            }
        }
    }

    public void unregisterNetworkAvailability(Context context, BroadcastReceiver networkAvailabilityReceiver){
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                context.unregisterReceiver(connectivityChangeReceiver);
            } else{
                connectivityManager.unregisterNetworkCallback(networkCallback);
            }
            context.unregisterReceiver(networkAvailabilityReceiver);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                context.sendBroadcast(getNetworkAvailabilityIntent(false,context));
            } else {
                context.sendBroadcast(getNetworkAvailabilityIntent(true,context));
            }
        }
    };

    @NonNull
    private Intent getNetworkAvailabilityIntent(boolean isNetworkAvailable,Context context) {
        Intent intent = new Intent(NETWORK_AVAILABILITY_ACTION);
        intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, !isNetworkAvailable);
        intent.setPackage(context.getPackageName());
        return intent;
    }
}