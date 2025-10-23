package br.ulbra.app_calculadora_eletrica.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.ulbra.app_calculadora_eletrica.R;
import br.ulbra.app_calculadora_eletrica.ui.home.LoginActivity;

public class RegistrarActivity extends AppCompatActivity {

    EditText txtNome, txtLogin, txtSenha, txtRepitaSenha;
    Button btnSalvar;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cadastro);

        txtNome = findViewById(R.id.txtNome);
        txtLogin = findViewById(R.id.txtLogin);
        txtSenha = findViewById(R.id.txtSenha);
        txtRepitaSenha = findViewById(R.id.txtRepitaSenha);
        btnSalvar = findViewById(R.id.btnSalvar);

        db = new DBHelper(this);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = txtLogin.getText().toString();
                String pas1 = txtSenha.getText().toString();
                String pas2 = txtRepitaSenha.getText().toString();

                if (userName.equals("")) {
                    Toast.makeText(RegistrarActivity.this, "Insira o LOGIN DO USUÁRIO", Toast.LENGTH_SHORT).show();
                } else if (pas1.equals("") || pas2.equals("")) {
                    Toast.makeText(RegistrarActivity.this, "Insira a SENHA DO USUÁRIO", Toast.LENGTH_SHORT).show();
                } else if (!pas1.equals(pas2)) {
                    Toast.makeText(RegistrarActivity.this, "As senhas não correspondem", Toast.LENGTH_SHORT).show();
                } else {

                    long res = db.criarUtilizador(userName, pas1);
                    if (res > 0) {
                        Toast.makeText(RegistrarActivity.this, "Registro OK", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();

                    } else {
                        Toast.makeText(RegistrarActivity.this, "Erro ao registrar usuário!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
