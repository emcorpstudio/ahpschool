package emcorp.studio.ahpschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import emcorp.studio.ahpschool.Library.Constant;
import emcorp.studio.ahpschool.Library.SharedPrefManager;

public class SearchActivity extends AppCompatActivity {
    Button btnSearch;
    Spinner spinJarak, spinAkreditasi, spinLingkungan;
    List<String> listJarak = new ArrayList<String>();
    List<String> listAkreditasi = new ArrayList<String>();
    List<String> listLingkungan = new ArrayList<String>();

    List<String> listKelurahan = new ArrayList<String>();
    List<String> listLongitude = new ArrayList<String>();
    List<String> listLatitude = new ArrayList<String>();
    String longitude_cari = "", latitude_cari = "", address_cari = "";
    String longitude = "", latitude = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Advanced Search");

        btnSearch = (Button)findViewById(R.id.btnSearch);
        spinJarak = (Spinner) findViewById(R.id.spinJarak);
        spinAkreditasi = (Spinner) findViewById(R.id.spinAkreditasi);
        spinLingkungan = (Spinner) findViewById(R.id.spinLingkungan);

        LoadSpinner();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            longitude_cari = extras.getString("longitude");
            latitude_cari = extras.getString("latitude");
            address_cari = extras.getString("address");
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinJarak.getSelectedItemPosition()<=0){
                    Toast.makeText(getApplicationContext(),"Jarak belum dipilih!",Toast.LENGTH_SHORT).show();
                }else{
                    if(spinAkreditasi.getSelectedItemPosition()<=0){
                        Toast.makeText(getApplicationContext(),"Akreditasi belum dipilih!",Toast.LENGTH_SHORT).show();
                    }else{
                        if(spinLingkungan.getSelectedItemPosition()<=0){
                            Toast.makeText(getApplicationContext(),"Lingkungan belum dipilih!",Toast.LENGTH_SHORT).show();
                        }else{
                            SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.JARAK,spinJarak.getSelectedItem().toString());
                            SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.AKREDITASI,spinAkreditasi.getSelectedItem().toString());
                            SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.LINGKUNGAN,spinLingkungan.getSelectedItem().toString());
                            Intent i = new Intent(SearchActivity.this,SearchAhpActivity.class);
                            i.putExtra("jarak",spinJarak.getSelectedItem().toString());
                            i.putExtra("akreditasi",spinAkreditasi.getSelectedItem().toString());
                            i.putExtra("lingkungan",spinLingkungan.getSelectedItem().toString());
                            i.putExtra("longitude",longitude);
                            i.putExtra("latitude",latitude);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }
        });
    }

    public void LoadSpinner() {
        String[] dataArray = getResources().getStringArray(R.array.data_search);
        listJarak.clear();
        listAkreditasi.clear();
        listLingkungan.clear();
        listJarak.add("Pilih Jarak");
        listAkreditasi.add("Pilih Akreditasi");
        listLingkungan.add("Pilih Kondisi Lingkungan");
        for(int i=0;i<dataArray.length;i++){
            listJarak.add(dataArray[i]);
            listAkreditasi.add(dataArray[i]);
            listLingkungan.add(dataArray[i]);
        }
        ArrayAdapter<String> jarakAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listJarak);
        jarakAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinJarak.setAdapter(jarakAdapter);

        ArrayAdapter<String> akreditasiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAkreditasi);
        akreditasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAkreditasi.setAdapter(akreditasiAdapter);

        ArrayAdapter<String> lingkunganAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listLingkungan);
        lingkunganAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLingkungan.setAdapter(lingkunganAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(SearchActivity.this,MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchActivity.this,MainActivity.class));
        finish();
    }
}
