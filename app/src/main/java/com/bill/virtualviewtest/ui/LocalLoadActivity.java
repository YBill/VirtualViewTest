package com.bill.virtualviewtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bill.virtualviewtest.R;
import com.bill.virtualviewtest.base.BaseActivity;

public class LocalLoadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_load);
    }

    public void handleLoadJavaBytes(View view) {
        startActivity(new Intent(this, LocalParserBytesActivity.class));
    }

    public void handleLoadMd5String(View view) {
        startActivity(new Intent(this, LocalParserMd5StrActivity.class));
    }

    public void handleLoadOut(View view) {
        startActivity(new Intent(this, LocalParserOutActivity.class));
    }
}
