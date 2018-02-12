package controller;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableRow;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Patient;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.function.Function;


@ViewController(value = "/view/Patients.fxml", title = "My App")
public class PatientsController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private JFXTreeTableView<Patient> treeTableView;
    @FXML
    private JFXTreeTableColumn<Patient, String> firstNameColumn;
    @FXML
    private JFXTreeTableColumn<Patient, String> lastNameColumn;
    @FXML
    private JFXTreeTableColumn<Patient, String> heightColumn;
    @FXML
    private JFXTreeTableColumn<Patient, String> weightColumn;
    @FXML
    private JFXTreeTableColumn<Patient, String> dietColumn;
    @FXML
    private JFXTextField searchField;

    ObservableList<Patient> list = FXCollections.observableArrayList();


    @ActionHandler
    protected FlowActionHandler actionHandler;

    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        setupTableView();
        loadData();
       contentFlow.withLink(PatientsController.class, "showPatient", PatientDetailsController.class);

    }

    private void setupTableView() {
        firstNameColumn.setCellValueFactory(param -> param.getValue().getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(param -> param.getValue().getValue().lastNameProperty());
        dietColumn.setCellValueFactory(param -> param.getValue().getValue().dietProperty());
        heightColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf((param.getValue().getValue().getHeight()))));
        weightColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf((param.getValue().getValue().getWeight()))));

        searchField.textProperty().addListener(setupSearchField(treeTableView));

        treeTableView.setRowFactory(param -> {
            TreeTableRow<Patient> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                Patient patient = row.getItem();
                System.out.println(patient);
                try {
                    FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
                    contentFlowHandler.handle("showPatient");
                } catch (VetoException | FlowException e) {
                    e.printStackTrace();
                }
            });
            return row;
        });
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


    private void loadData() {
        ObservableList<Patient> data = generateDummyData(30);
        treeTableView.setRoot(new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren));
        treeTableView.setShowRoot(false);
    }

    private ObservableList<Patient> generateDummyData(int numberOfEntries) {
        final ObservableList<Patient> dummyData = FXCollections.observableArrayList();
        for (int i = 1; i <= numberOfEntries; i++) {
            dummyData.add(createNewRandomPatient(i));
        }
        return dummyData;
    }

    private Patient createNewRandomPatient(int i) {
        return new Patient("John " + i, "Doe " + i, "jdoe" + i + "@gmail.com", "000000000", 180, 65, "Hypoglycemique");
    }

}
