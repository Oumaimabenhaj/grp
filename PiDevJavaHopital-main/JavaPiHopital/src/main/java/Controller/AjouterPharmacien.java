package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Model.getData;
import Service.PasswordComplexityChecker;
import Model.Pharmacien;
import Test.HelloApplication;
import Util.DataBase;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.paint.Color;

public class AjouterPharmacien {
    @FXML
    private RadioButton InterlockTF;
    @FXML
    private AnchorPane main_form;

    @FXML
    private Label agelabel;

    @FXML
    private TextField cinTF;

    @FXML
    private Label passwordComplexityLabel;

    @FXML
    private Button close;

    @FXML
    private DatePicker date_de_naissanceTF;

    @FXML
    private TextField emailTF;

    @FXML
    private RadioButton genreTF;

    @FXML
    private RadioButton genreTF1;

    @FXML
    private ImageView imageTF;

    @FXML
    private RadioButton interlockTF;

    @FXML
    private Button minimise;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField numtelTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private TableColumn<?, ?> pharmaciencol_cin;

    @FXML
    private TableColumn<?, ?> pharmaciencol_datedenaissance;

    @FXML
    private TableColumn<?, ?> pharmaciencol_email;

    @FXML
    private TableColumn<?, ?> pharmaciencol_genre;

    @FXML
    private TableColumn<?, ?> pharmaciencol_id;

    @FXML
    private TableColumn<?, ?> pharmaciencol_image;

    @FXML
    private TableColumn<?, ?> pharmaciencol_interlock;

    @FXML
    private TableColumn<?, ?> pharmaciencol_nom;

    @FXML
    private TableColumn<?, ?> pharmaciencol_numtel;

    @FXML
    private TableColumn<?, ?> pharmaciencol_password;

    @FXML
    private TableColumn<?, ?> pharmaciencol_poste;

    @FXML
    private TableColumn<?, ?> pharmaciencol_prenom;

    @FXML
    private TableColumn<?, ?> pharmaciencol_role;

    @FXML
    private TableView<Pharmacien> pharmaciencol_tableview;

    @FXML
    private RadioButton posteTF;

    @FXML
    private RadioButton posteTF1;

    @FXML
    private TextField prenomTF;

    @FXML
    private TextField recherche_Pharmacien;

    @FXML
    private RadioButton roleTF;

    @FXML
    private RadioButton roleTF1;

    @FXML
    private RadioButton roleTF2;

    @FXML
    private RadioButton roleTF3;
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;

    @FXML
    void addPharmacienSelect() {
        Pharmacien pharmacien1 = pharmaciencol_tableview.getSelectionModel().getSelectedItem();
        int num = pharmaciencol_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        cinTF.setText(String.valueOf(pharmacien1.getCin()));
        nomTF.setText(pharmacien1.getNom());
        prenomTF.setText(pharmacien1.getPrenom());

        // Décocher toutes les radio box
        genreTF.setSelected(false);
        genreTF1.setSelected(false);

        // Vérifier le genre et sélectionner le radio bouton approprié
        int genre = pharmacien1.getGenre();
        if (genre == 1) { // Suppose que 1 correspond à "Homme"
            genreTF.setSelected(true);
        } else if (genre == 0) { // Suppose que 2 correspond à "Femme"
            genreTF1.setSelected(true);
        }
        posteTF.setSelected(false);
        posteTF1.setSelected(false);

        int poste = pharmacien1.getPoste();
        if (poste == 1) { // Suppose que 1 correspond à "jour"
            posteTF.setSelected(true);
        } else if (poste == 0) { // Suppose que 2 correspond à "Femme"
            posteTF1.setSelected(true);
        }

        // Convertir Date en LocalDate
        Instant instant = pharmacien1.getDateNaissance().toInstant();
        LocalDate dateNaissance = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // Définir la date de naissance dans le DatePicker
        date_de_naissanceTF.setValue(dateNaissance);
        numtelTF.setText(String.valueOf(pharmacien1.getNumtel()));
        emailTF.setText(pharmacien1.getEmail());
        passwordTF.setText(pharmacien1.getPassword());
        interlockTF.setSelected(false);
        InterlockTF.setSelected(false);

        // Vérifier la valeur du champ interlock et sélectionner le radio bouton approprié
        boolean interlock = pharmacien1.isInterlock();
        if (interlock) {
            interlockTF.setSelected(true);
        } else {
            InterlockTF.setSelected(true);
        }
        getData.path = pharmacien1.getImage();
        String uri = "file:" + pharmacien1.getImage();
        image = new Image(uri, 118, 139, false, true);
        imageTF.setImage(image);

    }

    @FXML
    void ajouterPharmacienInsertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());
        if (file != null){
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(),118, 139, false, true);
            imageTF.setImage(image);
        }

    }

    @FXML
    void ajouterPharmacienReset() {
        cinTF.setText("");
        nomTF.setText("");
        prenomTF.setText("");
        date_de_naissanceTF.setValue(null);
        numtelTF.setText("");
        emailTF.setText("");
        passwordTF.setText("");
        imageTF.setImage(null);
        getData.path = "";
        genreTF.setSelected(false);
        genreTF1.setSelected(false);
        interlockTF.setSelected(false);
        InterlockTF.setSelected(false);
        posteTF.setSelected(false);
        posteTF1.setSelected(false);




    }

    @FXML
    void ajouterPharmacienadd() {
        String sql = "INSERT INTO global_user " +
                "(cin, nom, prenom, genre, datenaissance, numtel, email, password, interlock, image, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlPharmacien = "INSERT INTO pharmacien (poste, id) VALUES (?, LAST_INSERT_ID())";

        Connection connect = null;
        PreparedStatement prepare = null;

        try {
            Alert alert;
            if (cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() ||
                    genreTF.getText().isEmpty() || InterlockTF.getText().isEmpty() || date_de_naissanceTF.getValue() == null ||
                    numtelTF.getText().isEmpty() || emailTF.getText().isEmpty() || passwordTF.getText().isEmpty() ||
                    getData.path == null || getData.path.equals("")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides !");
                alert.showAndWait();
            } else {
                // Vérifier si une seule case à cocher est sélectionnée pour le genre
                if (!(genreTF.isSelected() ^ genreTF1.isSelected())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez sélectionner un seul genre !");
                    alert.showAndWait();
                    return; // Arrêter l'exécution de la méthode
                }else if (!isValidCIN(cinTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("CIN invalide !");
                alert.showAndWait();
            } else if (!isValidName(nomTF.getText()) || !isValidName(prenomTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Nom ou prénom invalide !");
                alert.showAndWait();
            } else if (!isValidDateOfBirth(date_de_naissanceTF.getValue())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("L'utilisateur doit avoir plus de 23 ans !");
                alert.showAndWait();
            } else if (!isValidPhoneNumber(numtelTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Numéro de téléphone invalide !");
                alert.showAndWait();
            } else if (!isValidEmail(emailTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Email invalide !");
                alert.showAndWait();
            } else if (!isValidPassword(passwordTF.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Mot de passe invalide ! Il doit contenir au moins 8 caractères alphanumériques.");
                alert.showAndWait();
            } else {
                connect = DataBase.getInstance().getCnx();
                String check = "SELECT cin FROM global_user WHERE cin = ?";
                prepare = connect.prepareStatement(check);
                prepare.setInt(1, Integer.parseInt(cinTF.getText()));
                ResultSet result = prepare.executeQuery();
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Pharmacien existe déjà !");
                    alert.showAndWait();
                } else {
                    // Afficher une alerte de confirmation pour ajouter le pharmacien
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText(null);
                    confirmationAlert.setContentText("Voulez-vous ajouter ce pharmacien ?");

                    // Ajouter des boutons de confirmation et d'annulation
                    ButtonType confirmButton = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

                    // Ajouter les boutons à l'alerte
                    confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

                    // Attendre la réponse de l'utilisateur
                    Optional<ButtonType> userChoice = confirmationAlert.showAndWait();

                    // Si l'utilisateur confirme, procéder à l'ajout du pharmacien
                    if (userChoice.isPresent() && userChoice.get() == confirmButton) {
                        String hashedPassword = BCrypt.hashpw(passwordTF.getText(), BCrypt.gensalt());

                        prepare = connect.prepareStatement(sql);
                        prepare.setInt(1, Integer.parseInt(cinTF.getText()));
                        prepare.setString(2, nomTF.getText());
                        prepare.setString(3, prenomTF.getText());

                        // Déterminer le genre en fonction de la case à cocher sélectionnée
                        if (genreTF.isSelected()) {
                            prepare.setBoolean(4, true); // Homme
                        } else {
                            prepare.setBoolean(4, false); // Femme
                        }

                        LocalDate dateNaissance = date_de_naissanceTF.getValue();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String dateNaissanceFormatee = dateNaissance.format(formatter);
                        prepare.setString(5, dateNaissanceFormatee);
                        prepare.setInt(6, Integer.parseInt(numtelTF.getText()));
                        prepare.setString(7, emailTF.getText());
                        prepare.setString(8, hashedPassword); // Utiliser le mot de passe crypté
                        prepare.setString(10, getData.path.replace("\\", "\\\\"));

                        // Déterminer la valeur interlock en fonction de la case à cocher sélectionnée
                        if (interlockTF.isSelected()) {
                            prepare.setInt(9, 1); // Oui
                        } else {
                            prepare.setInt(9, 0); // Non
                        }

                        // Définir le rôle par défaut sur "pharmacien"
                        prepare.setString(11, "pharmacien");

                        // Exécuter la requête SQL pour insérer dans la table global_user
                        prepare.executeUpdate();

                        // Insérer numcarte dans la table pharmacien
                        prepare = connect.prepareStatement(sqlPharmacien);
                        if (posteTF.isSelected()) {
                            prepare.setInt(1, 1); // Jour
                        } else {
                            prepare.setInt(1, 0); // Nuit
                        }

                        // Exécuter la requête SQL pour insérer dans la table pharmacien
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Ajout avec succès !");
                        alert.showAndWait();

                        addPharmacienShowList();
                        addPharmacienSelect();
                    }}
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        @FXML
    void close() {
        System.exit(0);

    }

    @FXML
    void minimise() {
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);

    }

    public ObservableList<Pharmacien> addPharmacienListData() throws SQLException {
        ObservableList<Pharmacien> list = FXCollections.observableArrayList();
        String sql = "SELECT u.id, u.cin, u.nom, u.prenom, u.genre, u.datenaissance, u.numtel, u.email, u.password, u.interlock, u.role,u.image, p.poste " +
                "FROM global_user u " +
                "JOIN pharmacien p ON u.id = p.id";
        connect = DataBase.getInstance().getCnx();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                Pharmacien pharmacien = new Pharmacien(
                        result.getInt("id"),
                        result.getInt("cin"),
                        result.getInt("numtel"),
                        result.getInt("genre"),
                        result.getString("nom"),
                        result.getString("prenom"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("image"),
                        result.getString("role"),
                        result.getTimestamp("datenaissance"),
                        result.getBoolean("interlock"),
                        result.getInt("poste")
                );
                list.add(pharmacien);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ObservableList<Pharmacien> addPharmacienList;
    public void addPharmacienShowList() throws SQLException {
        addPharmacienList = addPharmacienListData();
        pharmaciencol_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        pharmaciencol_poste.setCellValueFactory(new PropertyValueFactory<>("poste"));
        pharmaciencol_cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        pharmaciencol_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pharmaciencol_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        pharmaciencol_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        pharmaciencol_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        pharmaciencol_numtel.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        pharmaciencol_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        pharmaciencol_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        pharmaciencol_interlock.setCellValueFactory(new PropertyValueFactory<>("interlock"));
        pharmaciencol_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        pharmaciencol_image.setCellValueFactory(new PropertyValueFactory<>("image"));



        pharmaciencol_tableview.setItems(addPharmacienList);
        //addEventSearch();
    }

    public void initialize() throws SQLException {
        addPharmacienList = addPharmacienListData(); // Initialize addEventList
        addPharmacienShowList(); // Populate TableView with data from addEventList
        addPharmacienSelect();
        //setupSearchListener();
        date_de_naissanceTF.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Calculer l'âge à partir de la date de naissance
            if (newValue != null) {
                LocalDate currentDate = LocalDate.now();
                Period period = Period.between(newValue, currentDate);
                int age = period.getYears();
                agelabel.setTextFill(Color.RED);
                agelabel.setText("L'Âge est : " + age); // Mettre à jour le Label avec l'âge calculé
            } else {
                agelabel.setText(""); // Réinitialiser le Label si aucune date de naissance n'est sélectionnée
            }
        });

        // Ajouter un écouteur au champ de mot de passe pour calculer la complexité
        passwordTF.textProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifier la complexité du mot de passe
            String passwordComplexity = PasswordComplexityChecker.checkPasswordComplexity(newValue);
            // Mettre à jour le Label avec la complexité du mot de passe
            passwordComplexityLabel.setText("Complexité du mot de passe : " + passwordComplexity);
        });
    }

    @FXML
    void modifierPharmacienupdate() {
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sqlGlobalUser = "UPDATE global_user SET cin = ?, nom = ?, prenom = ?, genre = ?, datenaissance = ?, numtel = ?, password = ?, interlock = ?, role = ?, image = ? WHERE cin = ?";
        String sqlPharmacien = "UPDATE pharmacien SET poste = ? WHERE id = (SELECT id FROM global_user WHERE cin = ?)";
        Connection connect = null;
        PreparedStatement prepareGlobalUser = null;
        PreparedStatement preparePharmacien = null;

        try {
            Alert alert;
            if (posteTF.getText().isEmpty() || cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() ||
                    date_de_naissanceTF.getValue() == null || numtelTF.getText().isEmpty() || emailTF.getText().isEmpty() ||
                    passwordTF.getText().isEmpty() || (!interlockTF.isSelected() && !InterlockTF.isSelected()) || uri == null || uri.equals("")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides !");
                alert.showAndWait();
            } else {
                // Vérifier si une seule case à cocher est sélectionnée pour le genre
                if (!(genreTF.isSelected() ^ genreTF1.isSelected())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez sélectionner un seul genre !");
                    alert.showAndWait();
                    return; // Arrêter l'exécution de la méthode
                }

                // Vérification des données saisies
                if (!isValidCIN(cinTF.getText())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("CIN invalide !");
                    alert.showAndWait();
                    return; // Arrêter l'exécution de la méthode
                }
                if (!isValidName(nomTF.getText()) || !isValidName(prenomTF.getText())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Nom ou prénom invalide !");
                    alert.showAndWait();
                    return; // Arrêter l'exécution de la méthode
                }
                if (!isValidDateOfBirth(date_de_naissanceTF.getValue())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("L'utilisateur doit avoir plus de 23 ans !");
                    alert.showAndWait();
                    return; // Arrêter l'exécution de la méthode
                }
                if (!isValidPhoneNumber(numtelTF.getText())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Numéro de téléphone invalide !");
                    alert.showAndWait();
                    return; // Arrêter l'exécution de la méthode
                }
                if (!isValidEmail(emailTF.getText())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Email invalide !");
                    alert.showAndWait();
                    return; // Arrêter l'exécution de la méthode
                }
                if (!isValidPassword(passwordTF.getText())) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Mot de passe invalide ! Il doit contenir au moins 8 caractères alphanumériques.");
                    alert.showAndWait();
                    return; // Arrêter l'exécution de la méthode
                }

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous modifier ces informations!");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    // Récupérer le genre sélectionné
                    int genreSelectionne = genreTF.isSelected() ? 1 : 0; // 1 pour Homme, 0 pour Femme

                    // Obtention d'une nouvelle connexion à chaque fois
                    connect = DataBase.getInstance().getCnx();

                    // Préparation des requêtes avec des paramètres
                    prepareGlobalUser = connect.prepareStatement(sqlGlobalUser);
                    preparePharmacien = connect.prepareStatement(sqlPharmacien);

                    // Crypter le mot de passe avec bcrypt
                    String hashedPassword = BCrypt.hashpw(passwordTF.getText(), BCrypt.gensalt());

                    // Remplir les paramètres pour la requête global_user
                    prepareGlobalUser.setInt(1, Integer.parseInt(cinTF.getText()));
                    prepareGlobalUser.setString(2, nomTF.getText());
                    prepareGlobalUser.setString(3, prenomTF.getText());
                    prepareGlobalUser.setInt(4, genreSelectionne); // Utilisation du genre sélectionné
                    prepareGlobalUser.setObject(5, date_de_naissanceTF.getValue());
                    prepareGlobalUser.setInt(6, Integer.parseInt(numtelTF.getText()));
                    prepareGlobalUser.setString(7, hashedPassword); // Utilisation du mot de passe crypté
                    prepareGlobalUser.setInt(8, interlockTF.isSelected() ? 1 : 0); // Valeur de l'interlock
                    prepareGlobalUser.setString(9, "pharmacien"); // Rôle
                    prepareGlobalUser.setString(10, uri); // Image
                    prepareGlobalUser.setString(11, cinTF.getText()); // Utilisation du cin comme condition WHERE

                    // Remplir les paramètres pour la requête pharmacien
                    int posteValue = posteTF.isSelected() ? 1 : 0;
                    preparePharmacien.setInt(1, posteValue); // Modification pour inclure numcarte
                    preparePharmacien.setString(2, cinTF.getText()); // Utilisation du cin comme condition WHERE

                    // Exécuter les requêtes de mise à jour
                    prepareGlobalUser.executeUpdate();
                    preparePharmacien.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Modification avec succès !");
                    alert.showAndWait();

                    addPharmacienShowList();
                    ajouterPharmacienReset();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void supprimerPharmaciendelete() {
        String sqlGlobalUser = "DELETE FROM global_user WHERE cin = ?";
        String sqlPharmacien = "DELETE FROM pharmacien WHERE id = (SELECT id FROM global_user WHERE cin = ?)";

        Connection connect = null;
        PreparedStatement prepareGlobalUser = null;
        PreparedStatement preparePatient = null;

        connect = DataBase.getInstance().getCnx();

        try {
            Alert alert;
            if (posteTF.getText().isEmpty() ||cinTF.getText().isEmpty() || nomTF.getText().isEmpty() || prenomTF.getText().isEmpty() ||
                    genreTF.getText().isEmpty() || date_de_naissanceTF.getValue() == null || numtelTF.getText().isEmpty() ||
                    emailTF.getText().isEmpty() || passwordTF.getText().isEmpty() || interlockTF.getText().isEmpty() ||
                   getData.path == null || getData.path.equals("")) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides !");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Voulez-vous supprimer ce patient ?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    // Suppression de l'utilisateur de la table global_user
                    prepareGlobalUser = connect.prepareStatement(sqlGlobalUser);
                    prepareGlobalUser.setString(1, cinTF.getText());
                    prepareGlobalUser.executeUpdate();

                    // Suppression de l'utilisateur de la table patient
                    preparePatient = connect.prepareStatement(sqlPharmacien);
                    preparePatient.setString(1, cinTF.getText());
                    preparePatient.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Suppression avec succès !");
                    alert.showAndWait();

                    addPharmacienShowList();
                    ajouterPharmacienReset();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    @FXML
    private Button gererpatientbtn;

    @FXML
    public void gererpatientbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterPatient.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererpatientbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Button gereradministrateurbtn;

    @FXML
    private Button gerermedecinbtn;

    @FXML
    private Button gererpharmacienbtn;

    @FXML
    private Button tableaudeboardbtn;

    @FXML
    public void tableaudeboardbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Dashboard.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) tableaudeboardbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void gereradministrateurbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterAdmin.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gereradministrateurbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void gerermedecin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterMedecin.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gerermedecinbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void gererpharmacienbtn(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AjouterPharmacien.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) gererpharmacienbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private Button logout_btn;
    @FXML
    void logout(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/login.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage)  logout_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private boolean isValidCIN(String cin) {
        // Vérifie si le CIN est composé de 8 chiffres uniquement
        return Pattern.matches("\\d{8}", cin);
    }

    private boolean isValidName(String name) {
        // Vérifie si le nom ou le prénom ne contient que des caractères alphabétiques
        return Pattern.matches("[a-zA-Z]+", name);
    }

    private boolean isValidDateOfBirth(LocalDate dateOfBirth) {
        // Vérifie si l'utilisateur a plus de 23 ans
        LocalDate now = LocalDate.now();
        Period period = Period.between(dateOfBirth, now);
        return period.getYears() > 23;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Vérifie si le numéro de téléphone contient exactement 8 chiffres
        return Pattern.matches("\\d{8}", phoneNumber);
    }

    private boolean isValidEmail(String email) {
        // Vérifie si l'email est valide en utilisant une expression régulière simple
        return Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", email);
    }

    private boolean isValidPassword(String password) {
        // Vérifie si le mot de passe a au moins 8 caractères alphanumériques
        return Pattern.matches("[a-zA-Z0-9]{8,}", password);
    }
}


