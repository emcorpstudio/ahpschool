package emcorp.studio.ahpschool;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emcorp.studio.ahpschool.Adapter.SchoolAdapter;
import emcorp.studio.ahpschool.Library.Constant;

//public class SchoolActivity extends AppCompatActivity implements LocationListener {
public class SchoolActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        list = (ListView) findViewById(R.id.listView);
        getSupportActionBar().setTitle("School List");
        LoadProcess();
    }


    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = String.valueOf(location.getLongitude());
        latitude = String.valueOf(location.getLatitude());
//        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, (LocationListener) this);
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//        }
    }

    public void LoadProcess(){
        getLocation();
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
                params.put("function", Constant.FUNCTION_LISTSCHOOL);
                params.put("key", Constant.KEY);
                params.put("longitude", longitude);
//                params.put("longitude", "112.5541225");
                params.put("latitude", latitude);
                Log.d("CETAK - KIRIM",longitude +" - "+latitude);
//                params.put("latitude", "-7.1721493");
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
            startActivity(new Intent(SchoolActivity.this,MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SchoolActivity.this,MainActivity.class));
        finish();
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        Log.d("CETAK","Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
//        longitude = String.valueOf(location.getLongitude());
//        latitude = String.valueOf(location.getLatitude());
//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            Log.d("CETAK","\n"+addresses.get(0).getAddressLine(0)+", "+
//                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
//        }catch(Exception e){
//
//        }
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Toast.makeText(SchoolActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
}
