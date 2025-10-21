package br.ulbra.app_calculadora_eletrica;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexaoEstado extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "banco_estado";
    private static final int VERSAO = 1;

    public ConexaoEstado(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE estado (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "sigla TEXT UNIQUE NOT NULL," +
                "kwh REAL NOT NULL" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS estado");
        onCreate(db);
    }
}