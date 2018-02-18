package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Diet extends RecursiveTreeObject<Diet> {
    private StringProperty name;
    private StringProperty description;
    private SimpleIntegerProperty caloriesMin;
    private SimpleIntegerProperty caloriesMax;
    private SimpleIntegerProperty proteinsMin;
    private SimpleIntegerProperty proteinsMax;
    private SimpleIntegerProperty carbsMin;
    private SimpleIntegerProperty carbsMax;
    private SimpleIntegerProperty lipidsMin;
    private SimpleIntegerProperty lipidsMax;

    public Diet(String name,
                String description,
                int caloriesMin,
                int caloriesMax,
                int proteinsMin,
                int proteinsMax,
                int carbsMin,
                int carbsMax,
                int lipidsMin,
                int lipidsMax) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.caloriesMin = new SimpleIntegerProperty(caloriesMin);
        this.caloriesMax = new SimpleIntegerProperty(caloriesMax);
        this.proteinsMin = new SimpleIntegerProperty(proteinsMin);
        this.proteinsMax = new SimpleIntegerProperty(proteinsMax);
        this.carbsMin = new SimpleIntegerProperty(carbsMin);
        this.carbsMax = new SimpleIntegerProperty(carbsMax);
        this.lipidsMin = new SimpleIntegerProperty(lipidsMin);
        this.lipidsMax = new SimpleIntegerProperty(lipidsMax);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public int getCaloriesMin() {
        return caloriesMin.get();
    }

    public SimpleIntegerProperty caloriesMinProperty() {
        return caloriesMin;
    }

    public int getCaloriesMax() {
        return caloriesMax.get();
    }

    public SimpleIntegerProperty caloriesMaxProperty() {
        return caloriesMax;
    }

    public int getProteinsMin() {
        return proteinsMin.get();
    }

    public SimpleIntegerProperty proteinsMinProperty() {
        return proteinsMin;
    }

    public int getProteinsMax() {
        return proteinsMax.get();
    }

    public SimpleIntegerProperty proteinsMaxProperty() {
        return proteinsMax;
    }

    public int getCarbsMin() {
        return carbsMin.get();
    }

    public SimpleIntegerProperty carbsMinProperty() {
        return carbsMin;
    }

    public int getCarbsMax() {
        return carbsMax.get();
    }

    public SimpleIntegerProperty carbsMaxProperty() {
        return carbsMax;
    }

    public int getLipidsMin() {
        return lipidsMin.get();
    }

    public SimpleIntegerProperty lipidsMinProperty() {
        return lipidsMin;
    }

    public int getLipidsMax() {
        return lipidsMax.get();
    }

    public SimpleIntegerProperty lipidsMaxProperty() {
        return lipidsMax;
    }

    @Override
    public String toString() {
        return "Diet{" +
                "name=" + name.get() +
                ", description=" + description.get() +
                ", caloriesMin=" + caloriesMin.get() +
                ", caloriesMax=" + caloriesMax.get() +
                ", proteinsMin=" + proteinsMin.get() +
                ", proteinsMax=" + proteinsMax.get() +
                ", carbsMin=" + carbsMin.get() +
                ", carbsMax=" + carbsMax.get() +
                ", lipidsMin=" + lipidsMin.get() +
                ", lipidsMax=" + lipidsMax.get() +
                '}';
    }
}

