package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.DatabaseHandler;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import model.Diet;
import utils.AlertMaker;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;

@ViewController(value = "/view/Diets.fxml", title = "My App")
public class DietsController {

    DatabaseHandler handler;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    StackPane root;
    @FXML
    private JFXTreeTableView<Diet> treeTableView;
    @FXML
    private JFXTreeTableColumn<Diet, String> nameColumn;
    @FXML
    private JFXTreeTableColumn<Diet, Integer> caloriesMinColumn;
    @FXML
    private JFXTreeTableColumn<Diet, Integer> caloriesMaxColumn;
    @FXML
    private JFXTreeTableColumn<Diet, Integer> proteinsMinColumn;
    @FXML
    private JFXTreeTableColumn<Diet, Integer> proteinsMaxColumn;
    @FXML
    private JFXTreeTableColumn<Diet, Integer> carbsMinColumn;
    @FXML
    private JFXTreeTableColumn<Diet, Integer> carbsMaxColumn;
    @FXML
    private JFXTreeTableColumn<Diet, Integer> lipidsMinColumn;
    @FXML
    private JFXTreeTableColumn<Diet, Integer> lipidsMaxColumn;
    @FXML
    private JFXTextField searchField;

    ObservableList<Diet> list = FXCollections.observableArrayList();


    /*Diet edit fields*/
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
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXDialog dietEditDialog;

    @PostConstruct
    public void init() {
        root.getChildren().remove(dietEditDialog);
        handler = DatabaseHandler.getInstance();
        setupTreeTableView();
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateButton.setOnMouseClicked(event -> {
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

            Diet diet = treeTableView.getSelectionModel().getSelectedItem().getValue();
            if (handler.updateDiet(new Diet(
                    nameValue,
                    descrValue,
                    caloriesMinValue,
                    caloriesMaxValue,
                    proteinsMinValue,
                    proteinsMaxValue,
                    carbsMinValue,
                    carbsMaxValue,
                    lipidsMinValue,
                    lipidsMaxValue
            ), diet.getName())) {
                AlertMaker.showInfo("Régime modifié", "Modifications sauvegardées");
            } else {
                AlertMaker.showError("Impossible de modifier le régime", "Une erreur s'est produite");
            }
            try {
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dietEditDialog.close();
        });
    }

    private void setupTreeTableView() {
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        caloriesMinColumn.setCellValueFactory(param -> param.getValue().getValue().caloriesMinProperty().asObject());
        caloriesMaxColumn.setCellValueFactory(param -> param.getValue().getValue().caloriesMaxProperty().asObject());
        proteinsMinColumn.setCellValueFactory(param -> param.getValue().getValue().proteinsMinProperty().asObject());
        proteinsMaxColumn.setCellValueFactory(param -> param.getValue().getValue().proteinsMaxProperty().asObject());
        carbsMinColumn.setCellValueFactory(param -> param.getValue().getValue().carbsMinProperty().asObject());
        carbsMaxColumn.setCellValueFactory(param -> param.getValue().getValue().carbsMaxProperty().asObject());
        lipidsMinColumn.setCellValueFactory(param -> param.getValue().getValue().lipidsMinProperty().asObject());
        lipidsMaxColumn.setCellValueFactory(param -> param.getValue().getValue().lipidsMaxProperty().asObject());

        searchField.textProperty().addListener(setupSearchField(treeTableView));
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Diet> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(dietProp -> {
                    final Diet diet = dietProp.getValue();
                    return diet.getName().toLowerCase().contains(newVal.toLowerCase())
                            || Integer.toString(diet.getCaloriesMin()).contains(newVal.toLowerCase())
                            || Integer.toString(diet.getCaloriesMax()).contains(newVal.toLowerCase())
                            || Integer.toString(diet.getProteinsMin()).contains(newVal.toLowerCase())
                            || Integer.toString(diet.getProteinsMax()).contains(newVal.toLowerCase())
                            || Integer.toString(diet.getCarbsMin()).contains(newVal.toLowerCase())
                            || Integer.toString(diet.getCarbsMax()).contains(newVal.toLowerCase())
                            || Integer.toString(diet.getLipidsMin()).contains(newVal.toLowerCase())
                            || Integer.toString(diet.getLipidsMax()).contains(newVal.toLowerCase())
                            ;
                });
    }

    private void loadData() throws SQLException {
        ObservableList<Diet> diets = FXCollections.observableArrayList();
        String q = "SELECT * FROM regime";
        ResultSet resultSet = handler.execQuery(q);
        if (resultSet != null) {
            while (resultSet.next()) {
                Diet diet = new Diet(
                        resultSet.getString("nom"),
                        resultSet.getString("description"),
                        resultSet.getInt("energie_kcal_min"),
                        resultSet.getInt("energie_kcal_max"),
                        resultSet.getInt("proteines_min"),
                        resultSet.getInt("proteines_max"),
                        resultSet.getInt("glucides_min"),
                        resultSet.getInt("glucides_max"),
                        resultSet.getInt("lipides_min"),
                        resultSet.getInt("lipides_max")
                );
                diets.add(diet);
            }

            treeTableView.setRoot(new RecursiveTreeItem<>(diets, RecursiveTreeObject::getChildren));
            treeTableView.setShowRoot(false);
        }
    }

    public void dietEditOption() {
        Diet diet = treeTableView.getSelectionModel().getSelectedItem().getValue();
        name.setText(diet.getName());
        description.setText(diet.getDescription());
        caloriesMin.setText(Integer.toString(diet.getCaloriesMin()));
        caloriesMax.setText(Integer.toString(diet.getCaloriesMax()));
        proteinsMin.setValue(diet.getProteinsMin());
        proteinsMax.setValue(diet.getProteinsMax());
        carbsMin.setValue(diet.getCarbsMin());
        carbsMax.setValue(diet.getCarbsMax());
        lipidsMin.setValue(diet.getLipidsMin());
        lipidsMax.setValue(diet.getLipidsMax());
        System.out.println(diet);
        dietEditDialog.setTransitionType(JFXDialog.DialogTransition.TOP);
        dietEditDialog.show((StackPane) context.getRegisteredObject("ContentPane"));
    }

    public void dietDeleteOption() {
        Diet diet = treeTableView.getSelectionModel().getSelectedItem().getValue();
        if(handler.deleteDiet(diet)) {
            AlertMaker.showInfo("Régime supprimé", "Suppression effectuée");
        } else {
            AlertMaker.showError("Impossible de supprimer le régime", "Une erreur s'est produite");
        }
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
