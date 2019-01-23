package uk.co.ridseardbeag.firstarcgis.DB;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import uk.co.ridseardbeag.firstarcgis.Common.Common;
import uk.co.ridseardbeag.firstarcgis.DB.Model.Settings;
import uk.co.ridseardbeag.firstarcgis.DB.Repo.SettingsRepo;
import uk.co.ridseardbeag.firstarcgis.DB.Repo.ZonesRepo;


public class DBHelper  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME = "farcgis.db";
    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper( ) {
        super(Common.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SettingsRepo.createTable());
        db.execSQL(ZonesRepo.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));
        db.execSQL("DROP TABLE IF EXISTS " + Settings.TABLE);
        onCreate(db);
    }
}

