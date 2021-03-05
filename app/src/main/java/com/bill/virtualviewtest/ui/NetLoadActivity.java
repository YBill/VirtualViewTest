package com.bill.virtualviewtest.ui;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bill.virtualviewtest.MyApplication;
import com.bill.virtualviewtest.R;
import com.bill.virtualviewtest.util.ThreadUtils;
import com.bill.virtualviewtest.util.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.framework.ViewManager;
import com.tmall.wireless.vaf.virtualview.core.IContainer;
import com.tmall.wireless.vaf.virtualview.core.Layout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetLoadActivity extends AppCompatActivity {

    private LinearLayout mLinearLayout;
    private AppCompatButton loadTemplateBtn;
    private AppCompatButton addTemplateViewBtn;
    private AppCompatButton loadDataBtn;

    private VafContext sVafContext;
    private ViewManager sViewManager;

    private Map<String, View> mTemplateViewMap = new HashMap<>();

    private Map<String, Boolean> mLoadTemplateMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_load);
        initView();
        sVafContext = ((MyApplication) getApplication()).getVafContext();
        sViewManager = ((MyApplication) getApplication()).getViewManager();
        mLoadTemplateMap = ((MyApplication) getApplication()).getLoadTemplateMap();
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.container);
        loadTemplateBtn = findViewById(R.id.btn_load_template);
        addTemplateViewBtn = findViewById(R.id.btn_add_template_view);
        loadDataBtn = findViewById(R.id.btn_load_data);
        loadTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTemplateData();
            }
        });
        addTemplateViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTemplateName();
            }
        });
        loadDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    /////////////

    private void loadTemplateData() {
        ThreadUtils.runOnWork(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://run.mocky.io/v3/0e24ce7a-cd1f-4ba2-8d4e-5a639a6a9f5d")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String string = response.body().string();
                            final TemplateBean templateBean = new Gson().fromJson(string, TemplateBean.class);
                            if (templateBean != null) {
                                loadTemplates(templateBean.templates);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Utils.toast("Check Network");
                }

            }
        });

    }

    private void loadTemplates(ArrayList<TemplateBean.Template> templates) {
        int length = templates.size();
        for (int i = 0; i < length; i++) {
            TemplateBean.Template template = templates.get(i);
            int res = sViewManager.loadBinBufferSync(Base64.decode(template.template, Base64.DEFAULT));
            if (res > 0) {
                mLoadTemplateMap.put(template.name, true);
            }
        }

        // Toast

        StringBuilder builder = new StringBuilder("(1) ");
        builder.append("Total：");
        builder.append("[ ");
        for (int i = 0; i < length; i++) {
            TemplateBean.Template template = templates.get(i);
            builder.append(template.name);
            if (i == length - 1) {
                builder.append(" ]");
            } else
                builder.append("，");
        }

        builder.append("\n");
        builder.append("Success：");
        builder.append("[ ");
        if (mLoadTemplateMap.isEmpty()) {
            builder.append(" ]");
        } else {
            int i = 0;
            for (String key : mLoadTemplateMap.keySet()) {
                builder.append(key);
                if (i == length - 1) {
                    builder.append(" ]");
                } else
                    builder.append("，");
                i++;
            }
        }

        Snackbar.make(mLinearLayout, builder, Snackbar.LENGTH_INDEFINITE).show();

    }

    /////////////

    private void loadTemplateName() {
        ThreadUtils.runOnWork(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://run.mocky.io/v3/78fca175-0a73-4d78-9a58-41f2038bd4a0")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String string = response.body().string();
                            final TemplateNameBean templateBean = new Gson().fromJson(string, TemplateNameBean.class);
                            if (templateBean != null) {
                                ThreadUtils.runOnMain(new Runnable() {
                                    @Override
                                    public void run() {
                                        addTemplateView(templateBean.template_name);
                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Utils.toast("Check Network");
                }

            }
        });

    }

    private void addTemplateView(ArrayList<String> templateNameList) {
        if (templateNameList == null || templateNameList.isEmpty()) {
            Utils.toast("Load Template Name is Null");
            return;
        }

        int length = templateNameList.size();

        mLinearLayout.removeAllViews();

        for (String templateName : templateNameList) {
            if (!mLoadTemplateMap.containsKey(templateName)) {
                continue;
            }

            View mTemplateView = sVafContext.getContainerService().getContainer(templateName, true);
            IContainer iContainer = (IContainer) mTemplateView;

            Layout.Params p = iContainer.getVirtualView().getComLayoutParams();
            LinearLayout.LayoutParams marginLayoutParams = new LinearLayout.LayoutParams(p.mLayoutWidth, p.mLayoutHeight);
            marginLayoutParams.leftMargin = p.mLayoutMarginLeft;
            marginLayoutParams.topMargin = p.mLayoutMarginTop;
            marginLayoutParams.rightMargin = p.mLayoutMarginRight;
            marginLayoutParams.bottomMargin = p.mLayoutMarginBottom;

            mLinearLayout.addView(mTemplateView, marginLayoutParams);
            mTemplateViewMap.put(templateName, mTemplateView);
        }

        // Toast

        StringBuilder builder = new StringBuilder("(2) ");
        builder.append("Total：");
        builder.append("[ ");
        for (int i = 0; i < length; i++) {
            String template = templateNameList.get(i);
            builder.append(template);
            if (i == length - 1) {
                builder.append(" ]");
            } else
                builder.append("，");
        }

        builder.append("\n");
        builder.append("Success：");
        builder.append("[ ");
        if (mTemplateViewMap.isEmpty()) {
            builder.append(" ]");
        } else {
            int i = 0;
            for (String key : mTemplateViewMap.keySet()) {
                builder.append(key);
                if (i == length - 1) {
                    builder.append(" ]");
                } else
                    builder.append("，");
                i++;
            }
        }

        Snackbar.make(mLinearLayout, builder, Snackbar.LENGTH_INDEFINITE).show();

    }

    /////////////

    private void loadData() {
        ThreadUtils.runOnWork(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://run.mocky.io/v3/ec69eb64-deac-42aa-a6c4-f3c7a2439b4b")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String string = response.body().string();
                            final DataBean dataBean = new Gson().fromJson(string, DataBean.class);
                            if (dataBean == null)
                                return;
                            ThreadUtils.runOnMain(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<DataBean.Data> list = dataBean.data;
                                    preview(list);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Utils.toast("Check Network");
                }

            }
        });

    }

    private void preview(ArrayList<DataBean.Data> list) {
        if (list == null || list.isEmpty()) {
            Utils.toast("Data is null");
            return;
        }

        int length = list.size();
        List<String> loadSuccessList = new ArrayList<>(length);

        for (DataBean.Data data : list) {
            View view = mTemplateViewMap.get(data.template_name);
            if (view == null) {
                continue;
            }

            loadSuccessList.add(data.template_name);

            IContainer iContainer = (IContainer) view;
            try {
                JSONObject obj = new JSONObject(data.data.toString());
                iContainer.getVirtualView().setVData(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Toast

        StringBuilder builder = new StringBuilder("(3) ");
        builder.append("Total：");
        builder.append("[ ");
        for (int i = 0; i < length; i++) {
            DataBean.Data data = list.get(i);
            builder.append(data.template_name);
            if (i == length - 1) {
                builder.append(" ]");
            } else
                builder.append("，");
        }

        builder.append("\n");
        builder.append("Success：");
        builder.append("[ ");
        if (loadSuccessList.isEmpty()) {
            builder.append(" ]");
        } else {
            for (int i = 0; i < loadSuccessList.size(); i++) {
                String name = loadSuccessList.get(i);
                builder.append(name);
                if (i == length - 1) {
                    builder.append(" ]");
                } else
                    builder.append("，");
            }
        }

        Snackbar.make(mLinearLayout, builder, Snackbar.LENGTH_INDEFINITE).show();

    }

    /////////////////

    private static class TemplateBean {
        ArrayList<Template> templates;

        private static class Template {
            public String name;
            public String template;
        }
    }

    private static class TemplateNameBean {
        ArrayList<String> template_name;
    }

    private static class DataBean {
        ArrayList<Data> data;

        private static class Data {
            public String template_name;
            public com.google.gson.JsonObject data;
        }
    }


}
