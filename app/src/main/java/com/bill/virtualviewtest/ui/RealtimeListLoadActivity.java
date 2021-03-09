package com.bill.virtualviewtest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bill.virtualviewtest.MyApplication;
import com.bill.virtualviewtest.R;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RealtimeListLoadActivity extends AppCompatActivity {

    private VafContext sVafContext;
    private ViewManager sViewManager;

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_parser);
        mLinearLayout = findViewById(R.id.container);

        sVafContext = ((MyApplication) getApplication()).getVafContext();
        sViewManager = ((MyApplication) getApplication()).getViewManager();

        getListData();
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
                getListData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getListData() {
        mLinearLayout.removeAllViews();
        ThreadUtils.runOnWork(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(HttpUtil.getHostUrl() + ".dir")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String string = response.body().string();
                            final String[] dirs = new Gson().fromJson(string, String[].class);

                            if (dirs != null) {
                                List<String> list = handleName(dirs);
                                for (String name : list) {
                                    refreshByUrl(name);
                                }
                            } else {
                                Utils.toast("No templates!");
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

    private List<String> handleName(String[] names) {
        List<String> list = new ArrayList<>(names.length);
        for (String name : names) {
            if (name.startsWith("Layout"))
                list.add(name);
        }

        Collections.sort(list);

        return list;
    }

    private void refreshByUrl(final String name) {
        final String url = HttpUtil.getHostUrl() + name + "/data.json";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    String string = response.body().string();
                    final RealtimeDetailActivity.PreviewData previewData = new Gson().fromJson(string, RealtimeDetailActivity.PreviewData.class);
                    if (previewData != null) {
                        loadTemplates(previewData.templates);

                        ThreadUtils.runOnMain(new Runnable() {
                            @Override
                            public void run() {
                                JsonObject json = previewData.data;
                                if (json != null) {
                                    try {
                                        JSONObject mJsonData = JSON.parseObject(json.toString());
                                        preview(name, mJsonData);
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

    private void loadTemplates(ArrayList<String> templates) {
        for (String temp : templates) {
            sViewManager.loadBinBufferSync(Base64.decode(temp, Base64.DEFAULT));
        }
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

        mLinearLayout.addView(mContainer, marginLayoutParams);
    }

}
