package emcorp.studio.ahpschool;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emcorp.studio.ahpschool.Adapter.SchoolAdapter;
import emcorp.studio.ahpschool.Library.Constant;
import emcorp.studio.ahpschool.Library.SharedPrefManager;

public class SearchAhpActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    List<String> listid = new ArrayList<String>();
    List<String> listnpsn = new ArrayList<String>();
    List<String> listnama_sekolah = new ArrayList<String>();
    List<String> listalamat = new ArrayList<String>();
    List<String> listjumlah_siswa = new ArrayList<String>();
    List<String> listjumlah_guru = new ArrayList<String>();
    List<String> listkepala_sekolah = new ArrayList<String>();
    List<String> listtelepon = new ArrayList<String>();
    List<String> listkondisi_lingkungan = new ArrayList<String>();
    List<String> listakreditasi = new ArrayList<String>();
    List<String> listlongitude = new ArrayList<String>();
    List<String> listlatitude = new ArrayList<String>();
    List<String> listfoto = new ArrayList<String>();
    List<String> listcreated = new ArrayList<String>();
    List<String> listdistance = new ArrayList<String>();
    ListView list;
    private ProgressDialog progressDialog;
    LocationManager locationManager;
    String longitude = "", latitude = "";
    String longitude_cari = "", latitude_cari = "", address_cari = "";
    String jarak = "", akreditasi = "", lingkungan = "";

    private static final String TAG = "SearchAhpActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private boolean needLoad = true;
    String namaSekolah = "";
    Spinner spinLokasi;
    List<String> listKelurahan = new ArrayList<String>();
    List<String> listLongitude = new ArrayList<String>();
    List<String> listLatitude = new ArrayList<String>();
    EditText edtSearch, edtLocation;
    ImageButton btnSearch;
    String longitudeKel = "", latitudeKel = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ahp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        list = (ListView) findViewById(R.id.listView);
        spinLokasi = (Spinner) findViewById(R.id.spinLokasi);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        getSupportActionBar().setTitle("Advanced Search");

        jarak = SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.JARAK);
        akreditasi = SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.AKREDITASI);
        lingkungan = SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.LINGKUNGAN);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
//            jarak = extras.getString("jarak");
//            akreditasi = extras.getString("akreditasi");
//            lingkungan = extras.getString("lingkungan");
            longitude_cari = extras.getString("longitude");
            latitude_cari = extras.getString("latitude");
            address_cari = extras.getString("address");
            edtLocation.setText(address_cari);
            if(longitude_cari!=null){
                if(!longitude_cari.equals("")){
                    needLoad = false;
                    LoadProcess(namaSekolah,latitude_cari,longitude_cari);
                }
            }

        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        checkLocation();
        LoadSpinner();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namaSekolah = edtSearch.getText().toString();
                if(spinLokasi.getSelectedItemPosition()>1){
                    latitudeKel = listLatitude.get(spinLokasi.getSelectedItemPosition());
                    longitudeKel = listLongitude.get(spinLokasi.getSelectedItemPosition());
                    LoadProcess(namaSekolah,latitudeKel,longitudeKel);
                }else if(spinLokasi.getSelectedItemPosition()==0){
                    LoadProcess(namaSekolah,latitude,longitude);
                }else if(spinLokasi.getSelectedItemPosition()==1){
                    //Gunakan Custom Location
                    if(!latitude_cari.equals("")){
                        LoadProcess(namaSekolah,latitude_cari,longitude_cari);
                    }else{
                        Toast.makeText(getApplicationContext(),"Anda belum menggunakan custom lokasi!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        spinLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==1){
                    edtLocation.setEnabled(true);
                }else{
                    edtLocation.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(SearchAhpActivity.this,MapsActivity.class);
                intent.putExtra("module","search");
                startActivity(intent);
                finish();
                return true;
            }
        });
    }

    public void LoadSpinner() {
        String[] kelurahanArray = getResources().getStringArray(R.array.kelurahan);
        listKelurahan.clear();
        listLongitude.clear();
        listLatitude.clear();
        listKelurahan.add("Lokasi Saya");
        listLongitude.add("0");
        listLatitude.add("0");
        listKelurahan.add("Cari Lokasi");
        listLongitude.add("0");
        listLatitude.add("0");
        for(int i=0;i<kelurahanArray.length;i++){
            String[] data = kelurahanArray[i].split("#");
            listKelurahan.add(data[0]);
            listLatitude.add(data[1]);
            listLongitude.add(data[2]);
        }
        ArrayAdapter<String> nominalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listKelurahan);
        nominalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLokasi.setAdapter(nominalAdapter);

        if(!longitude_cari.equals("")){
            spinLokasi.setSelection(1);
        }
    }

    public void LoadProcess(final String search, final String latitude, final String longitude){
        needLoad = false;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CETAK",response);
                        progressDialog.dismiss();
                        listid.clear();
                        listnpsn.clear();
                        listnama_sekolah.clear();
                        listalamat.clear();
                        listjumlah_siswa.clear();
                        listjumlah_guru.clear();
                        listkepala_sekolah.clear();
                        listtelepon.clear();
                        listkondisi_lingkungan.clear();
                        listakreditasi.clear();
                        listlongitude.clear();
                        listlatitude.clear();
                        listfoto.clear();
                        listcreated.clear();
                        listdistance.clear();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(response.indexOf("null")>0){
                                Toast.makeText(getApplicationContext(),"Tidak ada data", Toast.LENGTH_SHORT).show();
                                list.setVisibility(View.GONE);
                            }else{
                                JSONArray jsonArray = obj.getJSONArray("hasil");
                                if(jsonArray.length()==0){
                                    Toast.makeText(getApplicationContext(),"Tidak ada data", Toast.LENGTH_SHORT).show();
                                    list.setVisibility(View.GONE);
                                }else{
                                    list.setVisibility(View.VISIBLE);
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        JSONObject isiArray = jsonArray.getJSONObject(i);
                                        String id = isiArray.getString("id");
                                        String npsn = isiArray.getString("npsn");
                                        String nama_sekolah = isiArray.getString("nama_sekolah");
                                        String alamat = isiArray.getString("alamat");
                                        String jumlah_siswa = isiArray.getString("jumlah_siswa");
                                        String jumlah_guru = isiArray.getString("jumlah_guru");
                                        String kepala_sekolah = isiArray.getString("kepala_sekolah");
                                        String telepon = isiArray.getString("telepon");
                                        String kondisi_lingkungan = isiArray.getString("kondisi_lingkungan");
                                        String akreditasi = isiArray.getString("akreditasi");
                                        String longitude = isiArray.getString("longitude");
                                        String latitude = isiArray.getString("latitude");
                                        String foto = isiArray.getString("foto");
                                        String created = isiArray.getString("created");
                                        String distance = isiArray.getString("distance");
                                        listid.add(id);
                                        listnpsn.add(npsn);
                                        listnama_sekolah.add(nama_sekolah);
                                        listalamat.add(alamat);
                                        listjumlah_siswa.add(jumlah_siswa);
                                        listjumlah_guru.add(jumlah_guru);
                                        listkepala_sekolah.add(kepala_sekolah);
                                        listtelepon.add(telepon);
                                        listkondisi_lingkungan.add(kondisi_lingkungan);
                                        listakreditasi.add(akreditasi);
                                        listlongitude.add(longitude);
                                        listlatitude.add(latitude);
                                        listfoto.add(foto);
                                        listcreated.add(created);
                                        listdistance.add(distance);
                                    }
                                    getAllData();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", Constant.FUNCTION_LISTSCHOOLAHP);
                params.put("key", Constant.KEY);
                params.put("longitude", longitude);
                params.put("latitude", latitude);
                params.put("jarak", jarak);
                params.put("akreditasi", akreditasi);
                params.put("lingkungan", lingkungan);
                params.put("nama", search);
                Log.d("CETAK - KIRIM",longitude +" - "+latitude);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void getAllData(){
        list.setAdapter(null);
        SchoolAdapter adapter = new SchoolAdapter(this, listid,listnpsn,listnama_sekolah,listalamat,listjumlah_siswa,listjumlah_guru,listkepala_sekolah,listtelepon,listkondisi_lingkungan,listakreditasi,listlongitude,listlatitude,listfoto,listcreated,listdistance);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(SearchAhpActivity.this,SearchActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchAhpActivity.this,SearchActivity.class));
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
//            longitude = String.valueOf(mLocation.getLongitude());
//            latitude = String.valueOf(mLocation.getLatitude());
            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());

//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        longitude = String.valueOf(location.getLongitude());
        latitude = String.valueOf(location.getLatitude());
        Log.d("CETAK",msg);
        if(needLoad){
            LoadProcess(namaSekolah,latitude,longitude);
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
