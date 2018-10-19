package com.savageking.internet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class InternetReceiver(_callback : InternetReceiverLink ) : BroadcastReceiver()
{
    val callback = _callback

    override fun onReceive(context: Context, intent: Intent) {
        callback.updateUi()
    }
}
