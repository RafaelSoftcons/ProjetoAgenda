package com.example.softcons.projetoagenda;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.softcons.projetoagenda.dominio.entidade.Agenda;

import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolderAgenda> {
    private List<Agenda> dados;

    public AgendaAdapter(List<Agenda> dados){
        this.dados = dados;
    }


    @Override
    public AgendaAdapter.ViewHolderAgenda onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.linha_cliente, null, false);
        ViewHolderAgenda holderAgenda = new ViewHolderAgenda(view);

        return holderAgenda;
    }

    @Override
    public void onBindViewHolder(AgendaAdapter.ViewHolderAgenda holder, int position) {

        if ((dados != null) && (dados.size() > 0)){
            Agenda agenda = dados.get(position);

            holder.txtDescricao.setText(agenda.descricao);
            holder.txtTipo.setText(agenda.tipo);
        }
    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderAgenda extends RecyclerView.ViewHolder{

        public TextView txtDescricao;
        public TextView txtTipo;

        public ViewHolderAgenda(View itemView) {
            super(itemView);
            txtDescricao      = (TextView) itemView.findViewById(R.id.txtDescricao);
            txtTipo  = (TextView) itemView.findViewById(R.id.txtTipo);
        }
    }
}
