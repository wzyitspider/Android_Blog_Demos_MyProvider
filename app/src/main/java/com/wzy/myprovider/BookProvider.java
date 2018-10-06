package com.wzy.myprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BookProvider extends ContentProvider {
    static final String DATABASE_NAME = "provider.db";
    static final String DATABASE_TABLE = "bookinfo";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE = "create table " +
            DATABASE_TABLE + " (_id integer primary key autoincrement, " + "name text not null, author text not null);";
    static final String PROVIDER_NAME = "com.wzy.provider";
    static final int BOOKS = 1;
    static final int BOOK_ID = 2;
    static UriMatcher uriMatcher;
    SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "books", BOOKS);
        uriMatcher.addURI(PROVIDER_NAME, "books/#", BOOK_ID);
    }

    private String TAG = "BookProvider";

    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS BookInfo");
            onCreate(db);
        }
    }


    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        db = helper.getWritableDatabase();
        return false;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG,"Run in method getType() ,uri:"+uri);
        switch (uriMatcher.match(uri)) {

            case BOOKS:
                return "vnd.android.cursor.dir/vnd.com.wzy.provider.books";
            case BOOK_ID:
                return "vnd.android.cursor.item/vnd.com.wzy.provider.books";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_ID:
                cursor = db.query(DATABASE_TABLE, projection, "_id = ", new String[]{uri.getPathSegments().get(1)}, null, null, sortOrder);
                break;
            case BOOKS:
                cursor = db.query(DATABASE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowId = db.insert(DATABASE_TABLE, null, values);
        if (rowId > 0) {
            Uri bookUri = ContentUris.withAppendedId(uri, rowId);
            return bookUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowIDs;
        switch (uriMatcher.match(uri)) {
            case BOOK_ID:
                String bookID = uri.getPathSegments().get(1);
                rowIDs = db.delete(DATABASE_TABLE, "_id = ", new String[]{bookID});
                break;
            case BOOKS:
                rowIDs = db.delete(DATABASE_TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return rowIDs;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowIDs;
        switch (uriMatcher.match(uri)) {
            case BOOK_ID:
                String bookID = uri.getPathSegments().get(1);
                rowIDs = db.update(DATABASE_TABLE, values, "_id = ?", new String[]{bookID});
                break;
            case BOOKS:
                rowIDs = db.update(DATABASE_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return rowIDs;
    }

}
