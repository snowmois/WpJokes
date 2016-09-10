package com.leeson.wpjokes.Utils;

import android.os.Handler;
import android.os.Message;

import com.leeson.wpjokes.Adapter.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * Created by MyComputer on 2016/8/15 0015.
 */
public class HttpUtils {

    public static void
    HttpURLConnection_GET(final String path, final Handler handler, final List articleList) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                articleList = new ArrayList<>();
//         String path = "http://wp.giligiliml.top/latestpost.php";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {

                        InputStream is = conn.getInputStream();       //以输入流的形式返回
                        //将输入流转换成字符串
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, len);
                        }
                        String jsonString = baos.toString();
                        baos.close();
                        is.close();
                        conn.disconnect();
                        //转换成json数据处理
                        JSONArray jsonArray = new JSONArray(jsonString);
                        int page = 1;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            Article article = new Article();
                            page = i / 11;
                            article.setPost_title(jsonobject.getString("post_title"));
                            article.setPost_date(jsonobject.getString("post_date"));
                            article.setPost_content(jsonobject.getString("post_content"));
                            article.set_Page(page);
                            articleList.add(article);
                        }
                        Message message = new Message();
                        message.what = 0;
                        message.obj = articleList;
                        handler.sendMessage(message);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }).start();
    }


}