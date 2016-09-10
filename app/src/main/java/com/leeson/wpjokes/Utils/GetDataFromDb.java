package com.leeson.wpjokes.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leeson.wpjokes.Adapter.Article;
import com.leeson.wpjokes.Db.Db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyComputer on 2016/8/15 0015.
 */
public class GetDataFromDb {
    private Context mContext;
    private Db db = null;

    public GetDataFromDb(Context context) {
        mContext = context;
    }

    //单例模式
    private SQLiteDatabase readDb() {
        if (db == null) {
            db = new Db(mContext);
        }
        //获取可读的SQLiteDatabase对象
        return db.getReadableDatabase();
    }


    //插入数据
    public void insertDb(List<Article> article) {
        if (article.size() != 0) {
            SQLiteDatabase db = readDb();
            for (Article item : article) {
                ContentValues values = new ContentValues();
                values.put("id", item.getID());
                values.put("post_author", item.getPost_author());
                values.put("post_date", item.getPost_date());
                values.put("post_content", item.getPost_content());
                values.put("post_title", item.getPost_title());
                db.insert(Db.TABLE_N_ARTICLE, null, values);

                values.clear();
            }
        }
    }

    // 更新数据库数据
    public void updateDb(List<Article> articles) {
        SQLiteDatabase db = readDb();
        for (Article item : articles) {
            ContentValues values = new ContentValues();
            values.put("id", item.getID());
            values.put("post_date", item.getPost_date());
            values.put("post_title", item.getPost_title());
            db.update("myarticle", values, "id = ?", new String[]{String.valueOf(item.getID())});
        }


    }

    //查询数据库
    public List<Article> queryDb(int page) {
        SQLiteDatabase db = readDb();
        List<Article> articles = new ArrayList<>();
        Cursor cursor = db.query(Db.TABLE_N_ARTICLE,
                null, null, null, null, null, "post_date desc");
        while (cursor.moveToNext()) {
            Article item = new Article();
            item.setID(cursor.getInt(cursor.getColumnIndex("id")));
            item.setPost_title(cursor.getString(cursor.getColumnIndex("post_title")));
            item.setPost_date(cursor.getString(cursor.getColumnIndex("post_date")));
            item.setPost_author(cursor.getString(cursor.getColumnIndex("post_author")));
            item.setPost_content(cursor.getString(cursor.getColumnIndex("post_content")));
            articles.add(item);
        }
        cursor.close();
        return articles;
    }


}
