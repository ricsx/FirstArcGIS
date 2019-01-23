package uk.co.ridseardbeag.firstarcgis.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Objects;

import uk.co.ridseardbeag.firstarcgis.Common.Common;
import uk.co.ridseardbeag.firstarcgis.DB.Repo.SettingsRepo;
import uk.co.ridseardbeag.firstarcgis.R;


public class Setup extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Spinner _spDefPlace = findViewById(R.id.spDefPlace);
        ImageButton _btnExtent1 = findViewById(R.id.btnExtent1);
        ImageButton _btnExtent2 = findViewById(R.id.btnExtent2);
        ImageButton _btnExtent4 = findViewById(R.id.btnExtent4);

        _btnExtent1.setOnClickListener(this);
        _btnExtent2.setOnClickListener(this);
        _btnExtent4.setOnClickListener(this);

        _btnExtent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsRepo.update("defExtent", "1");
            }
        });

        _btnExtent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsRepo.update("defExtent", "2");
            }
        });

        _btnExtent4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsRepo.update("defExtent", "4");
            }
        });


        Common.zonesListToSpinner(_spDefPlace, this,
                "select * from Zones order by zone_name",
                getString(R.string.chooseZone));
        _spDefPlace.setSelection(Common.calcIndexToZoneSpinner(Integer.parseInt(Common.settingTest("defZone")))+1);

        _spDefPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SettingsRepo.update("defZone",
                        String.valueOf(Common.zone_idFromSpinner(
                         "select * from zones where zone_name = \"" + parent.getItemAtPosition(position).toString()+"\"")));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}
