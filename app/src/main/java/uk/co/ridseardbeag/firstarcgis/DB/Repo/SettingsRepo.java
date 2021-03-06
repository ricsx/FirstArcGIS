package uk.co.ridseardbeag.firstarcgis.DB.Repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import uk.co.ridseardbeag.firstarcgis.DB.DatabaseManager;
import uk.co.ridseardbeag.firstarcgis.DB.Model.Settings;

public class SettingsRepo {

    public SettingsRepo(){
    }


    public static String createTable(){
        return "CREATE TABLE " + Settings.TABLE  + "("
                + Settings.KEY_settings_id  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Settings.KEY_settings_name + " TEXT, "
                + Settings.KEY_settings_val + " TEXT "
                + ")";
    }


    public static int insert(Settings settings) {
        int settingsId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Settings.KEY_settings_name, settings.get_settings_name());
        values.put(Settings.KEY_settings_val, settings.get_settings_val());

        settingsId=(int) db.insert(Settings.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return settingsId;
    }


    public static List<Settings> getSettings(String selectQuery){
        Settings settings;
        List<Settings> settings_s = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                settings= new Settings();
                settings.set_settings_id(cursor.getInt(cursor.getColumnIndex(Settings.KEY_settings_id)));
                settings.set_settings_name(cursor.getString(cursor.getColumnIndex(Settings.KEY_settings_name)));
                settings.set_settings_val(cursor.getString(cursor.getColumnIndex(Settings.KEY_settings_val)));
                settings_s.add(settings);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return settings_s;
    }

    public static void update(String settings_name, String settings_val) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(Settings.KEY_settings_name, settings_name);
        values.put(Settings.KEY_settings_val, settings_val);

        db.update (Settings.TABLE, values, Settings.KEY_settings_name + " = ?",
                new String[] { String.valueOf(settings_name) });
        db.close();
        DatabaseManager.getInstance().closeDatabase();
    }
}