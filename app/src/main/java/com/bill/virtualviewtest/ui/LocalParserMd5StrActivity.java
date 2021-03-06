package com.bill.virtualviewtest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;

import com.bill.virtualviewtest.MyApplication;
import com.bill.virtualviewtest.R;
import com.bill.virtualviewtest.base.BaseActivity;
import com.bill.virtualviewtest.bytes.MYTEST;
import com.bill.virtualviewtest.util.Utils;
import com.tmall.wireless.vaf.framework.VafContext;
import com.tmall.wireless.vaf.framework.ViewManager;
import com.tmall.wireless.vaf.virtualview.core.IContainer;
import com.tmall.wireless.vaf.virtualview.core.Layout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LocalParserMd5StrActivity extends BaseActivity {

    private static final String NAME = "MyTest";
    private static final String TEMPLATE = MYTEST.STR;
    private static final String DATA = "data/MyTest.json";

    private LinearLayout mLinearLayout;

    private VafContext sVafContext;
    private ViewManager sViewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_parser);
        mLinearLayout = findViewById(R.id.container);

        sVafContext = ((MyApplication) getApplication()).getVafContext();
        sViewManager = ((MyApplication) getApplication()).getViewManager();

        load();
    }

    private void load() {
        loadTemplates(TEMPLATE);
        JSONObject json = getJSONDataFromAsset(DATA);
        preview(NAME, json);
    }

    private void loadTemplates(String template) {
        sViewManager.loadBinBufferSync(Base64.decode(template, Base64.DEFAULT));
    }

    private void preview(String templateName, JSONObject jsonData) {
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

    private JSONObject getJSONDataFromAsset(String name) {
        try {
            InputStream inputStream = getAssets().open(name);
            BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = inputStreamReader.readLine()) != null) {
                sb.append(str);
            }
            inputStreamReader.close();
            return new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
