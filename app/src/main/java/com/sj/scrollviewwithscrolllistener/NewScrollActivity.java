package com.sj.scrollviewwithscrolllistener;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewScrollActivity extends AppCompatActivity {

    private MyScrollView scrollView;
    private LinearLayout container;
    private ProgressBar progressBar;
    int maxItem = 20;
    private View lastItemView;
    boolean alreadyExecutingRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scroll);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        scrollView = (MyScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollListener(scrollListener);
        container = (LinearLayout) findViewById(R.id.container);

        addItemsAsynchronously();
    }

    private MyScrollView.OnScrollListener scrollListener = new MyScrollView.OnScrollListener() {
        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {

            if (lastItemView != null && !alreadyExecutingRequest) {
                Rect scrollBounds = new Rect();
                scrollView.getHitRect(scrollBounds);
                if (lastItemView.getLocalVisibleRect(scrollBounds)) {
                    // Any portion of the imageView, even a single pixel, is within the visible window
                    addItemsAsynchronously();
                }
            }
        }
    };

    private void addItemsAsynchronously() {

        new AsyncTask<Void,Void,Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
                alreadyExecutingRequest = true;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                progressBar.setVisibility(View.GONE);
                addItemsToContainer();
                alreadyExecutingRequest = false;
            }


        }.execute();
    }

    private void addItemsToContainer() {
        int lastAddedItem = container.getChildCount();
        for (int i=lastAddedItem;i<maxItem;i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.new_item, null);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText("Item - " + i);

            container.addView(view);
        }
        lastItemView = container.getChildAt(container.getChildCount() -1);
        maxItem+=10;
    }
}
