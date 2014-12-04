package com.mobico.rcart;

import android.app.Application;

/**
 * Created by justinkhoo on 12/3/14.
 */
public class globalvariable extends Application {

    private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }
    private Double globallati;
    private Double globallongi;

    public Double getGloballati(){
        return globallati;
    }
    public void setGloballati(Double globallati){
        this.globallati = globallati;
    }
    public Double getGloballongi(){
        return globallongi;
    }
    public void setGloballongi(Double globallongi){
        this.globallongi = globallongi;
    }
}