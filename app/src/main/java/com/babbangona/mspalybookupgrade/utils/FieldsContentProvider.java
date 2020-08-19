package com.babbangona.mspalybookupgrade.utils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.FieldsDao;
import com.babbangona.mspalybookupgrade.data.db.entities.Fields;

public class FieldsContentProvider extends ContentProvider {

    public static final String TAG = FieldsContentProvider.class.getName();

    private FieldsDao fieldsDao;

    /**
     * Authority of this content provider
     */
    public static final String AUTHORITY =    "com.babbangona.mspalybookupgrade";

    public static final String PERSON_TABLE_NAME = DatabaseStringConstants.FIELDS_TABLE;

    /**
     * The match code for some items in the fields table
     */
    public static final int ID_FIELDS_DATA = 1;

    /**
     * The match code for an item in the PErson table
     */
    public static final int ID_FIELDS_DATA_ITEM = 2;

    public static final UriMatcher uriMatcher = new UriMatcher
            (UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY,
                PERSON_TABLE_NAME,
                ID_FIELDS_DATA);
        uriMatcher.addURI(AUTHORITY,
                PERSON_TABLE_NAME +
                        "/*", ID_FIELDS_DATA_ITEM);
    }

    @Override
    public boolean onCreate() {
        fieldsDao = AppDatabase.getInstance(getContext())
                .fieldsDao();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert");
        switch (uriMatcher.match(uri)) {
            case ID_FIELDS_DATA:
                if (getContext() != null) {
                    long id = 0;
                    if (values != null) {
                        id = fieldsDao.insert(Fields.
                                fromContentValues(values));
                    }else{
                        Log.d(TAG, "insert_failed");
                    }
                    if (id != 0) {
                        getContext().getContentResolver()
                                .notifyChange(uri, null);
                        return ContentUris.withAppendedId(uri, id);
                    }
                }
            case ID_FIELDS_DATA_ITEM:
                throw new IllegalArgumentException
                        ("Invalid URI: Insert failed" + uri);
            default:
                throw new IllegalArgumentException
                        ("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update");
        switch (uriMatcher.match(uri)) {
            case ID_FIELDS_DATA:
                if (getContext() != null) {
                    int count = 0;
                    if (values != null) {
                        count = fieldsDao
                                .update(Fields.fromContentValues(values));
                    }else{
                        Log.d(TAG, "update_failed");
                    }
                    if (count != 0) {
                        getContext().getContentResolver()
                                .notifyChange(uri, null);
                        return count;
                    }
                }
            case ID_FIELDS_DATA_ITEM:
                throw new IllegalArgumentException
                        ("Invalid URI:  cannot update");
            default:
                throw new IllegalArgumentException
                        ("Unknown URI: " + uri);
        }
    }
}
