package controller;

import com.jfoenix.controls.JFXListView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;
import java.util.Objects;

@ViewController(value = "/view/SideMenu.fxml", title = "My App")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    @ActionTrigger("patients")
    private Label patients;

    @FXML
    @ActionTrigger("addPatient")
    private Label addPatient;

    @FXML
    @ActionTrigger("diets")
    private Label diets;

    @FXML
    @ActionTrigger("addDiet")
    private Label addDiet;

    @FXML
    private JFXListView<javafx.scene.control.Label> sideList;


    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        sideList.propagateMouseEventsToParent();
        sideList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            new Thread(() -> Platform.runLater(() -> {
                if (newVal != null) {
                    try {
                        contentFlowHandler.handle(newVal.getId());
                    } catch (VetoException | FlowException exc) {
                        exc.printStackTrace();
                    }
                }
            })).start();
        });
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        bindNodeToController(patients, PatientsController.class,contentFlow, contentFlowHandler);
        bindNodeToController(addPatient, AddPatientController.class,contentFlow, contentFlowHandler);
        bindNodeToController(diets, DietsController.class,contentFlow, contentFlowHandler);
        bindNodeToController(addDiet, AddDietController.class,contentFlow, contentFlowHandler);

    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }
}
