package com.example.ricardoprieto.silenciador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MuteByWifiBootReceiver extends BroadcastReceiver {
    public MuteByWifiBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, MuteByWiFiService.class);
            context.startService(serviceIntent);
        }
    }
}
