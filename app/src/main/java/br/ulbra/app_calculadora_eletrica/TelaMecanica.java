package br.ulbra.app_calculadora_eletrica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class TelaMecanica extends Fragment {

    private EditText txtValorInicial;
    private RadioGroup rgDe, rgPara;
    private Button btnConverter;
    private TextView txtResposta;

    private final Map<String, Double> unidades = new HashMap<String, Double>() {{
        put("km", 1000.0);
        put("hm", 100.0);
        put("dam", 10.0);
        put("m", 1.0);
        put("dm", 0.1);
        put("cm", 0.01);
        put("mm", 0.001);
    }};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mecanica, container, false);

        txtValorInicial = root.findViewById(R.id.txtValorInicial);
        rgDe = root.findViewById(R.id.rgDe);
        rgPara = root.findViewById(R.id.rgPara);
        btnConverter = root.findViewById(R.id.btnConverter);
        txtResposta = root.findViewById(R.id.txtResposta);

        btnConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valorStr = txtValorInicial.getText().toString().trim();

                if (valorStr.isEmpty()) {
                    Toast.makeText(getContext(), "Digite um valor!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double valor;
                try {
                    valor = Double.parseDouble(valorStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Valor inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int idDe = rgDe.getCheckedRadioButtonId();
                int idPara = rgPara.getCheckedRadioButtonId();

                if (idDe == -1 || idPara == -1) {
                    Toast.makeText(getContext(), "Selecione ambas as unidades", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton rbDe = root.findViewById(idDe);
                RadioButton rbPara = root.findViewById(idPara);

                if (rbDe == null || rbPara == null) {
                    Toast.makeText(getContext(), "Erro ao obter unidades selecionadas", Toast.LENGTH_SHORT).show();
                    return;
                }

                String unidadeDe = rbDe.getText().toString().toLowerCase().trim();
                String unidadePara = rbPara.getText().toString().toLowerCase().trim();

                if (!unidades.containsKey(unidadeDe) || !unidades.containsKey(unidadePara)) {
                    Toast.makeText(getContext(), "Unidade inválida!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double emMetros = valor * unidades.get(unidadeDe);
                double convertido = emMetros / unidades.get(unidadePara);

                String resultado = String.format("%.3f %s = %.3f %s", valor, unidadeDe, convertido, unidadePara);
                txtResposta.setText(resultado);
            }
        });

        return root;
    }
}
