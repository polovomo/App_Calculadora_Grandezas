package br.ulbra.app_calculadora_eletrica.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.core.content.ContextCompat;
import br.ulbra.app_calculadora_eletrica.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private EditText etResistance, etCurrent, etVoltage, etPower;
    private TextView tvStatus;
    private Button btnRI, btnRV, btnRP, btnIV, btnIP, btnVP, btnCalculate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_lei_ohm, container, false);

        initializeViews(root);
        setupObservers();
        setupButtonListeners();
        setupTextWatchers();
        setDefaultSelection();

        return root;
    }

    private void initializeViews(View root) {
        etResistance = root.findViewById(R.id.et_resistance);
        etCurrent = root.findViewById(R.id.et_current);
        etVoltage = root.findViewById(R.id.et_voltage);
        etPower = root.findViewById(R.id.et_power);
        tvStatus = root.findViewById(R.id.tv_status);

        btnRI = root.findViewById(R.id.btn_ri);
        btnRV = root.findViewById(R.id.btn_rv);
        btnRP = root.findViewById(R.id.btn_rp);
        btnIV = root.findViewById(R.id.btn_iv);
        btnIP = root.findViewById(R.id.btn_ip);
        btnVP = root.findViewById(R.id.btn_vp);
        btnCalculate = root.findViewById(R.id.btn_calculate);
    }

    private void setupObservers() {
        // Observar mudanças na Resistência
        homeViewModel.getResistance().observe(getViewLifecycleOwner(), value -> {
            if (value != null && !isEditing(etResistance)) {
                etResistance.setText(String.format("%.2f", value));
            }
        });

        // Observar mudanças na Corrente
        homeViewModel.getCurrent().observe(getViewLifecycleOwner(), value -> {
            if (value != null && !isEditing(etCurrent)) {
                etCurrent.setText(String.format("%.2f", value));
            }
        });

        // Observar mudanças na Tensão
        homeViewModel.getVoltage().observe(getViewLifecycleOwner(), value -> {
            if (value != null && !isEditing(etVoltage)) {
                etVoltage.setText(String.format("%.2f", value));
            }
        });

        // Observar mudanças na Potência
        homeViewModel.getPower().observe(getViewLifecycleOwner(), value -> {
            if (value != null && !isEditing(etPower)) {
                etPower.setText(String.format("%.2f", value));
            }
        });

        // Observar mudanças no Status
        homeViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                tvStatus.setText(status);
            }
        });

        // Observar mudanças no Modo Selecionado
        homeViewModel.getSelectedMode().observe(getViewLifecycleOwner(), mode -> {
            if (mode != null) {
                updateButtonColors(mode);
            }
        });
    }

    private void setupButtonListeners() {
        btnRI.setOnClickListener(v -> homeViewModel.setSelectedMode("RI"));
        btnRV.setOnClickListener(v -> homeViewModel.setSelectedMode("RV"));
        btnRP.setOnClickListener(v -> homeViewModel.setSelectedMode("RP"));
        btnIV.setOnClickListener(v -> homeViewModel.setSelectedMode("IV"));
        btnIP.setOnClickListener(v -> homeViewModel.setSelectedMode("IP"));
        btnVP.setOnClickListener(v -> homeViewModel.setSelectedMode("VP"));

        btnCalculate.setOnClickListener(v -> {
            homeViewModel.calculateOhmsLaw();
            showToast("Calculado!");
        });
    }

    private void setupTextWatchers() {
        setupTextWatcher(etResistance, value -> homeViewModel.setResistance(value));
        setupTextWatcher(etCurrent, value -> homeViewModel.setCurrent(value));
        setupTextWatcher(etVoltage, value -> homeViewModel.setVoltage(value));
        setupTextWatcher(etPower, value -> homeViewModel.setPower(value));
    }

    private void setupTextWatcher(EditText editText, ValueSetter setter) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing(editText)) {
                    try {
                        double value = s.toString().isEmpty() ? 0 : Double.parseDouble(s.toString());
                        setter.setValue(value);
                    } catch (NumberFormatException e) {
                        // Ignorar entrada inválida
                    }
                }
            }
        });
    }

    private void setDefaultSelection() {
        homeViewModel.setSelectedMode("IV");
    }

    private void updateButtonColors(String selectedMode) {
        // Reset all buttons
        Button[] buttons = {btnRI, btnRV, btnRP, btnIV, btnIP, btnVP};
        int defaultColor = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light);
        int defaultTextColor = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark);

        for (Button btn : buttons) {
            btn.setBackgroundColor(defaultColor);
            btn.setTextColor(defaultTextColor);
        }

        // Highlight selected button
        int selectedColor = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark);
        int selectedTextColor = ContextCompat.getColor(requireContext(), android.R.color.white);

        switch (selectedMode) {
            case "RI":
                btnRI.setBackgroundColor(selectedColor);
                btnRI.setTextColor(selectedTextColor);
                break;
            case "RV":
                btnRV.setBackgroundColor(selectedColor);
                btnRV.setTextColor(selectedTextColor);
                break;
            case "RP":
                btnRP.setBackgroundColor(selectedColor);
                btnRP.setTextColor(selectedTextColor);
                break;
            case "IV":
                btnIV.setBackgroundColor(selectedColor);
                btnIV.setTextColor(selectedTextColor);
                break;
            case "IP":
                btnIP.setBackgroundColor(selectedColor);
                btnIP.setTextColor(selectedTextColor);
                break;
            case "VP":
                btnVP.setBackgroundColor(selectedColor);
                btnVP.setTextColor(selectedTextColor);
                break;
        }
    }

    private boolean isEditing(EditText editText) {
        return editText.hasFocus();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Interface para setar valores
    private interface ValueSetter {
        void setValue(double value);
    }
}