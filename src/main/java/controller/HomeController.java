package controller;

import com.jfoenix.controls.*;
import database.DatabaseHandler;
import datafx.ExtendedAnimatedFlowContainer;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import org.apache.commons.codec.digest.DigestUtils;
import utils.AlertMaker;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_LEFT;

@ViewController(value = "/view/Home.fxml", title = "My App")
public class HomeController {

    DatabaseHandler handler;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    JFXTextField loginField;
    @FXML
    JFXPasswordField passwordField;
    @FXML
    JFXTextField loginFieldR;
    @FXML
    JFXPasswordField passwordFieldR;
    @FXML
    JFXTextField lastNameField;
    @FXML
    JFXTextField firstNameField;
    @FXML
    JFXTextField emailField;
    @FXML
    JFXTextField phoneField;
    @FXML
    JFXButton loginButton;
    @FXML
    JFXButton registerButton;

    @PostConstruct
    public void init() {
        handler = DatabaseHandler.getInstance();
        Objects.requireNonNull(context, "context");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlow.withLink(HomeController.class, "showPatients", PatientsController.class);
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}?")) {
                phoneField.setText(oldValue);
            }
        });
    }

    private void initSideMenu() throws FlowException {
        JFXDrawer drawer = (JFXDrawer)context.getRegisteredObject("drawer");
        // side controller will add links to the content flow
        final Duration containerAnimationDuration = Duration.millis(320);
        Flow sideMenuFlow = new Flow(SideMenuController.class);
        final FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        drawer.setSidePane(sideMenuFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration,
                SWIPE_LEFT)));
    }

    public void login() throws SQLException, VetoException, FlowException {
        String login = loginField.getText();
        String pwd = DigestUtils.shaHex(passwordField.getText());
        if (login.isEmpty() || pwd.isEmpty()) {
            AlertMaker.showError("Champs vides", "Veuillez renseigner tous les champs");
        }
        String query = "SELECT login, mdp FROM docteur WHERE login = " + "'" + login + "'" + "AND mdp = " + "'" + pwd +"'";
        ResultSet resultSet = handler.execQuery(query);
        if (resultSet != null && !resultSet.isBeforeFirst()) {
            AlertMaker.showError("Erreur connexion", "Identifiants incorrects");
        } else {
            FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
            context.register("login", login);
            initSideMenu();
            contentFlowHandler.handle("showPatients");
        }

    }

    public void register() throws SQLException {
        String login = loginFieldR.getText();
        String pwd = DigestUtils.shaHex(passwordFieldR.getText());
        String lastname = lastNameField.getText();
        String firstname = firstNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (login.isEmpty() || pwd.isEmpty() || lastname.isEmpty() || firstname.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            AlertMaker.showError("Champs vides", "Veuillez renseigner tous les champs");
        }

        String q = "SELECT login, mdp FROM docteur WHERE login = " + "'" + login + "'";
        ResultSet resultSet = handler.execQuery(q);
        if (resultSet != null && resultSet.isBeforeFirst()) {
            AlertMaker.showError("Erreur connexion", "Ce pseudo est deja utilisé!");
            return;
        }

        String query = "INSERT INTO docteur VALUES ("
                + "'" + login + "',"
                + "'" + pwd + "',"
                + "'" + lastname + "',"
                + "'" + firstname + "',"
                + "'" + email + "',"
                + "'" + phone + "'"
                + ")";

        if (handler.execAction(query)) {
            AlertMaker.showInfo("Compte créé", "Vous pouvez vous connecter");
        } else{
            AlertMaker.showError("Création du compte impossible", "Une erreur s'est produite");
        }

    }
}
