package controller;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.DatabaseHandler;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableRow;
import model.Patient;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


@ViewController(value = "/view/Patients.fxml", title = "My App")
public class PatientsController {

    DatabaseHandler handler;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private JFXTreeTableView<Patient> treeTableView;
    @FXML
    private JFXTreeTableColumn<Patient, String> firstNameColumn;
    @FXML
    private JFXTreeTableColumn<Patient, String> lastNameColumn;
    @FXML
    private JFXTreeTableColumn<Patient, Integer> heightColumn;
    @FXML
    private JFXTreeTableColumn<Patient, Double> weightColumn;
    @FXML
    private JFXTreeTableColumn<Patient, String> dietColumn;
    @FXML
    private JFXTextField searchField;

    @ActionHandler
    protected FlowActionHandler actionHandler;

    @PostConstruct
    public void init() {
        handler = DatabaseHandler.getInstance();
        Objects.requireNonNull(context, "context");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        setupTreeTableView();
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contentFlow.withLink(PatientsController.class, "showPatient", PatientDetailsController.class);
    }

    private void setupTreeTableView() {
        firstNameColumn.setCellValueFactory(param -> param.getValue().getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(param -> param.getValue().getValue().lastNameProperty());
        dietColumn.setCellValueFactory(param -> param.getValue().getValue().dietProperty());
        heightColumn.setCellValueFactory(param -> param.getValue().getValue().heightProperty().asObject());
        weightColumn.setCellValueFactory(param -> param.getValue().getValue().weightProperty().asObject());

        searchField.textProperty().addListener(setupSearchField(treeTableView));

        treeTableView.setRowFactory(param -> {
            TreeTableRow<Patient> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                Patient patient = row.getItem();
                if (patient != null) {
                    try {
                        navigateToPatientDetails(patient);
                    } catch (VetoException | FlowException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    private void navigateToPatientDetails(Patient patient) throws VetoException, FlowException {
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        context.register("Patient", patient);
        contentFlowHandler.handle("showPatient");

    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Patient> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(patientProp -> {
                    final Patient patient = patientProp.getValue();
                    return patient.getFirstName().toLowerCase().contains(newVal.toLowerCase())
                            || patient.getLastName().toLowerCase().contains(newVal.toLowerCase())
                            || patient.getDiet().toLowerCase().contains(newVal.toLowerCase())
                            || Double.toString(patient.getHeight()).contains(newVal)
                            || Double.toString(patient.getWeight()).contains(newVal);
                });
    }


    private void loadData() throws SQLException {
        String doctor = (String)context.getRegisteredObject("login");
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        ResultSet resultSet = handler.getPatients(doctor);
        if (resultSet != null) {
            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getInt("id"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("email"),
                        resultSet.getString("tel"),
                        resultSet.getInt("taille"),
                        resultSet.getDouble("poids"),
                        resultSet.getString("regime")
                );
                patients.add(patient);
            }
        }
        treeTableView.setRoot(new RecursiveTreeItem<>(patients, RecursiveTreeObject::getChildren));
        treeTableView.setShowRoot(false);
    }
}
