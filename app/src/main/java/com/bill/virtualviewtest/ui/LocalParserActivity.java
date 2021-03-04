package com.bill.virtualviewtest.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bill.virtualviewtest.MyApplication;
import com.bill.virtualviewtest.R;
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

public class LocalParserActivity extends AppCompatActivity {

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
        loadTemplates(MYTEST.BIN);
    }

    private void loadTemplates(byte... bytes) {
        sViewManager.loadBinBufferSync(bytes);
        JSONObject json = getJSONDataFromAsset("data/MyTest.json");
        preview("MyTest", json);
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
