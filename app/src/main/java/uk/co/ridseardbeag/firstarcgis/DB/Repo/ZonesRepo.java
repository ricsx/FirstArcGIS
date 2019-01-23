package uk.co.ridseardbeag.firstarcgis.DB.Repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import uk.co.ridseardbeag.firstarcgis.DB.DatabaseManager;
import uk.co.ridseardbeag.firstarcgis.DB.Model.Zones;

public class ZonesRepo {

    public ZonesRepo(){

    }


    public static String createTable(){
        return "CREATE TABLE " + Zones.TABLE  + "("
                + Zones.zonesID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Zones.zonesName + " TEXT, "
                + Zones.zonesLat + " REAL, "
                + Zones.zonesLong + " REAL "
                + ")";
    }


    public static int insert(Zones zones) {
        int zoneId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Zones.zonesName, zones.get_zone_name());
        values.put(Zones.zonesLat, zones.get_zone_lat());
        values.put(Zones.zonesLong, zones.get_zone_long());

        zoneId=(int) db.insert(zones.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return zoneId;
    }


    public static int insertEmptyZone() {
        int zoneId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Zones.zonesID, 0);
        values.put(Zones.zonesName, "-");
        values.put(Zones.zonesLat, "0");
        values.put(Zones.zonesLong, "0");

        zoneId=(int) db.insert(Zones.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return zoneId;
    }


    public static List<Zones> getZones(String selectQuery){
        Zones zones;
        List<Zones> zonesArrayList= new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                zones = new Zones();
                zones.set_zone_id(cursor.getInt(cursor.getColumnIndex(Zones.zonesID)));
                zones.set_zone_name(cursor.getString(cursor.getColumnIndex(Zones.zonesName)));
                zones.set_zone_lat(cursor.getDouble(cursor.getColumnIndex(Zones.zonesLat)));
                zones.set_zone_long(cursor.getDouble(cursor.getColumnIndex(Zones.zonesLong)));
                zonesArrayList.add(zones);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return zonesArrayList;
    }


    public static void update(int zone_id, String zone_name, double zone_lat, double zone_long) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(Zones.zonesName, zone_name);
        values.put(Zones.zonesLat, zone_lat);
        values.put(Zones.zonesLong, zone_long);

        db.update(Zones.TABLE, values, Zones.zonesID + "= ?",
                new String[] { String.valueOf(zone_id) });
        db.close();
        DatabaseManager.getInstance().closeDatabase();
    }
}
