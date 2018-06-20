package com.example.softcons.projetoagenda;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.softcons.projetoagenda.database.DadosOpenHelper;
import com.example.softcons.projetoagenda.dominio.entidade.Agenda;
import com.example.softcons.projetoagenda.dominio.repositorio.AgendaRepositorio;

public class ActCadAgenda extends AppCompatActivity {

    private EditText edtDescricao;
    private EditText edtTipo;
    private EditText edtHora;
    private EditText edtData;
    private RelativeLayout rLCadAgenda;

    private boolean atualizar = false;

    private int codigo;
    private String descricao, tipo, hora, dataAgenda;

    private AgendaRepositorio agendaRepositorio;
    private Agenda agenda;

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_agenda);

        edtDescricao      = (EditText)findViewById(R.id.edtDescricao);
        edtTipo  = (EditText)findViewById(R.id.edtTipo);
        edtHora     = (EditText)findViewById(R.id.edtHora);
        edtData  = (EditText)findViewById(R.id.edtData);

        rLCadAgenda = (RelativeLayout) findViewById(R.id.rLCadAgenda);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            codigo = extras.getInt("codigo");
            descricao = extras.getString("DESCRICAO");
            tipo = extras.getString("TIPO");
            hora = extras.getString("HORA");
            dataAgenda = extras.getString("DATA");

            atualizar = true;
        }

        if(atualizar == true){
            edtDescricao.setText(descricao);
            edtTipo.setText(tipo);
            edtHora.setText(hora);
            edtData.setText(dataAgenda);

        }

        criarConexao();
    }

    //Função para verificar conexao com o banco de dados
    private void criarConexao(){
        //A conexao é criada e verificada retornano message_conexao_criada ou dar erro
        try {
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao =  dadosOpenHelper.getWritableDatabase();
            Snackbar.make(rLCadAgenda, R.string.message_conexao_criada, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_ok, null).show();

            agendaRepositorio = new AgendaRepositorio(conexao);

        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_erro);
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_agenda, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Validação dos campos e retornando o foco para o campo ----------------------------------------

    //Metodo para a inserção dos dados
    private void confirmar(){

        agenda = new Agenda();


        if(validaCampos() == false){

            // As linhas abaixo são utilizadas para ocorreu algum problema na inserção
            try {

                agendaRepositorio.inserir(agenda);
                Intent intent = new Intent(ActCadAgenda.this, ActMainAgenda.class);
                startActivity(intent);
                finish();

            }catch (SQLException ex){

                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.title_erro);
                dlg.setNeutralButton(R.string.action_ok, null);
                dlg.show();
            }

        }

    }

    private void atualizarAgenda(){

        agenda = new Agenda();

        agenda.codigo = codigo;
        agenda.descricao = edtDescricao.getText().toString();
        agenda.tipo = edtTipo.getText().toString();
        agenda.hora = edtHora.getText().toString();
        agenda.dataAgenda = edtData.getText().toString();

        if(validaCampos() == false){


            try {

                agendaRepositorio.alterar(agenda);

                Intent intent = new Intent(ActCadAgenda.this, ActMainAgenda.class);
                startActivity(intent);
                finish();

            }catch (SQLException ex){

                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle(R.string.title_erro);
                dlg.setNeutralButton(R.string.action_ok, null);
                dlg.show();
            }

        }

    }

    //----------------------------------------------------------------------------------------------
    private boolean validaCampos(){

        boolean res = false;
        String descricao     = edtDescricao.getText().toString();
        String tipo = edtTipo.getText().toString();
        String hora    = edtHora.getText().toString();
        String dataAgenda = edtData.getText().toString();


        agenda.descricao    = descricao;
        agenda.tipo         = tipo;
        agenda.hora         = hora;
        agenda.dataAgenda   = dataAgenda;

        if (res = isCampoVazio(descricao)){
            edtDescricao.requestFocusFromTouch();
        }else{
            if (res = isCampoVazio(tipo)){
                edtTipo.requestFocus();
            }else {
                if (res = !isCampoVazio(hora)){
                    edtHora.requestFocus();
                }else {
                    if (res = isCampoVazio(dataAgenda)){
                        edtData.requestFocus();
                    }
                }
            }
        }


        //Mensagem de campos invaidos ou em branco! ------------------------------------------------
        if (res){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.title_aviso);
            dlg.setMessage(R.string.message_campos_invalidos_branco);
            dlg.setNeutralButton(R.string.action_ok, null);
            dlg.show();
        }
        //------------------------------------------------------------------------------------------
        return res;
    }

    //----------------------------------------------------------------------------------------------

    //Função para verificar se o campo esta vazio --------------------------------------------------
    private boolean isCampoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }


    //Função para verificar qual item foi selecionado ----------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_ok:
                if(atualizar == false){
                    confirmar();
                } else {
                    atualizarAgenda();
                }
                //Toast.makeText(this, "Botão OK selecionado! ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_cancelar:

                //Toast.makeText(this, "Botão Cancelar selecionado! ", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //----------------------------------------------------------------------------------------------
}
