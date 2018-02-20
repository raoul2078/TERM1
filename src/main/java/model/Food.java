package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Food extends RecursiveTreeObject<Food> {

    private SimpleIntegerProperty alim_grp_code;
    private StringProperty name;
    private StringProperty alim_grp_nom_fr;
    private StringProperty alim_ssgrp_nom_fr;
    private StringProperty alim_ssssgrp_nom_fr;
    private SimpleIntegerProperty alim_code;
    private SimpleIntegerProperty fractionnable;
    private SimpleIntegerProperty unity;
    private SimpleIntegerProperty portion_min;
    private SimpleIntegerProperty portion_max;
    private SimpleIntegerProperty portion_moy;
    private SimpleIntegerProperty calories;
    private SimpleIntegerProperty proteins;
    private SimpleIntegerProperty carbs;
    private SimpleIntegerProperty lipids;

    public Food(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public Food(String name, int alim_code) {
        this.name = new SimpleStringProperty(name);
        this.alim_code = new SimpleIntegerProperty(alim_code);
    }

    public Food(int alim_grp_code, String alim_grp_nom_fr, String alim_ssgrp_nom_fr, String alim_ssssgrp_nom_fr, int alim_code, String name, int fractionnable, int unity, int portion_min, int portion_max, int portion_moy, int calories, int proteins, int carbs, int lipids) {
        this.alim_grp_code = new SimpleIntegerProperty(alim_grp_code);
        this.name = new SimpleStringProperty(name);
        this.alim_grp_nom_fr = new SimpleStringProperty(alim_grp_nom_fr);
        this.alim_ssgrp_nom_fr = new SimpleStringProperty(alim_ssgrp_nom_fr);
        this.alim_ssssgrp_nom_fr = new SimpleStringProperty(alim_ssssgrp_nom_fr);
        this.alim_code = new SimpleIntegerProperty(alim_code);
        this.fractionnable = new SimpleIntegerProperty(fractionnable);
        this.unity = new SimpleIntegerProperty(unity);
        this.portion_min = new SimpleIntegerProperty(portion_min);
        this.portion_max = new SimpleIntegerProperty(portion_max);
        this.portion_moy = new SimpleIntegerProperty(portion_moy);
        this.calories = new SimpleIntegerProperty(calories);
        this.proteins = new SimpleIntegerProperty(proteins);
        this.carbs = new SimpleIntegerProperty(carbs);
        this.lipids = new SimpleIntegerProperty(lipids);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getAlim_grp_code() {
        return alim_grp_code.get();
    }

    public SimpleIntegerProperty alim_grp_codeProperty() {
        return alim_grp_code;
    }

    public String getAlim_ssgrp_nom_fr() {
        return alim_ssgrp_nom_fr.get();
    }

    public StringProperty alim_ssgrp_nom_frProperty() {
        return alim_ssgrp_nom_fr;
    }

    public String getAlim_ssssgrp_nom_fr() {
        return alim_ssssgrp_nom_fr.get();
    }

    public StringProperty alim_ssssgrp_nom_frProperty() {
        return alim_ssssgrp_nom_fr;
    }

    public int getAlim_code() {
        return alim_code.get();
    }

    public SimpleIntegerProperty alim_codeProperty() {
        return alim_code;
    }

    public int getFractionnable() {
        return fractionnable.get();
    }

    public SimpleIntegerProperty fractionnableProperty() {
        return fractionnable;
    }

    public int getUnity() {
        return unity.get();
    }

    public SimpleIntegerProperty unityProperty() {
        return unity;
    }

    public int getPortion_min() {
        return portion_min.get();
    }

    public SimpleIntegerProperty portion_minProperty() {
        return portion_min;
    }

    public int getPortion_max() {
        return portion_max.get();
    }

    public SimpleIntegerProperty portion_maxProperty() {
        return portion_max;
    }

    public int getPortion_moy() {
        return portion_moy.get();
    }

    public SimpleIntegerProperty portion_moyProperty() {
        return portion_moy;
    }

    public int getCalories() {
        return calories.get();
    }

    public SimpleIntegerProperty caloriesProperty() {
        return calories;
    }

    public int getProteins() {
        return proteins.get();
    }

    public SimpleIntegerProperty proteinsProperty() {
        return proteins;
    }

    public int getCarbs() {
        return carbs.get();
    }

    public SimpleIntegerProperty carbsProperty() {
        return carbs;
    }

    public int getLipids() {
        return lipids.get();
    }

    public SimpleIntegerProperty lipidsProperty() {
        return lipids;
    }

    @Override
    public String toString() {
        return name.get();
    }
}
