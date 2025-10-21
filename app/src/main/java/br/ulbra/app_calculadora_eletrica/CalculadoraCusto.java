package br.ulbra.app_calculadora_eletrica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraCusto extends Fragment {

    private EditText editNomeUsuario, editNomeAparelho, editPotencia, editHorasDia;
    private Spinner spinnerEstado;
    private Button btnCalcular;

    private List<Estado> listaEstados;
    private EstadoDAO estadoDAO;
    private double precoKwhSelecionado = 0;
    private String estadoSelecionado = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculadora_custo, container, false);

        initComponents(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        estadoDAO = new EstadoDAO(requireContext());
        carregarEstados();
        configurarListeners();
    }

    private void initComponents(View view) {
        editNomeUsuario = view.findViewById(R.id.editNomeUsuario);
        editNomeAparelho = view.findViewById(R.id.editNomeAparelho);
        editPotencia = view.findViewById(R.id.editPotencia);
        editHorasDia = view.findViewById(R.id.editHorasDia);
        spinnerEstado = view.findViewById(R.id.spinnerEstado);
        btnCalcular = view.findViewById(R.id.btnCalcular);
    }

    private void carregarEstados() {
        if (estadoDAO.buscarTodosEstados().isEmpty()) {
            estadoDAO.popularTodosEstados();
        }

        listaEstados = estadoDAO.buscarTodosEstados();

        if (listaEstados.isEmpty()) {
            Toast.makeText(requireContext(), "Nenhum estado encontrado", Toast.LENGTH_LONG).show();
            return;
        }

        List<String> dadosSpinner = new ArrayList<>();
        dadosSpinner.add("-- Selecione --");

        for (Estado estado : listaEstados) {
            dadosSpinner.add(estado.getSigla() + " - R$ " + estado.getKwh());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                dadosSpinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
    }

    private void configurarListeners() {
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && listaEstados != null) {
                    Estado estado = listaEstados.get(position - 1);
                    precoKwhSelecionado = estado.getKwh();
                    estadoSelecionado = estado.getSigla();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnCalcular.setOnClickListener(v -> calcularConsumo());
    }

    private void calcularConsumo() {
        if (!validarCampos()) return;

        try {
            String nomeUsuario = editNomeUsuario.getText().toString().trim();
            String nomeAparelho = editNomeAparelho.getText().toString().trim();
            double potencia = Double.parseDouble(editPotencia.getText().toString());
            double horasDia = Double.parseDouble(editHorasDia.getText().toString());

            double consumoMensal = (potencia * horasDia * 30) / 1000.0;
            double custoMensal = consumoMensal * precoKwhSelecionado;

            exibirResultado(nomeUsuario, nomeAparelho, consumoMensal, custoMensal);

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Erro nos valores numéricos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        if (editNomeUsuario.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Digite o nome", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editNomeAparelho.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Digite o aparelho", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editPotencia.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Digite a potência", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editHorasDia.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Digite as horas", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (precoKwhSelecionado == 0) {
            Toast.makeText(requireContext(), "Selecione um estado", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void exibirResultado(String nomeUsuario, String nomeAparelho,
                                 double consumoMensal, double custoMensal) {
        String mensagem = "Usuário: " + nomeUsuario + "\n" +
                "Aparelho: " + nomeAparelho + "\n" +
                "Estado: " + estadoSelecionado + "\n" +
                "Consumo: " + String.format("%.2f", consumoMensal) + " kWh\n" +
                "Custo: R$ " + String.format("%.2f", custoMensal);

        new AlertDialog.Builder(requireContext())
                .setTitle("Resultado")
                .setMessage(mensagem)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (estadoDAO != null) {
            estadoDAO.fecharConexao();
        }
    }
}