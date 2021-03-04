package com.bill.virtualviewtest.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bill.virtualviewtest.R;
import com.bill.virtualviewtest.util.HttpUtil;
import com.bill.virtualviewtest.util.ThreadUtils;
import com.bill.virtualviewtest.util.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();
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

    private void initView() {
        mRv = findViewById(R.id.rv_list);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new ListAdapter(this);
        mRv.setAdapter(mAdapter);
    }

    private void getListData() {
        ThreadUtils.runOnWork(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(getUrl())
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String string = response.body().string();
                            final String[] dirs = new Gson().fromJson(string, String[].class);

                            if (dirs != null) {
                                ThreadUtils.runOnMain(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.setData(Arrays.asList(dirs));
                                    }
                                });
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

    private String getUrl() {
        return HttpUtil.getHostUrl() + ".dir";
    }

    private static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

        private Context mContext;
        private List<String> mData = new ArrayList<>(5);

        public ListAdapter(Context context) {
            mContext = context;
        }

        public void setData(List<String> data) {
            this.mData = data;
            this.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_item, parent, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder holder, int position) {
            holder.update(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ListHolder extends RecyclerView.ViewHolder {

            private AppCompatTextView textView;

            public ListHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_name);
            }

            private void update(int position) {
                final String name = mData.get(position);
                textView.setText(name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("name", name);
                        mContext.startActivity(intent);
                    }
                });
            }
        }

    }

}
