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
import com.dekontrol.app.activity.PromoteActivity;
import com.dekontrol.app.model.Promote;

import java.util.List;

public class ListPromoteAdapter extends RecyclerView.Adapter<ListPromoteAdapter.HolderView>{

    private List<Promote> listPromote;
    private Context context;

    public ListPromoteAdapter(List<Promote> listPromote, Context context) {
        this.listPromote = listPromote;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promote, parent, false);
        return new HolderView(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderView holder, @SuppressLint("RecyclerView") final int position) {
        int jumlahPersen = 0;
        for(Promote promote : listPromote){
            jumlahPersen += promote.getPersen();
        }
        jumlahPersen = 100-jumlahPersen;
        final int finalJumlahPersen = jumlahPersen;
        holder.wadah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PromoteActivity) context).bukaDetailPromote(listPromote.get(position).getId(), listPromote.get(position).getNama(), finalJumlahPersen);
            }
        });
        holder.namaApp.setText(listPromote.get(position).getNama());
        holder.packageApp.setText(listPromote.get(position).getPacage());
        holder.persen.setText(String.valueOf(listPromote.get(position).getPersen()) +"%");
    }

    @Override
    public int getItemCount() {
        return listPromote.size();
    }

    class HolderView extends RecyclerView.ViewHolder{

        private LinearLayout wadah;
        private TextView namaApp, packageApp, persen;

        public HolderView(View itemView) {
            super(itemView);
            wadah = itemView.findViewById(R.id.wadahItemPromote);
            namaApp = itemView.findViewById(R.id.namaApp);
            packageApp = itemView.findViewById(R.id.packageApp);
            persen = itemView.findViewById(R.id.persen);

        }
    }
}
