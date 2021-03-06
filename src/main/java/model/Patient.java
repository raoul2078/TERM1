package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class Patient extends RecursiveTreeObject<Patient> {
    private SimpleIntegerProperty id;
    private StringProperty lastName;
    private StringProperty firstName;
    private StringProperty email;
    private StringProperty phone;
    private SimpleIntegerProperty height;
    private SimpleDoubleProperty weight;
    private StringProperty diet;

    public Patient(int id, String firstName, String lastName, String email, String phone, int height, double weight, String diet) {
        this.id = new SimpleIntegerProperty(id);
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.height = new SimpleIntegerProperty(height);
        this.weight = new SimpleDoubleProperty(weight);
        this.diet = new SimpleStringProperty(diet);
    }


    public int getId() {
        return id.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public double getHeight() {
        return height.get();
    }

    public SimpleIntegerProperty heightProperty() {
        return height;
    }

    public double getWeight() {
        return weight.get();
    }

    public SimpleDoubleProperty weightProperty() {
        return weight;
    }

    public String getDiet() {
        return diet.get();
    }

    public StringProperty dietProperty() {
        return diet;
    }


    @Override
    public String toString() {
        return "Patient{" +
                "lastName=" + lastName.get() +
                ", firstName=" + firstName.get() +
                ", email=" + email.get() +
                ", phone=" + phone.get() +
                ", height=" + height.get() +
                ", weight=" + weight.get() +
                ", diet=" + diet.get() +
                '}';
    }
}
