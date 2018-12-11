package emcorp.studio.ahpschool.Adapter;

/**
 * Created by ASUS on 27/11/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import emcorp.studio.ahpschool.Library.Constant;
import emcorp.studio.ahpschool.R;

public class SchoolAdapter extends ArrayAdapter<String> {
    private final Activity context;
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
    public SchoolAdapter(Activity context,
                         List<String> listid,List<String> listnpsn,List<String> listnama_sekolah,List<String> listalamat,List<String> listjumlah_siswa,List<String> listjumlah_guru,List<String> listkepala_sekolah,List<String> listtelepon,List<String> listkondisi_lingkungan,List<String> listakreditasi,List<String> listlongitude,List<String> listlatitude,List<String> listfoto,List<String> listcreated,List<String> listdistance) {
        super(context, R.layout.school_list, listid);

        this.context = context;
        this.listid = listid;
        this.listnpsn = listnpsn;
        this.listnama_sekolah = listnama_sekolah;
        this.listalamat = listalamat;
        this.listjumlah_siswa = listjumlah_siswa;
        this.listjumlah_guru = listjumlah_guru;
        this.listkepala_sekolah = listkepala_sekolah;
        this.listtelepon = listtelepon;
        this.listkondisi_lingkungan = listkondisi_lingkungan;
        this.listakreditasi = listakreditasi;
        this.listlongitude = listlongitude;
        this.listlatitude = listlatitude;
        this.listfoto = listfoto;
        this.listcreated = listcreated;
        this.listdistance = listdistance;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.school_list, null, true);
        TextView tvNama = (TextView) rowView.findViewById(R.id.tvNama);
        TextView tvAlamat = (TextView) rowView.findViewById(R.id.tvAlamat);
        TextView tvLingkungan = (TextView) rowView.findViewById(R.id.tvLingkungan);
        TextView tvAkreditasi = (TextView) rowView.findViewById(R.id.tvAkreditasi);
        TextView tvJarak = (TextView) rowView.findViewById(R.id.tvJarak);
        TextView tvTelepon = (TextView) rowView.findViewById(R.id.tvTelepon);
        ImageView imgSchool = (ImageView) rowView.findViewById(R.id.imgSchool);

        tvNama.setText(listnama_sekolah.get(position));
        tvAlamat.setText(listalamat.get(position));
        tvLingkungan.setText("Kondisi Lingkungan : "+listkondisi_lingkungan.get(position));
        tvAkreditasi.setText("Akreditasi : "+listakreditasi.get(position));
        tvJarak.setText("Jarak : "+listdistance.get(position)+" Km");
        tvTelepon.setText("No Telp : "+listtelepon.get(position));
        Picasso.with(getContext())
                .load(Constant.PICT_URL+listfoto.get(position))
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .noFade()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .resize(120, 120)
                .into(imgSchool);
        return rowView;
    }


}