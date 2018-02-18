package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseHandler;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Diet;
import model.Patient;
import utils.AlertMaker;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;

@ViewController(value = "/view/AddPatient.fxml", title = "My App")
public class AddPatientController {

    DatabaseHandler handler;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField phone;

    @FXML
    private JFXTextField height;

    @FXML
    private JFXTextField weight;

    @FXML
    private JFXButton submitButton;

    @FXML
    JFXComboBox<Label> diets = new JFXComboBox<>();


    @PostConstruct
    public void init() throws SQLException {
        handler = DatabaseHandler.getInstance();

        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}?")) {
                phone.setText(oldValue);
            }
        });

        height.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,3}?")) {
                height.setText(oldValue);
            }
        });

        weight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,3}([.|,]\\d{0,2})?")) {
                weight.setText(oldValue);
            }
        });

        String q = "SELECT nom FROM regime";
        ResultSet resultSet = handler.execQuery(q);
        if (resultSet != null) {
            while (resultSet.next()) {
                diets.getItems().add(new Label(resultSet.getString("nom")));
            }
        }
    }

    public void add() {
        String lastname = this.lastName.getText();
        String firstname = this.firstName.getText();
        String email = this.email.getText();
        String phone = this.phone.getText();
        int height = Integer.parseInt(this.height.getText());
        double weight = Double.parseDouble(this.weight.getText().replace(",", "."));
        String diet = this.diets.getSelectionModel().getSelectedItem().getText();
        String doctor = (String) context.getRegisteredObject("login");

        String q = "INSERT INTO patient VALUES ("
                + "null,"
                + "'" + lastname + "',"
                + "'" + firstname + "',"
                + height + ","
                + weight + ","
                + "'" + email + "',"
                + "'" + phone + "',"
                + "'" + doctor + "',"
                + "'" + diet + "'"
                + ")";

        if (handler.execAction(q)) {
            AlertMaker.showInfo("Ajout patient", "Patient ajouté");
        } else {
            AlertMaker.showError("Ajout patient impossible", "Une erreur s'est produite");
        }
    }
}
