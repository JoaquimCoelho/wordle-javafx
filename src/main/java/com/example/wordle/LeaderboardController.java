package com.example.wordle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class LeaderboardController {

    @FXML private Label firstPlaceLabel, secondPlaceLabel, thirdPlaceLabel, fourthPlaceLabel, fifthPlaceLabel, sixthPlaceLabel, seventhPlaceLabel, eighthPlaceLabel, ninthPlaceLabel, tenthPlaceLabel;
    @FXML
    private Button bVoltar;


    @FXML
    public void initialize() {

        bVoltar.setOnAction(e -> {
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
        });


        updateLeaderboard();
    }

    private void updateLeaderboard() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            int count = 1;

            while ((line = reader.readLine()) != null && count <= 10) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String username = parts[0];
                    int score = Integer.parseInt(parts[2]);
                    updateLabel(count, username, score);
                    count++;
                }
            }

            // Clear remaining labels if there are fewer than 10 entries
            clearRemainingLabels(count);

        } catch (IOException e) {
            System.out.println("Error reading users file");
            e.printStackTrace();
        }
    }

    private void updateLabel(int position, String username, int score) {
        Label label = getLabelByPosition(position);
        if (label != null) {
            if (label.equals(firstPlaceLabel)) {
                label.setText("1st Place: " + username + " - " + score);
            }
            else if (label.equals(secondPlaceLabel)) {
                label.setText("2nd Place: " + username + " - " + score);
            }
            else if (label.equals(thirdPlaceLabel)) {
                label.setText("3rd Place: " + username + " - " + score);
            }
            else {
                label.setText(position + "th Place: " + username + " - " + score);
            }
        }
    }

    private Label getLabelByPosition(int position) {
        switch (position) {
            case 1: return firstPlaceLabel;
            case 2: return secondPlaceLabel;
            case 3: return thirdPlaceLabel;
            case 4: return fourthPlaceLabel;
            case 5: return fifthPlaceLabel;
            case 6: return sixthPlaceLabel;
            case 7: return seventhPlaceLabel;
            case 8: return eighthPlaceLabel;
            case 9: return ninthPlaceLabel;
            case 10: return tenthPlaceLabel;
            default: return null;
        }
    }

    private void clearRemainingLabels(int start) {
        for (int i = start; i <= 10; i++) {
            Label label = getLabelByPosition(i);
            if (label != null) {
                label.setText("");
            }
        }
    }
}
