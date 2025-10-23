package br.ulbra.app_calculadora_eletrica.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import br.ulbra.app_calculadora_eletrica.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private EditText etResistance, etCurrent, etVoltage, etPower;
    private TextView tvStatus;
    private Button btnRI, btnRV, btnRP, btnIV, btnIP, btnVP, btnCalculate;

    // Novas variáveis para divisor de tensão
    private EditText etResistor1, etResistor2, etVoltageIn;
    private TextView tvVoltageOut;
    private Button btnCalculateDivider;
    private LinearLayout dividerResultLayout;

    // Nova paleta de cores suave
    private final int COLOR_BUTTON_DEFAULT = 0xFFE3F2FD;    // Azul muito claro
    private final int COLOR_BUTTON_SELECTED = 0xFF1976D2;   // Azul principal
    private final int COLOR_TEXT_DEFAULT = 0xFF1976D2;      // Azul
    private final int COLOR_TEXT_SELECTED = 0xFFFFFFFF;     // Branco

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_lei_ohm, container, false);

        initializeViews(root);
        setupObservers();
        setupButtonListeners();
        setupTextWatchers();
        setupDividerTextWatchers();
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

        // Novas views para divisor de tensão
        etResistor1 = root.findViewById(R.id.et_resistor1);
        etResistor2 = root.findViewById(R.id.et_resistor2);
        etVoltageIn = root.findViewById(R.id.et_voltage_in);
        tvVoltageOut = root.findViewById(R.id.tv_voltage_out);
        btnCalculateDivider = root.findViewById(R.id.btn_calculate_divider);
        dividerResultLayout = root.findViewById(R.id.divider_result_layout);
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

        // Novos observadores para divisor de tensão
        homeViewModel.getResistor1().observe(getViewLifecycleOwner(), value -> {
            if (value != null && !isEditing(etResistor1)) {
                etResistor1.setText(String.format("%.0f", value));
            }
        });

        homeViewModel.getResistor2().observe(getViewLifecycleOwner(), value -> {
            if (value != null && !isEditing(etResistor2)) {
                etResistor2.setText(String.format("%.0f", value));
            }
        });

        homeViewModel.getVoltageIn().observe(getViewLifecycleOwner(), value -> {
            if (value != null && !isEditing(etVoltageIn)) {
                etVoltageIn.setText(String.format("%.1f", value));
            }
        });

        homeViewModel.getVoltageOut().observe(getViewLifecycleOwner(), value -> {
            if (value != null) {
                tvVoltageOut.setText(String.format("%.2f V", value));
            }
        });

        homeViewModel.getDividerResultVisible().observe(getViewLifecycleOwner(), visible -> {
            if (visible != null) {
                dividerResultLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
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

        // Listener para o botão do divisor de tensão
        btnCalculateDivider.setOnClickListener(v -> {
            homeViewModel.calculateVoltageDivider();
            showToast("Divisor de tensão calculado!");
        });
    }

    private void setupTextWatchers() {
        setupTextWatcher(etResistance, value -> homeViewModel.setResistance(value));
        setupTextWatcher(etCurrent, value -> homeViewModel.setCurrent(value));
        setupTextWatcher(etVoltage, value -> homeViewModel.setVoltage(value));
        setupTextWatcher(etPower, value -> homeViewModel.setPower(value));
    }

    private void setupDividerTextWatchers() {
        setupTextWatcher(etResistor1, value -> homeViewModel.setResistor1(value));
        setupTextWatcher(etResistor2, value -> homeViewModel.setResistor2(value));
        setupTextWatcher(etVoltageIn, value -> homeViewModel.setVoltageIn(value));
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
        // Reset all buttons to default colors
        Button[] buttons = {btnRI, btnRV, btnRP, btnIV, btnIP, btnVP};

        for (Button btn : buttons) {
            btn.setBackgroundColor(COLOR_BUTTON_DEFAULT);
            btn.setTextColor(COLOR_TEXT_DEFAULT);
        }

        // Highlight selected button
        Button selectedButton = null;

        switch (selectedMode) {
            case "RI":
                selectedButton = btnRI;
                break;
            case "RV":
                selectedButton = btnRV;
                break;
            case "RP":
                selectedButton = btnRP;
                break;
            case "IV":
                selectedButton = btnIV;
                break;
            case "IP":
                selectedButton = btnIP;
                break;
            case "VP":
                selectedButton = btnVP;
                break;
        }

        if (selectedButton != null) {
            selectedButton.setBackgroundColor(COLOR_BUTTON_SELECTED);
            selectedButton.setTextColor(COLOR_TEXT_SELECTED);
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