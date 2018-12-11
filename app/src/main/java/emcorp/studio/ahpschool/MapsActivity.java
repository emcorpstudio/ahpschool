package emcorp.studio.ahpschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String longitude = "", latitude = "", address = "";
    Button btnSave;
    String module = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnSave = (Button)findViewById(R.id.btnSave);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            module = extras.getString("module");
        }

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
                longitude = String.valueOf(place.getLatLng().longitude);
                latitude = String.valueOf(place.getLatLng().latitude);
                address = place.getName().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(longitude.equals("")){
                    Toast.makeText(getApplicationContext(),"Anda belum memilih lokasi!",Toast.LENGTH_SHORT).show();
                }else{
                    switch (module){
                        case "list" :
                            Intent i1 = null;
                            i1 = new Intent(MapsActivity.this,SchoolActivity.class);
                            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i1.putExtra("longitude",longitude);
                            i1.putExtra("latitude",latitude);
                            i1.putExtra("address",address);
                            startActivity(i1);
                            finish();
                            break;
                        case "ahp" :
                            Intent i2 = null;
                            i2 = new Intent(MapsActivity.this,AhpActivity.class);
                            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i2.putExtra("longitude",longitude);
                            i2.putExtra("latitude",latitude);
                            i2.putExtra("address",address);
                            startActivity(i2);
                            finish();
                            break;
                        case "search" :
                            Intent i3 = null;
                            i3 = new Intent(MapsActivity.this,SearchAhpActivity.class);
                            i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i3.putExtra("longitude",longitude);
                            i3.putExtra("latitude",latitude);
                            i3.putExtra("address",address);
                            startActivity(i3);
                            finish();
                            break;
                    }

                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-6.16156235, 106.74389124);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Jakarta Barat"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap = googleMap;
    }

    @Override
    public void onBackPressed() {
        switch (module){
            case "list" :
                Intent i1 = null;
                i1 = new Intent(MapsActivity.this,SchoolActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i1);
                finish();
                break;
            case "ahp" :
                Intent i2 = null;
                i2 = new Intent(MapsActivity.this,AhpActivity.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i2);
                finish();
                break;
            case "search" :
                Intent i3 = null;
                i3 = new Intent(MapsActivity.this,SearchAhpActivity.class);
                i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i3);
                finish();
                break;
        }
    }
}
