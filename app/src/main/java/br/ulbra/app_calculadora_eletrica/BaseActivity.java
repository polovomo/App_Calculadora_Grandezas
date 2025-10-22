package br.ulbra.app_calculadora_eletrica;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private boolean enableAnimations = true;

    // Método para desabilitar animações temporariamente
    public void setEnableAnimations(boolean enable) {
        this.enableAnimations = enable;
    }

    @Override
    public void finish() {
        super.finish();
        if (enableAnimations) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public void startActivity(android.content.Intent intent) {
        super.startActivity(intent);
        if (enableAnimations) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}