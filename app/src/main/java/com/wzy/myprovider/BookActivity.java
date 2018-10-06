package com.wzy.myprovider;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BookActivity extends AppCompatActivity {

    public static final String AUTHORITY = "com.wzy.provider.book";
    public static final Uri CONTENT_URI_FIRST = Uri.parse("content://" + AUTHORITY + "/first");
    public static Uri mCurrentURI = CONTENT_URI_FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
    }
}
