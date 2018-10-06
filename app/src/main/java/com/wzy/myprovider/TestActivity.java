package com.wzy.myprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

public class TestActivity extends Activity{
    public static final String AUTHORITY = "com.wzy.provider";
    public static final Uri CONTENT_URI_FIRST = Uri.parse("content://" + AUTHORITY + "/books");
    public static Uri mCurrentURI = CONTENT_URI_FIRST;
    public static final Uri CONTENT_URI_FIRST1 = Uri.parse("content://" + AUTHORITY + "/books/1");
    public static Uri mCurrentURI1 = CONTENT_URI_FIRST1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

    public void test(View view) {
        Intent intent = new Intent();
        intent.setAction("com.wzy.action");
        intent.setData(mCurrentURI);
        startActivity(intent);
    }

}