package com.bill.virtualviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bill.virtualviewtest.ui.LocalLoadActivity;
import com.bill.virtualviewtest.ui.NetLoadActivity;
import com.bill.virtualviewtest.ui.RealtimeListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void handleLocal(View view) {
        startActivity(new Intent(this, LocalLoadActivity.class));

    }

    public void handleRealtime(View view) {
        startActivity(new Intent(this, RealtimeListActivity.class));
    }

    public void handleNet(View view) {
        startActivity(new Intent(this, NetLoadActivity.class));
    }
}
