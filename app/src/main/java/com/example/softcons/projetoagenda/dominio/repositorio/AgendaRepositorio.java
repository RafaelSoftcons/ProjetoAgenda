package com.example.softcons.projetoagenda.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.softcons.projetoagenda.dominio.entidade.Agenda;

import java.util.ArrayList;
import java.util.List;

public class AgendaRepositorio {
    private SQLiteDatabase conexao;


    public AgendaRepositorio(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void inserir(Agenda agenda){
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRICAO", agenda.descricao);
        contentValues.put("TIPO", agenda.tipo);
        contentValues.put("HORA", agenda.hora);
        contentValues.put("DATAAGENDA", agenda.dataAgenda);


        conexao.insertOrThrow("AGENDA", null, contentValues);
    }

    public void excluir(int codigo){
        String[] paramentro = new String[1];
        paramentro[0] = String.valueOf (codigo);

        conexao.delete("AGENDA", "CODIGO = ?", paramentro);
    }

    public void alterar(Agenda agenda){
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESCRICAO", agenda.descricao);
        contentValues.put("TIPO", agenda.tipo);
        contentValues.put("HORA", agenda.hora);
        contentValues.put("DATAAGENDA", agenda.dataAgenda);

        String[] paramentro = new String[1];
        paramentro[0] = String.valueOf (agenda.codigo);

        conexao.update("AGENDA", contentValues, "CODIGO = ?", paramentro);
    }

    public List<Agenda> buscaTodos(){

        List<Agenda> agenda= new ArrayList<Agenda>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CODIGO, DESCRICAO, TIPO, HORA, DATAAGENDA");
        sql.append("    FROM AGENDA");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if (resultado.getCount() > 0){

            resultado.moveToFirst();

            do {
                Agenda agen = new Agenda();
                agen.codigo      = resultado.getInt(resultado.getColumnIndexOrThrow("CODIGO"));
                agen.descricao        = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"));
                agen.tipo    = resultado.getString(resultado.getColumnIndexOrThrow("TIPO"));
                agen.hora       = resultado.getString(resultado.getColumnIndexOrThrow("HORA"));
                agen.dataAgenda    = resultado.getString(resultado.getColumnIndexOrThrow("DATAAGENDA"));

                agenda.add(agen);

            }while (resultado.moveToNext());


        }

        return agenda;
    }

    public Agenda buscarAgenda(int codigo){

        Agenda agenda = new Agenda();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CODIGO, DESCRICAO, TIPO, HORA, DATAAGENDA");
        sql.append("    FROM AGENDA");
        sql.append("    WHERE CODIGO = ?");

        String[] paramentro = new String[1];
        paramentro[0] = String.valueOf (codigo);

        Cursor resultado = conexao.rawQuery(sql.toString(), paramentro);

        if (resultado.getCount() > 0){

            resultado.moveToFirst();

            agenda.codigo      = resultado.getInt(resultado.getColumnIndexOrThrow("CODIGO"));
            agenda.descricao        = resultado.getString(resultado.getColumnIndexOrThrow("DESCRICAO"));
            agenda.tipo    = resultado.getString(resultado.getColumnIndexOrThrow("TIPO"));
            agenda.hora       = resultado.getString(resultado.getColumnIndexOrThrow("HORA"));
            agenda.dataAgenda    = resultado.getString(resultado.getColumnIndexOrThrow("DATAAGENDA"));

            return agenda;
        }
        return null;
    }
}
