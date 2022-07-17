package com.dekontrol.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dekontrol.app.R;
import com.dekontrol.app.activity.ListDeveloper;
import com.dekontrol.app.model.Developer;

import java.util.List;

public class ListDeveloperAdapter extends RecyclerView.Adapter<ListDeveloperAdapter.HolderView>{

    private List<Developer> listDeveloper;
    private Context context;

    public ListDeveloperAdapter(List<Developer> listDeveloper, Context context) {
        this.listDeveloper = listDeveloper;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_developer, parent, false);
        return new HolderView(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderView holder, @SuppressLint("RecyclerView") final int position) {
        holder.wadah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListDeveloper) context).bukaListApp(listDeveloper.get(position).getId(), listDeveloper.get(position).getNama());
            }
        });
        holder.namaDeveloper.setText(listDeveloper.get(position).getNama());
        holder.jumlahApp.setText("Total : " + String.valueOf(listDeveloper.get(position).getJumlahApp() +" App"));
        holder.jumlahTrafik.setText(String.valueOf(listDeveloper.get(position).getJumlahTrafik()));
    }

    @Override
    public int getItemCount() {
        return listDeveloper.size();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private LinearLayout wadah;
        private TextView namaDeveloper, jumlahApp, jumlahTrafik;

        public HolderView(View itemView) {
            super(itemView);
            wadah = itemView.findViewById(R.id.wadahItemDeveloper);
            namaDeveloper = itemView.findViewById(R.id.namaDeveloper);
            jumlahApp = itemView.findViewById(R.id.jumlahApp);
            jumlahTrafik = itemView.findViewById(R.id.jumlahTrafik);

        }
    }
}
