package com.bill.virtualviewtest.event;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.bill.virtualviewtest.util.ActivityManager;
import com.bill.virtualviewtest.util.Utils;
import com.bill.virtualviewtest.web.DetailActivity;
import com.tmall.wireless.vaf.virtualview.event.EventData;
import com.tmall.wireless.vaf.virtualview.event.IEventProcessor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author : Bill
 * date : 2021/3/12
 * description :
 */
public class ClickProcessorImpl implements IEventProcessor {

    @Override
    public boolean process(EventData data) {
        Log.d("IEventProcessor", "ClickProcessorImpl-> event " + data.mVB.getAction() + " " + data.mVB.getId());
        String action = data.mVB.getAction();

        if (action == null)
            return true;

        if (action.startsWith("{")) {
            try {
                JSONObject obj = new JSONObject(action);
                String url = obj.optString("url");
                gotoDetail(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Utils.toast(action);
        }

        return true;
    }

    private void gotoDetail(String url) {
        Activity currentAct = ActivityManager.getInstance().getTopActivity();
        if (currentAct != null) {
            Intent intent = new Intent(currentAct, DetailActivity.class);
            intent.putExtra("url", url);
            currentAct.startActivity(intent);
        }
    }

}
