package com.example.calculator.Model;

public class Measure {
    private String name;
    private String[] units;
    private double[] toMain; //value To Convert To Main Unit (Metre, Kilogram, etc.) //So first we convert to these main units then
    private double[] fromMain; //value To Convert From Main Unit                     // We convert them to required unit by multiplying with this value

    public Measure(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getUnits() {
        return units;
    }

    public void setUnits(String[] units) {
        this.units = units;
    }

    public double[] getToMain() {
        return toMain;
    }

    public void setToMain(double[] toMain) {
        this.toMain = toMain;
    }

    public double[] getFromMain() {
        return fromMain;
    }

    public void setFromMain(double[] fromMain) {
        this.fromMain = fromMain;
    }
}
