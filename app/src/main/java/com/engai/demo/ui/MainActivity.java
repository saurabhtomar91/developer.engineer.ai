package com.engai.demo.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.engai.demo.App;
import com.engai.demo.R;
import com.engai.demo.adapter.UserAdapter;
import com.engai.demo.model.UserModel;
import com.engai.demo.persistence.rest.ApiClientHttp;
import com.engai.demo.widget.EndlessScrollRecyclListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {

    private int offsetValue = 0;  // offset value
    private UserAdapter adapter;
    ArrayList<UserModel> userModelArrayList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EndlessScrollRecyclListener endlessScrollRecyclListener;
    private int limit = 10;
    private LinearLayoutManager linearLayoutManager;
    private boolean hasMore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        //Published RecyclerView list
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        endlessScrollRecyclListener = new EndlessScrollRecyclListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreApi(offsetValue);
            }
        };
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(endlessScrollRecyclListener);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    endlessScrollRecyclListener.resetState();
                    if(userModelArrayList != null)
                        userModelArrayList.clear();
                    if(adapter != null)
                        adapter.notifyDataSetChanged();
                    fetchApi(offsetValue);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchApi(offsetValue);

    }


    public void fetchApi(int offsetValue) {
        try {
            showProgress();
            RequestParams requestParams = new RequestParams();
            requestParams.put("limit", limit);
            requestParams.put("offset", offsetValue);
            ApiClientHttp.get(MainActivity.this, "users", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    dismissProgress();
                    try {
                        if (App.isJsonKey(response, "data")) {
                            JSONObject result = response.getJSONObject("data");
                             hasMore = result.getBoolean("has_more");
                            if (App.isJsonEmpty(result)) {
                                userModelArrayList = new ArrayList<UserModel>();
                                userModelArrayList.addAll(App.getUserData(result));
                                adapter = new UserAdapter(MainActivity.this, userModelArrayList);
                                adapter.setHasStableIds(true);
                                mRecyclerView.setAdapter(adapter);
                                mSwipeRefreshLayout.setRefreshing(false);
                                endlessScrollRecyclListener.resetState();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        dismissProgress();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    dismissProgress();
                    App.showToast(MainActivity.this, "OnFailure");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void loadMoreApi(int offsetValue) {
        try {
            if (hasMore) {
                offsetValue+=10;
                showProgress();
                RequestParams requestParams = new RequestParams();
                requestParams.put("limit", limit);
                requestParams.put("offset", offsetValue);
                ApiClientHttp.get(MainActivity.this, "users", requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        dismissProgress();
                        try {
                                if (App.isJsonKey(response, "data")) {
                                    JSONObject result = response.getJSONObject("data");
                                    hasMore = result.getBoolean("has_more");
                                    if (App.isJsonEmpty(result)) {
                                        userModelArrayList.addAll(App.getUserData(result));
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                        } catch (Exception e) {
                            dismissProgress();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        dismissProgress();
                        Toast.makeText(MainActivity.this, "OnFailure", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                //App.showToast(MarketOverviewActivity.this, "No Data to shown");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
