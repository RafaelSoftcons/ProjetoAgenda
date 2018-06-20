package com.example.softcons.projetoagenda;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;


import com.example.softcons.projetoagenda.database.DadosOpenHelper;
import com.example.softcons.projetoagenda.dominio.entidade.Agenda;
import com.example.softcons.projetoagenda.dominio.repositorio.AgendaRepositorio;
import com.example.softcons.projetoagenda.helper.RecyclerItemClickListener;

import java.util.List;

public class ActMainAgenda extends AppCompatActivity {


    private RecyclerView lsDados;
    private FloatingActionButton fab;
    private RelativeLayout layoutContentMain;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private AgendaRepositorio agendaRepositorio;
    private AgendaAdapter agendaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main_agenda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        lsDados = (RecyclerView) findViewById(R.id.lsDados);
        layoutContentMain = (RelativeLayout) findViewById(R.id.layoutContentMain);

        criarConexao();
        lsDados.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lsDados.setLayoutManager(linearLayoutManager);
        agendaRepositorio = new AgendaRepositorio(conexao);
        final List<Agenda> dados = agendaRepositorio.buscaTodos();
        agendaAdapter = new AgendaAdapter(dados);
        lsDados.setAdapter(agendaAdapter);


        lsDados.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), lsDados, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                final Agenda agenda = dados.get(position);

                AlertDialog alertDialog = new AlertDialog.Builder(ActMainAgenda.this).setTitle("Agenda").setMessage("Descricao : " + agenda.descricao
                        +"\n"+"Tipo : " + agenda.tipo + "\n" + "Hora : " + agenda.hora + "\n" + "Data : " + agenda.dataAgenda).setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(ActMainAgenda.this, ActCadAgenda.class);
                        intent.putExtra("codigo", agenda.codigo);
                        intent.putExtra("descricao", agenda.descricao);
                        intent.putExtra("tipo", agenda.tipo);
                        intent.putExtra("hora", agenda.hora);
                        intent.putExtra("data", agenda.dataAgenda);
                        startActivity(intent);

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setCancelable(false).show();// utilizado para impedir que a jaela de dialogo feihe ao clicar fora

            }

            @Override
            public void onLongItemClick(View view, int position) {

                final Agenda agenda = dados.get(position);

                AlertDialog alertDialog = new AlertDialog.Builder(ActMainAgenda.this).setTitle("Deseja realmente excluir ").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        agendaRepositorio.excluir(agenda.codigo);
                        Intent intent = new Intent(ActMainAgenda.this, ActMainAgenda.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setCancelable(false).show();

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));


    }


    //Função para verificar conexao com o banco de dados -------------------------------------------
    private void criarConexao() {
        //A conexao é criada e verificada retornano message_conexao_criada ou dar erro
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            Snackbar.make(layoutContentMain, R.string.message_conexao_criada, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_ok, null).show();

        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    //----------------------------------------------------------------------------------------------

    public void cadastrar(View view) {
        //chamando a tela Cadastro da Agenda
        Intent it = new Intent(ActMainAgenda.this, ActCadAgenda.class);
        startActivityForResult(it, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Agenda> dados = agendaRepositorio.buscaTodos();
        agendaAdapter = new AgendaAdapter(dados);
        lsDados.setAdapter(agendaAdapter);
    }

}
