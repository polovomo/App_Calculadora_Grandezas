package br.ulbra.app_calculadora_eletrica.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.ulbra.app_calculadora_eletrica.MainActivity;
import br.ulbra.app_calculadora_eletrica.R;

public class LoginActivity extends AppCompatActivity {
    EditText txtLoginLogin, txtSenhaLogin;
    Button btnEntrar, btnCadastre;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        db = new DBHelper(this);
        txtLoginLogin = (EditText)findViewById(R.id.txtLoginLogin);
        txtSenhaLogin = (EditText)findViewById(R.id.txtSenhaLogin);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        btnCadastre = (Button)findViewById(R.id.btnCadastre);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=txtLoginLogin.getText().toString();
                String password=txtSenhaLogin.getText().toString();
                if(username.equals("")){
                    Toast.makeText(LoginActivity.this,"Usuario não inserido, tente novamente",Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    Toast.makeText(LoginActivity.this,"Senha não inserida, tente novamente",Toast.LENGTH_SHORT).show();
                }else{
                    String res = db.validarLogin(username,password);
                    if(res.equals("OK")){
                        Toast.makeText(LoginActivity.this,"Login OK !!",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Login ou Senha errado(s)!!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
                startActivity(intent);
            }
        });
    }
}


