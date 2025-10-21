package br.ulbra.app_calculadora_eletrica.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Double> resistance;
    private final MutableLiveData<Double> current;
    private final MutableLiveData<Double> voltage;
    private final MutableLiveData<Double> power;
    private final MutableLiveData<String> selectedMode;
    private final MutableLiveData<String> status;

    public HomeViewModel() {
        resistance = new MutableLiveData<>();
        resistance.setValue(5.0);

        current = new MutableLiveData<>();
        current.setValue(2.0);

        voltage = new MutableLiveData<>();
        voltage.setValue(10.0);

        power = new MutableLiveData<>();
        power.setValue(20.0);

        selectedMode = new MutableLiveData<>();
        selectedMode.setValue("IV");

        status = new MutableLiveData<>();
        status.setValue("Agora I e V são os valores de entrada. R e P estão sendo calculados.");
    }

    public LiveData<Double> getResistance() {
        return resistance;
    }

    public LiveData<Double> getCurrent() {
        return current;
    }

    public LiveData<Double> getVoltage() {
        return voltage;
    }

    public LiveData<Double> getPower() {
        return power;
    }

    public LiveData<String> getSelectedMode() {
        return selectedMode;
    }

    public LiveData<String> getStatus() {
        return status;
    }

    public void setResistance(double value) {
        resistance.setValue(value);
    }

    public void setCurrent(double value) {
        current.setValue(value);
    }

    public void setVoltage(double value) {
        voltage.setValue(value);
    }

    public void setPower(double value) {
        power.setValue(value);
    }

    public void setSelectedMode(String mode) {
        selectedMode.setValue(mode);
        updateStatus(mode);
    }

    private void updateStatus(String mode) {
        switch (mode) {
            case "RI":
                status.setValue("R e I são entradas. Calculando V e P.");
                break;
            case "RV":
                status.setValue("R e V são entradas. Calculando I e P.");
                break;
            case "RP":
                status.setValue("R e P são entradas. Calculando I e V.");
                break;
            case "IV":
                status.setValue("I e V são entradas. Calculando R e P.");
                break;
            case "IP":
                status.setValue("I e P são entradas. Calculando R e V.");
                break;
            case "VP":
                status.setValue("V e P são entradas. Calculando R e I.");
                break;
        }
    }

    public void calculateOhmsLaw() {
        Double r = resistance.getValue();
        Double i = current.getValue();
        Double v = voltage.getValue();
        Double p = power.getValue();
        String mode = selectedMode.getValue();

        if (r == null || i == null || v == null || p == null || mode == null) return;

        try {
            switch (mode) {
                case "RI": // R and I are inputs
                    if (r <= 0 || i <= 0) return;
                    v = r * i;
                    p = v * i;
                    break;
                case "RV": // R and V are inputs
                    if (r <= 0 || v <= 0) return;
                    i = v / r;
                    p = v * i;
                    break;
                case "RP": // R and P are inputs
                    if (r <= 0 || p <= 0) return;
                    i = Math.sqrt(p / r);
                    v = i * r;
                    break;
                case "IV": // I and V are inputs
                    if (i <= 0 || v <= 0) return;
                    r = v / i;
                    p = v * i;
                    break;
                case "IP": // I and P are inputs
                    if (i <= 0 || p <= 0) return;
                    v = p / i;
                    r = v / i;
                    break;
                case "VP": // V and P are inputs
                    if (v <= 0 || p <= 0) return;
                    i = p / v;
                    r = v / i;
                    break;
            }

            // Update values
            resistance.setValue(r);
            current.setValue(i);
            voltage.setValue(v);
            power.setValue(p);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}