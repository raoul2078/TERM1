package controller;

import com.jfoenix.controls.JFXListView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;

@ViewController(value = "/view/PatientDetails.fxml", title = "My App")
public class PatientDetailsController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private Label name;
    @FXML
    private Label heightAndWeight;
    @FXML
    private Label email;
    @FXML
    private Label phone;
    @FXML
    private JFXListView<Label> list;

    @PostConstruct
    public void init() {

    }
}
