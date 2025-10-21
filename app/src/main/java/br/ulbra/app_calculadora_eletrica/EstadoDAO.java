package br.ulbra.app_calculadora_eletrica;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {
    private ConexaoEstado conexao;
    private SQLiteDatabase banco;

    public EstadoDAO(Context context){
        conexao = new ConexaoEstado(context);
        banco = conexao.getWritableDatabase();
    }

    public List<Estado> buscarTodosEstados() {
        List<Estado> estados = new ArrayList<>();

        Cursor cursor = banco.query("estado",
                new String[]{"id", "sigla", "kwh"},
                null, null, null, null, "sigla");

        if (cursor.moveToFirst()) {
            do {
                Estado estado = new Estado();
                estado.setId(cursor.getInt(0));
                estado.setSigla(cursor.getString(1));
                estado.setKwh(cursor.getDouble(2));
                estados.add(estado);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return estados;
    }

    public void popularTodosEstados() {
        String[][] estados = {
                {"AC", "0.95"}, {"AL", "0.88"}, {"AP", "0.82"}, {"AM", "0.85"},
                {"BA", "0.78"}, {"CE", "0.75"}, {"DF", "0.68"}, {"ES", "0.72"},
                {"GO", "0.74"}, {"MA", "0.80"}, {"MT", "0.76"}, {"MS", "0.73"},
                {"MG", "0.65"}, {"PA", "0.83"}, {"PB", "0.79"}, {"PR", "0.62"},
                {"PE", "0.77"}, {"PI", "0.81"}, {"RJ", "0.70"}, {"RN", "0.78"},
                {"RS", "0.67"}, {"RO", "0.90"}, {"RR", "0.89"}, {"SC", "0.64"},
                {"SP", "0.60"}, {"SE", "0.85"}, {"TO", "0.88"}
        };

        for (String[] estado : estados) {
            ContentValues values = new ContentValues();
            values.put("sigla", estado[0]);
            values.put("kwh", estado[1]);
            banco.insert("estado", null, values);
        }
    }

    public void fecharConexao() {
        if (banco != null && banco.isOpen()) {
            banco.close();
        }
    }
}