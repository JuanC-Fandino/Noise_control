package com.example.juank.noise_control;

/**
 * Created by Juank on 20/08/17.
 */

public class MaterialesCaracteristicas {

    private int materialID;
    private String materialName;
    private float DENSIDAD;
    private float COEFICIENTE;
    private float YOUNG;
    private float POISSON;

    public int getMaterialId() {
        return materialID;
    }

    public void setMaterialId(int materialID) {
        this.materialID = materialID;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Float getDENSIDAD() {
        return DENSIDAD;
    }

    public void setDENSIDAD(Float Alfa125) {
        this.DENSIDAD = Alfa125;
    }

    public Float getCOEFICIENTE() {
        return COEFICIENTE;
    }

    public void setCOEFICIENTE(Float Alfa250) {
        this.COEFICIENTE = Alfa250;
    }

    public Float getYOUNG() {
        return YOUNG;
    }

    public void setYOUNG(Float Alfa500) {
        this.YOUNG = Alfa500;
    }

    public Float getPOISSON() {
        return POISSON;
    }

    public void setPOISSON(Float Alfa1000) {
        this.POISSON = Alfa1000;
    }



}
