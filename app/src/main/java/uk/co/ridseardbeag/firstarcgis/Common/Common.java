package uk.co.ridseardbeag.firstarcgis.Common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.ridseardbeag.firstarcgis.DB.DBHelper;
import uk.co.ridseardbeag.firstarcgis.DB.DatabaseManager;
import uk.co.ridseardbeag.firstarcgis.DB.Model.Settings;
import uk.co.ridseardbeag.firstarcgis.DB.Model.Zones;
import uk.co.ridseardbeag.firstarcgis.DB.Repo.SettingsRepo;
import uk.co.ridseardbeag.firstarcgis.DB.Repo.ZonesRepo;
import uk.co.ridseardbeag.firstarcgis.R;


public class Common extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        DBHelper dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);
    }


    public static Context getContext(){
        return context;
    }


    public static String settingTest(String settingName){
        String settings_val = "";
        String selectQuery="SELECT * FROM Settings WHERE settings_name=\""+settingName+"\"";
        List<Settings> settings_s = SettingsRepo.getSettings(selectQuery);
        for(int i=0; i<settings_s.size(); i++) {
            settings_val = settings_s.get(i).get_settings_val();
        } return settings_val;
    }


    public static int calcIndexToZoneSpinner(int id) {
        int calcedIDX = 0;
        String selectQuery = "select * from Zones order by zone_name";
        List<Zones> zonesList = ZonesRepo.getZones(selectQuery);
        for(int i=0; i<zonesList.size(); i++){
            int zoneID = zonesList.get(i).get_zone_id();
            if(zoneID==id){ calcedIDX = i; }
        } return calcedIDX;
    }


    public static void setStatusBarColor(Window window, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(context.getResources().getColor(R.color.statusbar));
        }
    }


    public static void zonesListToSpinner(Spinner spinnername, Context context, String selectQuery, String def){
        List<Zones> zonesList = ZonesRepo.getZones(selectQuery);
        List<String> values = new ArrayList<>();
        if(!Objects.equals(def, "false")){ values.add(def); }
        for(int i=0; i<zonesList.size();i++){
            values.add(zonesList.get(i).get_zone_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
                R.layout.spinner_item, values);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnername.setAdapter(dataAdapter);
    }


    public static Integer zone_idFromSpinner(String selectQuery){
        List<Zones> zonesList = ZonesRepo.getZones(selectQuery);
        int zoneID = 0;
        for(int i = 0; i<zonesList.size(); i++){ zoneID = zonesList.get(i).get_zone_id(); }
        return zoneID;
    }

}
