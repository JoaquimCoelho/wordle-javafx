package com.example.wordle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class LoginController {

    public static User currentUser;
    @FXML
    private TextField uField;
    @FXML
    private PasswordField pField;
    @FXML
    private Button bLogin, bRegister;
    @FXML
    private Label status;

    public void initialize() {
        bLogin.setOnAction(e -> {
            String username = uField.getText();
            String password = pField.getText();
            int result = User.checkUser(username, password);
            if (result == 0) {
                currentUser = User.getUser(username);
                System.out.println("Login successful");
                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    stage.setTitle("Wordle Game");
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();

                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            else if (result == 1) {
                System.out.println("Incorrect password");
                status.setText("Incorrect password");
            }
            else if (result == 2) {
                System.out.println("User not found");
                status.setText("User not found");
            }
            else {
                System.out.println("Error logging in");
                status.setText("Error logging in");
            }
        });

        bRegister.setOnAction(e -> {
            String username = uField.getText();
            String password = pField.getText();
            new User(username, password, 0);
            if (User.isDuplicateUsername(username)) {
                status.setText("Username already exists");
            }
            else {
                status.setText("Registration successful");
            }
        });

    }
}
