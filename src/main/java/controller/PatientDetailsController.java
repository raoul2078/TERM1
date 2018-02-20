package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.DatabaseHandler;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.Food;
import model.Patient;
import utils.AlertMaker;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ViewController(value = "/view/PatientDetails.fxml", title = "My App")
public class PatientDetailsController {

    DatabaseHandler handler;
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private StackPane root;
    @FXML
    private HBox hboxData;

    @FXML
    private Label name;
    @FXML
    private Label diet;
    @FXML
    private Label heightAndWeight;
    @FXML
    private Label email;
    @FXML
    private Label phone;
    @FXML
    private JFXListView<Label> list;


    @FXML
    private JFXTreeTableView<Food> favoriteFoodsTreeTableView;
    @FXML
    private JFXTreeTableColumn<Food, String> favoriteFoodsNameColumn;

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

    Patient patient;

    @FXML
    JFXTreeTableView<Food> allFoodsTableView;
    @FXML
    JFXTreeTableColumn<Food, String> allFoodsNameColumn;
    @FXML
    JFXTreeTableColumn<Food, Integer> allFoodsCaloriesColumn;
    @FXML
    JFXTreeTableColumn<Food, Integer> allFoodsProteinsColumn;
    @FXML
    JFXTreeTableColumn<Food, Integer> allFoodsCarbsColumn;
    @FXML
    private
    JFXTreeTableColumn<Food, Integer> allFoodsLipidsColumn;
    @FXML
    private JFXTextField searchField;

    @FXML
    JFXListView<Food> menuFoodsListView;

    ListProperty<Food> menuFoodsListProperty = new SimpleListProperty<>();
    ObservableList<Food> foodObservableList = FXCollections.observableArrayList();

    @PostConstruct
    public void init() {
        handler = DatabaseHandler.getInstance();
        Objects.requireNonNull(context, "context");
        patient = (Patient) context.getRegisteredObject("Patient");
        name.setText(patient.getFirstName() + " " + patient.getLastName());
        heightAndWeight.setText(String.valueOf(patient.getHeight())
                + "cm, "
                + String.valueOf(patient.getWeight())
                + "kg"
        );
        email.setText(patient.getEmail());
        phone.setText(patient.getPhone());
        diet.setText("Régime " + patient.getDiet());
        setupFavoriteFoodsTable();
        setupAllFoodsTable();
        loadData();

        caloriesMin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,5}?")) {
                caloriesMin.setText(oldValue);
            }
        });
        caloriesMax.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,5}?")) {
                caloriesMax.setText(oldValue);
            }
        });

        menuFoodsListView.itemsProperty().bind(menuFoodsListProperty);
        menuFoodsListProperty.set(foodObservableList);
    }


    private void setupFavoriteFoodsTable() {
        favoriteFoodsTreeTableView.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        favoriteFoodsNameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
    }

    private void setupAllFoodsTable() {
        allFoodsTableView.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        allFoodsNameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        allFoodsCaloriesColumn.setCellValueFactory(param -> param.getValue().getValue().caloriesProperty().asObject());
        allFoodsProteinsColumn.setCellValueFactory(param -> param.getValue().getValue().proteinsProperty().asObject());
        allFoodsCarbsColumn.setCellValueFactory(param -> param.getValue().getValue().carbsProperty().asObject());
        allFoodsLipidsColumn.setCellValueFactory(param -> param.getValue().getValue().lipidsProperty().asObject());

        searchField.textProperty().addListener(setupSearchField(allFoodsTableView));
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Food> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(foodProp -> {
                    final Food food = foodProp.getValue();
                    return food.getName().toLowerCase().contains(newVal.toLowerCase())
                            || Integer.toString(food.getCalories()).contains(newVal.toLowerCase())
                            || Integer.toString(food.getProteins()).contains(newVal.toLowerCase())
                            || Integer.toString(food.getCarbs()).contains(newVal.toLowerCase())
                            || Integer.toString(food.getLipids()).contains(newVal.toLowerCase());
                });
    }


    private void loadData() {
        try {
            loadFavoriteFoodsData();
            loadAllFoodsData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFavoriteFoodsData() throws SQLException {
        ObservableList<Food> data = FXCollections.observableArrayList();
        ResultSet resultSet = handler.getFavoriteFoods(patient.getId());
        if (resultSet != null) {
            while (resultSet.next()) {
                Food food = new Food(resultSet.getString("alim_nom_fr"), resultSet.getInt("alim_code"));
                data.add(food);
            }
            favoriteFoodsTreeTableView.setRoot(new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren));
        }
        favoriteFoodsTreeTableView.setShowRoot(false);
    }

    private void loadAllFoodsData() throws SQLException {
        String q = "SELECT * FROM aliment";
        ObservableList<Food> foods = FXCollections.observableArrayList();
        ResultSet resultSet = handler.execQuery(q);
        if (resultSet != null) {
            while (resultSet.next()) {
                Food food = new Food(
                        resultSet.getInt("alim_grp_code"),
                        resultSet.getString("alim_grp_nom_fr"),
                        resultSet.getString("alim_ssgrp_nom_fr"),
                        resultSet.getString("alim_ssssgrp_nom_fr"),
                        resultSet.getInt("alim_code"),
                        resultSet.getString("alim_nom_fr"),
                        resultSet.getInt("fractionnable"),
                        resultSet.getInt("unite_de_base"),
                        resultSet.getInt("portion_min_g"),
                        resultSet.getInt("portion_max_g"),
                        resultSet.getInt("portion_moy_g"),
                        resultSet.getInt("energie_kcal"),
                        resultSet.getInt("proteines_g"),
                        resultSet.getInt("glucides_g"),
                        resultSet.getInt("lipides_g")

                );
                foods.add(food);
            }
            allFoodsTableView.setRoot(new RecursiveTreeItem<>(foods, RecursiveTreeObject::getChildren));
            allFoodsTableView.setShowRoot(false);
        }
    }

    public void navigateBack() throws VetoException, FlowException {
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        contentFlowHandler.navigateBack();
    }

    public void addToMenu() {
        Food food = allFoodsTableView.getSelectionModel().getSelectedItem().getValue();
        foodObservableList.add(food);
    }

    public void deleteFromMenu() {
        Food food = allFoodsTableView.getSelectionModel().getSelectedItem().getValue();
        foodObservableList.remove(food);
    }

    public void addToFavorite() {
        Food food = allFoodsTableView.getSelectionModel().getSelectedItem().getValue();
        String q = "INSERT INTO aime VALUES ("
                + patient.getId() + ","
                + food.getAlim_code()
                + ")";
        if (handler.execAction(q)) {
            AlertMaker.showInfo("Ajout aliment", "Aliment ajouté");
        } else {
            AlertMaker.showError("Ajout aliment impossible", "Une erreur s'est produite");
        }
        try {
            loadFavoriteFoodsData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromFavorite() {
        Food food = allFoodsTableView.getSelectionModel().getSelectedItem().getValue();
        if (handler.deleteFavoriteFood(food.getAlim_code(), patient.getId())) {
            AlertMaker.showInfo("Suppression aliment", "Aliment retiré");
        } else {
            AlertMaker.showError("Suppression aliment impossible", "Une erreur s'est produite");
        }
        try {
            loadFavoriteFoodsData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromFavoriteBis() {
        Food food = favoriteFoodsTreeTableView.getSelectionModel().getSelectedItem().getValue();
        if (handler.deleteFavoriteFood(food.getAlim_code(), patient.getId())) {
            AlertMaker.showInfo("Suppression aliment", "Aliment retiré");
        } else {
            AlertMaker.showError("Suppression aliment impossible", "Une erreur s'est produite");
        }
        try {
            loadFavoriteFoodsData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateMeal() {

    }
}
