package uk.co.ridseardbeag.firstarcgis.DB.Model;


public class Zones {
    public static final String TABLE = "Zones";

    public static final String zonesID = "zone_id";
    public static final String zonesName = "zone_name";
    public static final String zonesLat = "zone_lat";
    public static final String zonesLong = "zone_long";

    private int _zone_id;
    private String _zone_name;
    private double _zone_lat;
    private double _zone_long;


    public Zones() {

    }

    public Zones(int zone_id, String zone_name, double zone_lat, double zone_long){
        this._zone_id = zone_id;
        this._zone_name = zone_name;
        this._zone_lat = zone_lat;
        this._zone_long = zone_long;
    }


    public int get_zone_id() {
        return _zone_id;
    }

    public void set_zone_id(int _zone_id) {
        this._zone_id = _zone_id;
    }

    public String get_zone_name() {
        return _zone_name;
    }

    public void set_zone_name(String _zone_name) {
        this._zone_name = _zone_name;
    }

    public double get_zone_lat() {
        return _zone_lat;
    }

    public void set_zone_lat(double _zone_lat) {
        this._zone_lat = _zone_lat;
    }

    public double get_zone_long() {
        return _zone_long;
    }

    public void set_zone_long(double _zone_long) {
        this._zone_long = _zone_long;
    }
}
