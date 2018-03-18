package me.maxandroid.italker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.data.Helper.AccountHelper;
import com.example.factory.persistence.Account;
import com.igexin.sdk.PushConsts;

/**
 * Created by mxz on 18-3-17.
 */

public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID: {
                Log.i(TAG, "CLIENT_ID" + bundle.toString());
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA: {
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String message = new String(payload);
                    Log.i(TAG, "MESSAGE" + message);
                    onMessageArrived(message);
                }
                break;
            }
            default:
                Log.i(TAG, "OTHER");
                break;
        }
    }

    private void onClientInit(String cid) {
        Account.setPushId(cid);
        if (Account.isLogin()) {
            AccountHelper.bindPush(null);
        }
    }

    private void onMessageArrived(String message) {
        Factory.dispatchPush(message);
    }
}
