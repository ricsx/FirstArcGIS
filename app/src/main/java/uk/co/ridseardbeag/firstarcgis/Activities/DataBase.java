package uk.co.ridseardbeag.firstarcgis.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.ridseardbeag.firstarcgis.DB.DatabaseManager;
import uk.co.ridseardbeag.firstarcgis.DB.Model.Zones;
import uk.co.ridseardbeag.firstarcgis.DB.Repo.ZonesRepo;
import uk.co.ridseardbeag.firstarcgis.R;

public class DataBase extends AppCompatActivity implements View.OnLongClickListener {

    private EditText _edPLaceName, _edLat, _edLong;
    Intent newActivity;
    ListView _results;
    ArrayList<Integer> valuesID = new ArrayList<>();
    int zoneIDString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        _edPLaceName = findViewById(R.id.edPlaceName);
        _edLat = findViewById(R.id.edLat);
        _edLong = findViewById(R.id.edLong);
        _results = findViewById(R.id.results);
        Button _savePlace = findViewById(R.id.btnSavePLace);

        _edLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        _edLong.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        _savePlace.setOnLongClickListener(this);

        makeListView();

        _savePlace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }


    public void newPLace(View view) {

        Zones zones = new Zones();
        zones.set_zone_name(_edPLaceName.getText().toString());
        zones.set_zone_lat(Double.parseDouble(_edLat.getText().toString()));
        zones.set_zone_long(Double.parseDouble(_edLong.getText().toString()));
        ZonesRepo.insert(zones);
        _edPLaceName.setText(""); _edLat.setText(""); _edLong.setText("");
        finish();
        newActivity = new Intent(DataBase.this, DataBase.class);
        startActivity(newActivity);
    }


    private void makeListView(){
        String selectQuery =  "select * from Zones order by zone_name";
        List<Zones> zonesList = ZonesRepo.getZones(selectQuery);
        ArrayList<String> listViewValue = new ArrayList<>();

        for(int i=0; i<zonesList.size();i++){
            valuesID.add(zonesList.get(i).get_zone_id());
            listViewValue.add(zonesList.get(i).get_zone_name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listViewValue);
        _results.setAdapter(adapter);

        _results.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String itemValue = (String) _results.getItemAtPosition(position);
                zoneIDString= getZoneIDFromQuery( "select * from Zones where zone_name = \"" + itemValue + "\"");
                createDialog();
            }
        });
    }


    private void createDialog() {

        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);

        alertDlg.setTitle(getString(R.string.Delete))
                .setMessage(getString(R.string.RUSure))
                .setCancelable(false);

        alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                db.delete(Zones.TABLE, Zones.zonesID+ "=?", new String[]{String.valueOf(zoneIDString)});
                newActivity = new Intent(DataBase.this, DataBase.class);
                startActivity(newActivity );
            }
        });

        alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDlg.create().show();
    }


    private int getZoneIDFromQuery(String query){
        int zoneID=0;
        List<Zones> zonesList = ZonesRepo.getZones(query);

        for(int i=0; i<zonesList.size();i++){ zoneID = zonesList.get(i).get_zone_id(); }

        return zoneID;
    }


    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
