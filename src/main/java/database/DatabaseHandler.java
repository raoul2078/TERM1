package database;

import model.Diet;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.Properties;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:sqlite:term1.db";
    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() {
        createConnection();
        setupFoodTable();
        setupDietTable();
        setupDoctorTable();
        setupPatientTable();
        setupLikeTable();
        setupDislikeTable();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    private void createConnection() {
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(DB_URL, config.toProperties());
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Something went wrong: " + e.getMessage());
            System.exit(0);
        }
    }

    private void setupFoodTable() {
        String tableName = "aliment";
        try {
            stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + "("
                    + "alim_grp_code INT,"
                    + "alim_grp_nom_fr VARCHAR(50),"
                    + "alim_ssgrp_nom_fr VARCHAR(45),"
                    + "alim_ssssgrp_nom_fr VARCHAR(40),"
                    + "alim_code INT PRIMARY KEY,"
                    + "alim_nom_fr VARCHAR(100),"
                    + "fractionnable TINYINT,"
                    + "unite_de_base INT,"
                    + "portion_min_g INT,"
                    + "portion_max_g INT,"
                    + "portion_moy_g INT,"
                    + "energie_kcal NUMERIC(5,2),"
                    + "proteines_g NUMERIC(5,2),"
                    + "glucides_g NUMERIC(5,2),"
                    + "lipides_g NUMERIC(5,2)"
                    + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupDietTable() {
        String tableName = "regime";
        try {
            stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + "("
                    + "nom VARCHAR(255) PRIMARY KEY ,"
                    + "description VARCHAR(255),"
                    + "energie_kcal_min NUMERIC(5,2),"
                    + "energie_kcal_max NUMERIC(5,2),"
                    + "proteines_min NUMERIC(5,2),"
                    + "proteines_max NUMERIC(5,2),"
                    + "glucides_min NUMERIC(5,2),"
                    + "glucides_max NUMERIC(5,2),"
                    + "lipides_min NUMERIC(5,2),"
                    + "lipides_max NUMERIC(5,2)"
                    + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupDoctorTable() {
        String tableName = "docteur";
        try {
            stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + "("
                    + "login VARCHAR(40) PRIMARY KEY ,"
                    + "mdp VARCHAR(255),"
                    + "nom VARCHAR(255),"
                    + "prenom VARCHAR(255),"
                    + "email VARCHAR(40),"
                    + "tel VARCHAR(40)"
                    + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupPatientTable() {
        String tableName = "patient";

        try {
            stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + "nom VARCHAR(255),"
                    + "prenom VARCHAR(255),"
                    + "taille INT,"
                    + "poids NUMERIC(5,2),"
                    + "email VARCHAR(40),"
                    + "tel VARCHAR(255),"
                    + "docteur VARCHAR(40),"
                    + "regime VARCHAR(455),"
                    + "CONSTRAINT fk_patient_reg FOREIGN KEY (regime) REFERENCES regime(nom) ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "CONSTRAINT fk_patient_doc FOREIGN KEY (docteur) REFERENCES docteur(login) ON DELETE CASCADE ON UPDATE CASCADE"
                    + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupLikeTable() {
        String tableName = "aime";

        try {
            stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + "("
                    + "idPatient INT ,"
                    + "idAlim_code INT,"
                    + "CONSTRAINT fk_aime_pat FOREIGN KEY (idPatient) REFERENCES patient(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "CONSTRAINT fk_aime_alim FOREIGN KEY (idAlim_code) REFERENCES aliment(alim_code) ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "CONSTRAINT pk_aime PRIMARY KEY (idPatient,idAlim_code)"
                    + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupDislikeTable() {
        String tableName = "aime_pas";

        try {
            stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS " + tableName + "("
                    + "idPatient INT ,"
                    + "idAlim_code INT,"
                    + "CONSTRAINT fk_aime_pas_pat FOREIGN KEY (idPatient) REFERENCES patient(id) ON DELETE CASCADE ON UPDATE CASCADE ,"
                    + "CONSTRAINT fk_aime_pas_alim FOREIGN KEY (idAlim_code) REFERENCES aliment(alim_code) ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "CONSTRAINT pk_aime_pas PRIMARY KEY (idPatient, idAlim_code)"
                    + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Exception at execQuery " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public boolean execAction(String query) {
        try {
            stmt = conn.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.println("Exception at execAction " + e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDiet(Diet diet, String oldName) {
        String update = "UPDATE regime SET nom = ?, description = ?, energie_kcal_min = ?, energie_kcal_max = ?, proteines_min = ?, proteines_max = ?, glucides_min = ?, glucides_max = ?, lipides_min = ?, lipides_max = ? WHERE nom = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, diet.getName());
            stmt.setString(2, diet.getDescription());
            stmt.setInt(3, diet.getCaloriesMin());
            stmt.setInt(4, diet.getCaloriesMax());
            stmt.setInt(5, diet.getProteinsMin());
            stmt.setInt(6, diet.getProteinsMax());
            stmt.setInt(7, diet.getCarbsMin());
            stmt.setInt(8, diet.getCarbsMax());
            stmt.setInt(9, diet.getLipidsMin());
            stmt.setInt(10, diet.getLipidsMax());
            stmt.setString(11, oldName);

            int res = stmt.executeUpdate();
            return res > 0;
        } catch (SQLException e) {
            System.out.println("Exception at updateDiet " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDiet(Diet diet) {
        String delete = "DELETE FROM regime WHERE nom = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(delete);
            stmt.setString(1, diet.getName());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Exception at deleteDiet " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet getPatients(String doctor) {
        String q = "SELECT * FROM patient WHERE docteur = ?";
        ResultSet result;
        try {
            PreparedStatement stmt = conn.prepareStatement(q);
            stmt.setString(1, doctor);
            result = stmt.executeQuery();
            return result;
        } catch (SQLException e) {
            System.out.println("Exception at getPatient " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getFavoriteFoods(int patientId) {
        String q = "SELECT alim_code, alim_nom_fr " +
                "FROM aliment, aime WHERE aime.idPatient = ? " +
                "AND aliment.alim_code = aime.idAlim_code";
        ResultSet resultSet;
        try {
            PreparedStatement stmt = conn.prepareStatement(q);
            stmt.setInt(1, patientId);
            resultSet = stmt.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteFavoriteFood(int foodId, int patientId) {
        String q = "DELETE FROM aime WHERE idPatient = ? AND idAlim_code = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(q);
            stmt.setInt(1, patientId);
            stmt.setInt(2, foodId);
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
