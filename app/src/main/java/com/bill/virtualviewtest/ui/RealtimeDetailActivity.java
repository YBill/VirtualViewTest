package com.bill.virtualviewtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bill.virtualviewtest.MyApplication;
import com.bill.virtualviewtest.R;
import com.bill.virtualviewtest.base.BaseActivity;
import com.bill.virtualviewtest.util.HttpUtil;
import com.bill.virtualviewtest.util.ThreadUtils;
import com.bill.virtualviewtest.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.framework.ViewManager;
import com.tmall.wireless.vaf.virtualview.core.IContainer;
import com.tmall.wireless.vaf.virtualview.core.Layout;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RealtimeDetailActivity extends BaseActivity {

    private LinearLayout mLinearLayout;

    private String mTemplateName;
    private VafContext sVafContext;
    private ViewManager sViewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_detail);
        mLinearLayout = findViewById(R.id.container);

        sVafContext = ((MyApplication) getApplication()).getVafContext();
        sViewManager = ((MyApplication) getApplication()).getViewManager();

        load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                load();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void load() {
        Intent intent = getIntent();
        mTemplateName = intent.getStringExtra("name");
        setTitle(mTemplateName);
        refreshByUrl(getUrl(mTemplateName));
    }

    private void refreshByUrl(final String url) {
        ThreadUtils.runOnWork(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String string = response.body().string();
                            final PreviewData previewData = new Gson().fromJson(string, PreviewData.class);
                            if (previewData != null) {
                                loadTemplates(previewData.templates);

                                ThreadUtils.runOnMain(new Runnable() {
                                    @Override
                                    public void run() {
                                        JsonObject json = previewData.data;
                                        if (json != null) {
                                            try {
                                                JSONObject mJsonData = JSON.parseObject(json.toString());
                                                preview(mTemplateName, mJsonData);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Utils.toast("Server is not running!");
                }

            }
        });
    }

    private void preview(String templateName, com.alibaba.fastjson.JSONObject jsonData) {
        if (TextUtils.isEmpty(templateName)) {
            Utils.toast("Template name should not be empty!!!!");
            return;
        }

        View mContainer = sVafContext.getContainerService().getContainer(templateName, true);
        IContainer iContainer = (IContainer) mContainer;
        if (jsonData != null) {
            iContainer.getVirtualView().setVData(jsonData);
        }

        Layout.Params p = iContainer.getVirtualView().getComLayoutParams();
        LinearLayout.LayoutParams marginLayoutParams = new LinearLayout.LayoutParams(p.mLayoutWidth, p.mLayoutHeight);
        marginLayoutParams.leftMargin = p.mLayoutMarginLeft;
        marginLayoutParams.topMargin = p.mLayoutMarginTop;
        marginLayoutParams.rightMargin = p.mLayoutMarginRight;
        marginLayoutParams.bottomMargin = p.mLayoutMarginBottom;

        mLinearLayout.removeAllViews();
        mLinearLayout.addView(mContainer, marginLayoutParams);
    }

    private void loadTemplates(ArrayList<String> templates) {
        for (String temp : templates) {
            sViewManager.loadBinBufferSync(Base64.decode(temp, Base64.DEFAULT));
        }
    }

    private String getUrl(String name) {
        return HttpUtil.getHostUrl() + name + "/data.json";
    }

    public static class PreviewData implements Serializable {

        ArrayList<String> templates;
        JsonObject data;
    }

}
