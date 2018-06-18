package com.example.softcons.projetoagenda.database;

public class ScriptDLL {

    public static String getCreateTableAgenda(){
        StringBuilder sql = new StringBuilder();

        sql.append("    CREATE TABLE IF NOT EXISTS AGENDA (");
        sql.append("        CODIGO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
        sql.append("        DESCRICAO        VARCHAR(250) NOT NULL DEFAULT(''),");
        sql.append("        TIPO    VARCHAR(255) NOT NULL DEFAULT(''),");
        sql.append("        HORA       VARCHAR(250) NOT NULL DEFAULT(''),");
        sql.append("        DATAAGENDA    VARCHAR(200) NOT NULL DEFAULT('') )");

        return sql.toString();

    }

}
