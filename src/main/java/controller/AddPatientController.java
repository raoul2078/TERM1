package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Patient;

import javax.annotation.PostConstruct;

@ViewController(value = "/view/AddPatient.fxml", title = "My App")
public class AddPatientController {

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
    public void init() {
        diets.getItems().add(new Label("Hypoglycemique"));

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
    }

    public void add() {
        String lastname = this.lastName.getText();
        String firstname = this.firstName.getText();
        String email = this.email.getText();
        String phone = this.phone.getText();
        double height = Double.parseDouble(this.height.getText());
        double weight = Double.parseDouble(this.weight.getText().replace(",", "."));
        String diet = this.diets.getSelectionModel().getSelectedItem().toString();

        Patient patient = new Patient(firstname, lastname, email, phone, height, weight, diet);
        System.out.println(patient);
    }
}
