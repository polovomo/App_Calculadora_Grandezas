package br.ulbra.app_calculadora_eletrica;

import java.io.Serializable;

public class Estado implements Serializable {
    private Integer id;
    private String sigla;
    private Double kwh;

    public Estado() {
    }

    public Estado(String sigla, Double kwh) {
        this.sigla = sigla;
        this.kwh = kwh;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Double getKwh() {
        return kwh;
    }

    public void setKwh(Double kwh) {
        this.kwh = kwh;
    }
}
