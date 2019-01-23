package uk.co.ridseardbeag.firstarcgis.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import java.util.List;

import uk.co.ridseardbeag.firstarcgis.Common.Common;
import uk.co.ridseardbeag.firstarcgis.DB.Model.Settings;
import uk.co.ridseardbeag.firstarcgis.DB.Model.Zones;
import uk.co.ridseardbeag.firstarcgis.DB.Repo.SettingsRepo;
import uk.co.ridseardbeag.firstarcgis.DB.Repo.ZonesRepo;
import uk.co.ridseardbeag.firstarcgis.R;

import static uk.co.ridseardbeag.firstarcgis.Common.Common.settingTest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView _mMapView;
    int level, extent;
    double latit, longit, latVal=0, longVal=0;
    Spinner _spZones;
    private Intent newActivity;
    private final SpatialReference wgs84 = SpatialReference.create(4326);
    Graphic buoyGraphic1;
    GraphicsOverlay graphicsOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Common.setStatusBarColor(this.getWindow(), this);

        final double deflat = 55.9503322, deflong = -3.176824399999987;
        ImageButton _btnInMap = findViewById(R.id.btnInMap);
        ImageButton _btnOutMap = findViewById(R.id.btnOutMap);
        Button _btnDB = findViewById(R.id.btnDB);
        Button _btnSetup = findViewById(R.id.btnSetup);
        _mMapView = findViewById(R.id.mapView);
        _spZones = findViewById(R.id.spZones);

        _btnInMap.setOnClickListener(this);
        _btnOutMap.setOnClickListener(this);
        _btnDB.setOnClickListener(this);
        _btnSetup.setOnClickListener(this);

        if (settingTest("firstrun").equals("") || settingTest("firstrun").equals("0")) firstRunLoadDefaults();

        String selectQuery = "select * from Settings where " + Settings.KEY_settings_name + " = \"defExtent\"";
        List<Settings> settings = SettingsRepo.getSettings(selectQuery);
        for (int i = 0; i < settings.size(); i++) extent = Integer.valueOf(settings.get(i).get_settings_val());

        _btnDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity = new Intent(MainActivity.this, DataBase.class);
                newActivity.putExtra("selector", "normal");
                startActivity(newActivity);
            }
        });

        _btnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity = new Intent(MainActivity.this, Setup.class);
                newActivity.putExtra("selector", "normal");
                startActivity(newActivity);
            }
        });

        _btnInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap(level+extent, latit, longit);
            }
        });

        _btnOutMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap(level-extent, latit, longit);
            }
        });

        Common.zonesListToSpinner(_spZones, this,
                "select * from Zones order by zone_name",
                getString(R.string.chooseZone));

        _spZones.setSelection(Common.calcIndexToZoneSpinner(Integer.parseInt(Common.settingTest("defZone")))+1);

        _spZones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int iid =  Common.zone_idFromSpinner(
                        "select * from zones where zone_name = \"" + parent.getItemAtPosition(position).toString()+"\"");
                String selectQuery = "select * from zones where zone_id = " + iid;
                List<Zones> zonesList = ZonesRepo.getZones(selectQuery);
                for(int i=0; i<zonesList.size(); i++){
                    latVal = zonesList.get(i).get_zone_lat();
                    longVal = zonesList.get(i).get_zone_long();
                }
                if(latVal==0){ latVal = deflat; }
                if(longVal==0){ longVal = deflong; }
                showMap(13, latVal, longVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void firstRunLoadDefaults() {

        Settings settings = new Settings();
        settings.set_settings_name("firstrun");
        settings.set_settings_val("1");
        SettingsRepo.insert(settings);
        settings.set_settings_name("defZone");
        settings.set_settings_val("1");
        SettingsRepo.insert(settings);
        settings.set_settings_name("defExtent");
        settings.set_settings_val("1");
        SettingsRepo.insert(settings);

        Zones zones = new Zones();
        zones.set_zone_name("Esri");
        zones.set_zone_lat(55.9503322);
        zones.set_zone_long(-3.176824399999987);
        ZonesRepo.insert(zones);
        zones.set_zone_name("Arthur Seat");
        zones.set_zone_lat(55.9440825);
        zones.set_zone_long(-3.1618323999999802);
        ZonesRepo.insert(zones);
        zones.set_zone_name("Edinburgh Castle");
        zones.set_zone_lat(55.9485947);
        zones.set_zone_long(-3.1999134999999796);
        ZonesRepo.insert(zones);
        zones.set_zone_name("Bellshill");
        zones.set_zone_lat(55.81676100000001);
        zones.set_zone_long(-4.026535999999965);
        ZonesRepo.insert(zones);
        zones.set_zone_name("Granton Mill West");
        zones.set_zone_lat(55.9746609);
        zones.set_zone_long(-3.2534401000000344);
        ZonesRepo.insert(zones);
    }


    private void showMap(int lev, double lat, double longi) {
        if (_mMapView != null) {
            if(buoyGraphic1!=null){ graphicsOverlay.getGraphics().remove(buoyGraphic1); }
            Basemap.Type basemapType = Basemap.Type.IMAGERY;
            level = lev; latit = lat; longit = longi;
            ArcGISMap map = new ArcGISMap(basemapType, latit, longit, level);
            _mMapView.setMap(map);
            graphicsOverlay = addGraphicsOverlay(_mMapView);
            addBuoyPoints(graphicsOverlay, latit, longit);
        }
    }

    private GraphicsOverlay addGraphicsOverlay(MapView mapView) {
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        return graphicsOverlay;
    }

    private void addBuoyPoints(GraphicsOverlay graphicOverlay, double latit, double longit) {

        Point buoy1Loc = new Point(longit, latit, wgs84);
        SimpleMarkerSymbol buoyMarker = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 10);
        buoyGraphic1 = new Graphic(buoy1Loc, buoyMarker);
        graphicOverlay.getGraphics().add(buoyGraphic1);

    }


    @Override
    protected void onPause() {
        if (_mMapView != null) {
            _mMapView.pause();
        }
    super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (_mMapView != null) {
            _mMapView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (_mMapView != null) {
            _mMapView.dispose();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

}
