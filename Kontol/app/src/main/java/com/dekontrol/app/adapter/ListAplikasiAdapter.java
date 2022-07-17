package com.dekontrol.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dekontrol.app.Constant;
import com.dekontrol.app.R;
import com.dekontrol.app.activity.DetailAplikasi;
import com.dekontrol.app.activity.ListAplikasi;
import com.dekontrol.app.model.Aplikasi;
import com.dekontrol.app.model.Developer;

import java.util.List;

public class ListAplikasiAdapter extends RecyclerView.Adapter<ListAplikasiAdapter.HolderView>{

    private List<Aplikasi> listAplikasi;
    private Context context;
    private Developer developer;

    public ListAplikasiAdapter(List<Aplikasi> listAplikasi, Context context, Developer developer) {
        this.listAplikasi = listAplikasi;
        this.context = context;
        this.developer = developer;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aplikasi, parent, false);
        return new HolderView(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
        final Aplikasi aplikasi = listAplikasi.get(position);
        holder.wadah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailAplikasi.class);
                intent.putExtra(Constant.KODE_INTENT, aplikasi.getId());
                intent.putExtra("id_developer", aplikasi.getIdDeveloper());
                intent.putExtra("nama_aplikasi", aplikasi.getNama());
                ((ListAplikasi) context).bukaDetail(intent);
            }
        });
        holder.namaAplikasi.setText(aplikasi.getNama());
        holder.pakage.setText(aplikasi.getPacage());
        holder.trafik.setText(String.valueOf(aplikasi.getTrafikTerima() + aplikasi.getTrafikTolak()));

        if(developer.getStatus().equals(Constant.KODE_ON) && aplikasi.getStatus().equals(Constant.KODE_ON)){
            if(aplikasi.getStatusBanner().equals(Constant.KODE_ONDEV) || aplikasi.getStatusBanner().equals(Constant.KODE_ONAPP)
                    || aplikasi.getStatusInterstitial().equals(Constant.KODE_ONDEV) || aplikasi.getStatusInterstitial().equals(Constant.KODE_ONAPP)
                    || aplikasi.getStatusVideo().equals(Constant.KODE_ONDEV) || aplikasi.getStatusVideo().equals(Constant.KODE_ONAPP)){
                holder.adaIklanAktif.setTextColor(ContextCompat.getColor(context, R.color.hijau));
                holder.adaIklanAktif.setText("ON");
            }else{
                holder.adaIklanAktif.setTextColor(ContextCompat.getColor(context, R.color.merah));
                holder.adaIklanAktif.setText("OFF");
            }
        }else{
            holder.adaIklanAktif.setTextColor(ContextCompat.getColor(context, R.color.merah));
            holder.adaIklanAktif.setText("OFF");
        }
    }

    @Override
    public int getItemCount() {
        return listAplikasi.size();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private LinearLayout wadah;
        private TextView namaAplikasi, pakage, trafik, adaIklanAktif;

        public HolderView(View itemView) {
            super(itemView);
            wadah = itemView.findViewById(R.id.wadahItemAplikasi);
            namaAplikasi = itemView.findViewById(R.id.namaAplikasi);
            pakage = itemView.findViewById(R.id.pakage);
            trafik = itemView.findViewById(R.id.jumlahTrafikAplikasi);
            adaIklanAktif = itemView.findViewById(R.id.adaIklanAktif);
        }
    }
}
