package com.example.kirill.placelocator.LocationActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kirill.placelocator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String PLACE_TITLE = "placetitle";
    public static final String PLACE_DRAWABLE_ID = "placedrawableid";
    public static final String PLACE_ABOUT = "placeabout";
    private LocationManager manager;
    private GoogleMap mMap;
    private List<Place> listPlaces;
    private int objectId = -1;
    private LinearLayout linearLayoutMoreObject;
    private LocationListener listener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location != null)
                objectCheck(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(LocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LocationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LocationActivity.this, "Access denied", Toast.LENGTH_SHORT).show();
                return;
            }
            Location location = manager.getLastKnownLocation(provider);
            if(location != null)
                objectCheck(location);
        }

        @Override
        public void onProviderDisabled(String provider) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listPlaces = new LinkedList<Place>(){
            {
                add(new Place(51.277884, 1.088516, R.string.location_activity_st_augustine_abbey, R.string.location_activity_st_augustine_abbey, R.drawable.augustine_abbey));
                add(new Place(51.278731, 1.07786, R.string.location_activity_canterbury_heritage_museum_title, R.string.location_activity_canterbury_heritage_museum, R.drawable.canterbury_heritage_museum));
                add(new Place(51.281441, 1.075839, R.string.location_activity_westgate_title, R.string.location_activity_westgate, R.drawable.westgate));
                add(new Place(51.28088, 1.076016, R.string.location_activity_kent_museum_of_freemasonry_title, R.string.location_activity_kent_museum_of_freemasonry, R.drawable.kent_museum_of_freemasonry));
                add(new Place(51.278651, 1.081495, R.string.location_activity_canterbury_roman_museum_title, R.string.location_activity_canterbury_roman_museum, R.drawable.canterbury_roman_museum));
                add(new Place(51.2756, 1.07452, R.string.location_activity_canterbury_castle_title, R.string.location_activity_canterbury_castle, R.drawable.canterbury_castle));
                add(new Place(51.279793, 1.08288, R.string.location_activity_canterbury_cathedral_title, R.string.location_activity_canterbury_cathedral, R.drawable.canterbury_cathedral));
                add(new Place(51.243764, 0.960475, R.string.location_activity_chilham_castle_title, R.string.location_activity_chilham_castle, R.drawable.chilham_castle));
                add(new Place(51.26742, 1.146913,  R.string.location_activity_howlers_wild_animal_park_title, R.string.location_activity_howlers_wild_animal_park, R.drawable.howletts_map));
                add(new Place(51.279636, 1.079192,  R.string.location_activity_beaney_house_of_art_and_knowledge_title, R.string.location_activity_beaney_house_of_art_and_knowledge, R.drawable.beaney_house_of_art_and_knowledge));
                add(new Place(55.783324, 49.151488, R.string.location_activity_house_title, R.string.location_activity_house, R.drawable.beaney_house_of_art_and_knowledge));
            }
        };

        this.linearLayoutMoreObject = (LinearLayout) this.findViewById(R.id.activity_location_more_object);
        this.linearLayoutMoreObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationActivity.this, ObjectInformationActivity.class);
                intent.putExtra(PLACE_DRAWABLE_ID, listPlaces.get(objectId).getIdDrawable());
                intent.putExtra(PLACE_ABOUT, listPlaces.get(objectId).getIdInfo());
                intent.putExtra(PLACE_TITLE, listPlaces.get(objectId).getIdTitle());
                startActivity(intent);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void location() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                /*Данный код применяется для Андроид 6.0 и выше. В коде происходит запрос андроиду
                  на разрешения доступа к локации. Вывод сообщения если доступ к локации запрещен. */
            Toast.makeText(this, "Access denied", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            return;
        }
        if (mMap != null) {
            /* Запрос всем провайдерам на обновление локации с интервалом 0 мили сек. */
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            manager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, listener);
            Location location;
            String[] providers = new String[]{LocationManager.GPS_PROVIDER,
                    LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER};
            for (int i = 0; i < providers.length; i++) {
                String provider = providers[i];
                location = manager.getLastKnownLocation(provider); //получение локации
                /*
                Локация может быть не найдена если провайдеры не отвечают
                Соответственно проверяем ее на пустоту.
                 */
                if (location != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                    objectCheck(location);
                } else
                    providers[i] = null;
            }
            if(!hasLocation(providers))
                Toast.makeText(this, "Location Service Not available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasLocation(String[] providers){
        return !(providers[0] == null && providers[1] == null && providers[2] == null);
    }

    private void objectCheck(Location location) {
        float[] distance = new float[2];
        for (int i = 0; i < listPlaces.size(); i++) {
            Place place = listPlaces.get(i);
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    place.getLatitude(), place.getLongitude(), distance);
            if (distance[0] < 200){
                linearLayoutMoreObject.setVisibility(View.VISIBLE);
                objectId = i;
                return;
            }
        }
        linearLayoutMoreObject.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        location();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.removeUpdates(listener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);//установка отображения точки.

        for(Place place : listPlaces){
            LatLng lng = new LatLng(place.getLatitude(), place.getLongitude());
            mMap.addMarker(new MarkerOptions().position(lng).title(getResources().getString(place.getIdInfo())).icon(BitmapDescriptorFactory.fromResource(R.drawable.castle_icon)));
        }
        location();
    }
}
