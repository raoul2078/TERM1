package controller;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import utils.AlertMaker;

import javax.annotation.PostConstruct;
import java.util.Objects;

@ViewController(value = "/view/AddDiet.fxml", title = "My App")
public class AddDietController {

    DatabaseHandler handler;

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    JFXTextField name;
    @FXML
    JFXTextArea description;

    @FXML
    JFXTextField caloriesMin;
    @FXML
    JFXTextField caloriesMax;
    @FXML
    JFXSlider proteinsMin;
    @FXML
    JFXSlider proteinsMax;
    @FXML
    JFXSlider carbsMin;
    @FXML
    JFXSlider carbsMax;
    @FXML
    JFXSlider lipidsMin;
    @FXML
    JFXSlider lipidsMax;

    @PostConstruct
    public void init() {
        handler = DatabaseHandler.getInstance();
        Objects.requireNonNull(context, "context");
    }

    public void addDiet() {
        String nameValue = name.getText();
        String descrValue = description.getText();
        if (nameValue.isEmpty() || descrValue.isEmpty() || caloriesMin.getText().isEmpty() || caloriesMax.getText().isEmpty()) {
            AlertMaker.showError("Champs vides", "Veuillez renseigner tous les champs");
            return;
        }
        int caloriesMinValue = Integer.parseInt(caloriesMin.getText());
        int proteinsMinValue = (int) proteinsMin.getValue();
        int carbsMinValue = (int) carbsMin.getValue();
        int lipidsMinValue = (int) lipidsMin.getValue();
        int caloriesMaxValue = Integer.parseInt(caloriesMax.getText());
        int proteinsMaxValue = (int) proteinsMax.getValue();
        int carbsMaxValue = (int) carbsMax.getValue();
        int lipidsMaxValue = (int) lipidsMax.getValue();


        String q = "INSERT INTO regime VALUES ("
                + "'" + nameValue + "',"
                + "'" + descrValue + "',"
                + caloriesMinValue + ","
                + caloriesMaxValue + ","
                + proteinsMinValue + ","
                + proteinsMaxValue + ","
                + carbsMinValue + ","
                + carbsMaxValue + ","
                + lipidsMinValue + ","
                + lipidsMaxValue + ""
                + ")";

        if (handler.execAction(q)) {
            AlertMaker.showInfo("Ajout régime", "Régime ajouté");
        } else {
            AlertMaker.showError("Ajout régime impossible", "Une erreur s'est produite");
        }
    }
}
