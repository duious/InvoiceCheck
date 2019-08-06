/*
 * Copyright (C) 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sjxd.invoicecheck.util.zxing.history;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * <p>Manages functionality related to scan history.</p>
 *
 * @author Sean Owen
 */
public final class HistoryManager {

  private static final String TAG = HistoryManager.class.getSimpleName();

  private static final int MAX_ITEMS = 2000;

  private static final String[] COLUMNS = {
      DBHelper.TEXT_COL,
      DBHelper.DISPLAY_COL,
      DBHelper.FORMAT_COL,
      DBHelper.TIMESTAMP_COL,
      DBHelper.DETAILS_COL,
  };


  private static final String[] ID_COL_PROJECTION = { DBHelper.ID_COL };

  private final Activity activity;

  public HistoryManager(Activity activity) {
    this.activity = activity;
  }

  public HistoryItem buildHistoryItem(int number) {
    SQLiteOpenHelper helper = new DBHelper(activity);
    try (SQLiteDatabase db = helper.getReadableDatabase();
         Cursor cursor = db.query(DBHelper.TABLE_NAME,
                                  COLUMNS,
                                  null, null, null, null,
                                  DBHelper.TIMESTAMP_COL + " DESC")) {
      cursor.move(number + 1);
      String text = cursor.getString(0);
      String display = cursor.getString(1);
      String format = cursor.getString(2);
      long timestamp = cursor.getLong(3);
      String details = cursor.getString(4);
      Result result = new Result(text, null, null, BarcodeFormat.valueOf(format), timestamp);
      return new HistoryItem(result, display, details);
    }
  }

  public void trimHistory() {
    SQLiteOpenHelper helper = new DBHelper(activity);
    try (SQLiteDatabase db = helper.getWritableDatabase();
         Cursor cursor = db.query(DBHelper.TABLE_NAME,
                                  ID_COL_PROJECTION,
                                  null, null, null, null,
                                  DBHelper.TIMESTAMP_COL + " DESC")) {
      cursor.move(MAX_ITEMS);
      while (cursor.moveToNext()) {
        String id = cursor.getString(0);
        Log.i(TAG, "Deleting scan history ID " + id);
        db.delete(DBHelper.TABLE_NAME, DBHelper.ID_COL + '=' + id, null);
      }
    } catch (SQLException sqle) {
      Log.w(TAG, sqle);
    }
  }

}
