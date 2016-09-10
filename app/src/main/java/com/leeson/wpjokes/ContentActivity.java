package com.leeson.wpjokes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.leeson.wpjokes.Db.Db;

/**
 * Created by MyComputer on 2016/8/5 0005.
 */
public class ContentActivity extends AppCompatActivity {
    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_content;
    private Db db;
    private SQLiteDatabase dbReader;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);
        init();
        Intent intent = getIntent();
        String postTitle = intent.getStringExtra("postTitle");
        String postDate = intent.getStringExtra("postDate");
        String postContent = intent.getStringExtra("postContent");
        tv_title.setText(postTitle);
        tv_time.setText(postDate);
        tv_content.setText(postContent);
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_content = (TextView) findViewById(R.id.tv_content);
        db = new Db(this);
        dbReader = db.getReadableDatabase();

    }

    /*按返回键后，结束activity*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
