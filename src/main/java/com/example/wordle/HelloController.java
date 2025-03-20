package com.example.wordle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.animation.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static com.example.wordle.LoginController.currentUser;


public class HelloController {
    @FXML
    private Button bLogin, bLead, BACK_SPACE, INSERT, A, B, C, C2, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, filler;
    @FXML
    private MenuButton bLing, bDif;
    @FXML
    private MenuItem m3, m4, m5, m6, m7, en, fr, pt;
    @FXML
    private Label letraR1C1, letraR1C2, letraR1C3, letraR1C4, letraR1C5, letraR1C6, letraR1C7, letraR2C1, letraR2C2, letraR2C3, letraR2C4, letraR2C5, letraR2C6, letraR2C7, letraR3C1, letraR3C2, letraR3C3, letraR3C4, letraR3C5, letraR3C6, letraR3C7, letraR4C1, letraR4C2, letraR4C3, letraR4C4, letraR4C5, letraR4C6, letraR4C7, letraR5C1, letraR5C2, letraR5C3, letraR5C4, letraR5C5, letraR5C6, letraR5C7;
    @FXML
    private TilePane tPane;

    @FXML
    private AnchorPane anchor;

    @FXML
    private FlowPane fPane;

    private int row = 0;
    private int col = 0;
    private int check = 0;
    private String solution = "copilot";
    private String answer = "";

    private int dif = 7;

    private String ling = "en";


    @FXML
    public void initialize() {

        Label[][] labels = new Label[][]{
                {letraR1C1, letraR1C2, letraR1C3, letraR1C4, letraR1C5, letraR1C6, letraR1C7},
                {letraR2C1, letraR2C2, letraR2C3, letraR2C4, letraR2C5, letraR2C6, letraR2C7},
                {letraR3C1, letraR3C2, letraR3C3, letraR3C4, letraR3C5, letraR3C6, letraR3C7},
                {letraR4C1, letraR4C2, letraR4C3, letraR4C4, letraR4C5, letraR4C6, letraR4C7},
                {letraR5C1, letraR5C2, letraR5C3, letraR5C4, letraR5C5, letraR5C6, letraR5C7}
        };

        //2D array of keyboard buttons
        Button[][] buttons = new Button[][]{
                {Q, W, E, R, T, Y, U, I, O, P},
                {A, S, D, F, G, H, J, K, L, C2},
                {BACK_SPACE, Z, X, C, V, B, N, M, INSERT}
        };

        //set the solution and make it uppercase
        solution = selectWord(7).toUpperCase();
        //print the solution to console
        System.out.println(solution);

        //when clicking on login, open a new window
        bLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //load the new window
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
                    //set the new window
                    Scene scene = new Scene(root);
                    //get the stage
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    //set the scene
                    stage.setScene(scene);
                    //show the stage
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //when clicking on leaderboard, open a new window
        bLead.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //load the new window
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("leaderboard.fxml")));
                    //set the new window
                    Scene scene = new Scene(root);
                    //get the stage
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    //set the scene
                    stage.setScene(scene);
                    //show the stage
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        anchor.setOnKeyPressed(new EventHandler<KeyEvent>() {
            Label label = labels[row][col];

            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println(keyEvent.getCode().toString());
                //if the key pressed is INSERT (confirm word)
                if (keyEvent.getCode() == KeyCode.INSERT) {
                    if (col == dif-1 && !(label.getText().equals(""))) {
                        //check if word is in the dictionary
                        if (containsWord(answer) == 1) {
                            //check if the word is correct
                            if (answer.equals(solution)) {
                                for (int i = 0; i < dif; i++) {
                                    //animate each letter to rotate
                                    RotateTransition rotateTransition = new RotateTransition(Duration.millis(1500), labels[row][i]);
                                    rotateTransition.setByAngle(360);
                                    rotateTransition.setCycleCount(1);
                                    rotateTransition.setAutoReverse(false);
                                    rotateTransition.play();
                                    //change the color of the letters
                                    labels[row][i].setStyle("-fx-background-color: #608B55; -fx-text-fill: white; -fx-font-weight:bold;");
                                }
                                //update user score
                                currentUser.setScore(currentUser.getScore() + 1);
                                //reset the variables
                                answer = "";
                                check = 0;
                                col = 0;
                                row = 0;
                                label = labels[row][col];
                                //set new solution and make it uppercase
                                solution = selectWord(7).toUpperCase();
                                //print the solution to console
                                System.out.println(solution);
                                //popup stating you won
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("You Won!");
                                alert.setHeaderText(null);
                                alert.setContentText("Congratulations, you guessed the word!\nThe game will reset when you close this window.");
                                alert.showAndWait();
                                //reset the board
                                for (int i = 0; i < 5; i++) {
                                    for (int j = 0; j < dif; j++) {
                                        labels[i][j].setText("");
                                        labels[i][j].setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-weight:bold; -fx-border-color: lightgrey;");
                                    }
                                }
                                //reset the keyboard
                                for (int i = 0; i < 3; i++) {
                                    //if the row is not the last row (last row has 9 buttons, aka 8 indexes)
                                    if (i != 2) {
                                        for (int j = 0; j < 10; j++) {
                                            buttons[i][j].setStyle("-fx-background-color: lightgrey; -fx-text-fill: black; -fx-font-weight:bold;");
                                        }
                                    } else {
                                        for (int j = 0; j < 9; j++) {
                                            buttons[i][j].setStyle("-fx-background-color: lightgrey; -fx-text-fill: black; -fx-font-weight:bold;");
                                        }
                                    }
                                }
                            } else {
                                //check each letter and button, and change the color, reset if on final row
                                if (row == 4) {
                                    //reset the variables
                                    answer = "";
                                    check = 0;
                                    col = 0;
                                    row = 0;
                                    label = labels[row][col];
                                    //set new solution and make it uppercase
                                    solution = selectWord(7).toUpperCase();
                                    //print the solution to console
                                    System.out.println(solution);
                                    //popup stating you lost
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("You Lost...");
                                    alert.setHeaderText(null);
                                    alert.setContentText("You couldn't guess the correct word :" + solution + "\nThe game will reset when you close this window.");
                                    alert.showAndWait();
                                    //reset the board
                                    for (int i = 0; i < 5; i++) {
                                        for (int j = 0; j < dif; j++) {
                                            labels[i][j].setText("");
                                            labels[i][j].setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-weight:bold; -fx-border-color: lightgrey;");
                                        }
                                    }
                                    //reset the keyboard
                                    for (int i = 0; i < 3; i++) {
                                        //if the row is not the last row (last row has 9 buttons, aka 8 indexes)
                                        if (i != 2) {
                                            for (int j = 0; j < 10; j++) {
                                                buttons[i][j].setStyle("-fx-background-color: lightgrey; -fx-text-fill: black; -fx-font-weight:bold;");
                                            }
                                        } else {
                                            for (int j = 0; j < 9; j++) {
                                                buttons[i][j].setStyle("-fx-background-color: lightgrey; -fx-text-fill: black; -fx-font-weight:bold;");
                                            }
                                        }
                                    }

                                }
                                else {
                                    for (int i = 0; i < dif; i++) {
                                        //if the letter is in the word and in the correct position
                                        if (answer.charAt(i) == solution.charAt(i)) {
                                            labels[row][i].setStyle("-fx-background-color: #608B55; -fx-text-fill: white; -fx-font-weight:bold;");
                                            for (int j = 0; j < 3; j++) {
                                                //if the row is not the last row (last row has 9 buttons, aka 8 indexes)
                                                if (j != 2) {
                                                    for (int k = 0; k < 10; k++) {
                                                        if (Objects.equals(buttons[j][k].getId(), labels[row][i].getText())) {
                                                            buttons[j][k].setStyle("-fx-background-color: #608B55; -fx-text-fill: white; -fx-font-weight:bold;");
                                                        }
                                                    }
                                                } else {
                                                    for (int k = 0; k < 9; k++) {
                                                        if (Objects.equals(buttons[j][k].getId(), labels[row][i].getText())) {
                                                            buttons[j][k].setStyle("-fx-background-color: #608B55; -fx-text-fill: white; -fx-font-weight:bold;");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //if the letter is in the word but in the wrong position
                                        else if (solution.contains(Character.toString(answer.charAt(i)))) {
                                            labels[row][i].setStyle("-fx-background-color: #B29F4B; -fx-text-fill: white; -fx-font-weight:bold;");
                                            for (int j = 0; j < 3; j++) {
                                                //if the row is not the last row (last row has 9 buttons, aka 8 indexes)
                                                if (j != 2) {
                                                    for (int k = 0; k < 10; k++) {
                                                        if (Objects.equals(buttons[j][k].getId(), labels[row][i].getText())) {
                                                            buttons[j][k].setStyle("-fx-background-color: #B29F4B; -fx-text-fill: white; -fx-font-weight:bold;");
                                                        }
                                                    }
                                                } else {
                                                    for (int k = 0; k < 9; k++) {
                                                        if (Objects.equals(buttons[j][k].getId(), labels[row][i].getText())) {
                                                            buttons[j][k].setStyle("-fx-background-color: #B29F4B; -fx-text-fill: white; -fx-font-weight:bold;");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //if the letter is not in the word
                                        else {
                                            labels[row][i].setStyle("-fx-background-color: #3A3A3C; -fx-text-fill: white; -fx-font-weight:bold;");
                                            for (int j = 0; j < 3; j++) {
                                                //if the row is not the last row (last row has 9 buttons, aka 8 indexes)
                                                if (j != 2) {
                                                    for (int k = 0; k < 10; k++) {
                                                        if (Objects.equals(buttons[j][k].getId(), labels[row][i].getText())) {
                                                            buttons[j][k].setStyle("-fx-background-color: #3A3A3C; -fx-text-fill: white; -fx-font-weight:bold;");
                                                        }
                                                    }
                                                } else {
                                                    for (int k = 0; k < 9; k++) {
                                                        if (Objects.equals(buttons[j][k].getId(), labels[row][i].getText())) {
                                                            buttons[j][k].setStyle("-fx-background-color: #3A3A3C; -fx-text-fill: white; -fx-font-weight:bold;");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    //reset the variables and move to the next row
                                    answer = "";
                                    check = 0;
                                    col = 0;
                                    row++;
                                    label = labels[row][col];
                                }
                            }
                        }
                        else {
                            //animate each letter in the row to shake
                            for (int i = 0; i < dif; i++) {
                                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(50), labels[row][i]);
                                translateTransition.setFromX(0);
                                translateTransition.setToX(5);
                                translateTransition.setCycleCount(2);
                                translateTransition.setAutoReverse(true);
                                TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(50), labels[row][i]);
                                translateTransition2.setFromX(0);
                                translateTransition2.setToX(-5);
                                translateTransition2.setCycleCount(2);
                                translateTransition2.setAutoReverse(true);

                                SequentialTransition sequentialTransition = new SequentialTransition(translateTransition, translateTransition2);
                                sequentialTransition.play();

                            }
                        }
                    }
                    System.out.println("Row: " + row + " Col: " + col);
                }
                // If the key pressed is BACKSPACE (delete letter)
                else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    // Delete the last letter
                    if (col == (dif - 1) && !(label.getText().equals(""))) {
                        label.setText("");
                        answer = answer.substring(0, answer.length() - 1);
                        check = 0;
                    }
                    // Delete the letter and move to the previous letter
                    else if (col > 0) {
                        col--;
                        label = labels[row][col];
                        label.setText("");
                        answer = answer.substring(0, answer.length() - 1);
                    }
                    // Delete the first letter
                    else if (col == 0 && !(label.getText().equals(""))) {
                        label.setText("");
                        answer = answer.substring(0, answer.length() - 1);
                    }
                    System.out.println("Row: " + row + " Col: " + col);
                    System.out.println("Answer: " + answer);
                }
                // If the key pressed is a letter
                else if (keyEvent.getCode().isLetterKey() && col < dif) {
                    // Input the letter, unless it's the last letter, and it's already filled
                    if (check == 0) {
                        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), label);
                        scaleTransition.setToX(1.08);
                        scaleTransition.setToY(1.08);
                        scaleTransition.setCycleCount(2);
                        scaleTransition.setAutoReverse(true);
                        scaleTransition.play();
                        label.setText(keyEvent.getText().toUpperCase());
                        answer = answer + keyEvent.getText().toUpperCase();
                    }
                    // Move to the next letter, unless it's the last letter
                    if (col != (dif - 1)) {
                        col++;
                    }
                    // If it's the last letter, don't move to the next letter and set check to 1 (for first if statement)
                    else {
                        check++;
                    }
                    label = labels[row][col];

                    System.out.println("Row: " + row + " Col: " + col);
                    System.out.println("Answer: " + answer);
                }
            }
        });
    }

    @FXML
    private void handleDif(ActionEvent event) {
        Object source = event.getSource();
        if (source.equals(m3)) {// change the difficulty to 3
            dif = 3;
            letraR1C4.setVisible(false);letraR1C5.setVisible(false);letraR1C6.setVisible(false);letraR1C7.setVisible(false);
            letraR2C4.setVisible(false);letraR2C5.setVisible(false);letraR2C6.setVisible(false);letraR2C7.setVisible(false);
            letraR3C4.setVisible(false);letraR3C5.setVisible(false);letraR3C6.setVisible(false);letraR3C7.setVisible(false);
            letraR4C4.setVisible(false);letraR4C5.setVisible(false);letraR4C6.setVisible(false);letraR4C7.setVisible(false);
            letraR5C4.setVisible(false);letraR5C5.setVisible(false);letraR5C6.setVisible(false);letraR5C7.setVisible(false);
        } else if (source.equals(m4)) {// change the difficulty to 4
            dif = 4;
            letraR1C4.setVisible(true);letraR1C5.setVisible(false);letraR1C6.setVisible(false);letraR1C7.setVisible(false);
            letraR2C4.setVisible(true);letraR2C5.setVisible(false);letraR2C6.setVisible(false);letraR2C7.setVisible(false);
            letraR3C4.setVisible(true);letraR3C5.setVisible(false);letraR3C6.setVisible(false);letraR3C7.setVisible(false);
            letraR4C4.setVisible(true);letraR4C5.setVisible(false);letraR4C6.setVisible(false);letraR4C7.setVisible(false);
            letraR5C4.setVisible(true);letraR5C5.setVisible(false);letraR5C6.setVisible(false);letraR5C7.setVisible(false);
        } else if (source.equals(m5)) {// change the difficulty to 5
            dif = 5;
            letraR1C4.setVisible(true);letraR1C5.setVisible(true);letraR1C6.setVisible(false);letraR1C7.setVisible(false);
            letraR2C4.setVisible(true);letraR2C5.setVisible(true);letraR2C6.setVisible(false);letraR2C7.setVisible(false);
            letraR3C4.setVisible(true);letraR3C5.setVisible(true);letraR3C6.setVisible(false);letraR3C7.setVisible(false);
            letraR4C4.setVisible(true);letraR4C5.setVisible(true);letraR4C6.setVisible(false);letraR4C7.setVisible(false);
            letraR5C4.setVisible(true);letraR5C5.setVisible(true);letraR5C6.setVisible(false);letraR5C7.setVisible(false);
        } else if (source.equals(m6)) {// change the difficulty to 6
            dif = 6;
            letraR1C4.setVisible(true);letraR1C5.setVisible(true);letraR1C6.setVisible(true);letraR1C7.setVisible(false);
            letraR2C4.setVisible(true);letraR2C5.setVisible(true);letraR2C6.setVisible(true);letraR2C7.setVisible(false);
            letraR3C4.setVisible(true);letraR3C5.setVisible(true);letraR3C6.setVisible(true);letraR3C7.setVisible(false);
            letraR4C4.setVisible(true);letraR4C5.setVisible(true);letraR4C6.setVisible(true);letraR4C7.setVisible(false);
            letraR5C4.setVisible(true);letraR5C5.setVisible(true);letraR5C6.setVisible(true);letraR5C7.setVisible(false);
        } else if (source.equals(m7)) {// change the difficulty to 7
            dif = 7;
            letraR1C4.setVisible(true);letraR1C5.setVisible(true);letraR1C6.setVisible(true);letraR1C7.setVisible(true);
            letraR2C4.setVisible(true);letraR2C5.setVisible(true);letraR2C6.setVisible(true);letraR2C7.setVisible(true);
            letraR3C4.setVisible(true);letraR3C5.setVisible(true);letraR3C6.setVisible(true);letraR3C7.setVisible(true);
            letraR4C4.setVisible(true);letraR4C5.setVisible(true);letraR4C6.setVisible(true);letraR4C7.setVisible(true);
            letraR5C4.setVisible(true);letraR5C5.setVisible(true);letraR5C6.setVisible(true);letraR5C7.setVisible(true);
        }
        solution = selectWord(dif).toUpperCase();
        System.out.println("Difficulty: " + dif);
        System.out.println(solution);
    }

    @FXML
    private void handleLing(ActionEvent event) {
        Object source = event.getSource();
        if (source.equals(en)) {// change the language to english
            ling = "en";
        } else if (source.equals(pt)) {// change the language to portuguese
            ling = "pt";
        } else if (source.equals(fr)) {
            ling = "fr";
        }
        solution = selectWord(dif).toUpperCase();
        System.out.println("Language: " + ling);
        System.out.println(solution);
    }


    public String selectWord(int wordLength) {
        String DICTIONARY_FILE = "";
        if (Objects.equals(ling, "en")) {
            DICTIONARY_FILE = "en_GBf.txt";
        }
        else if (Objects.equals(ling, "fr")) {
            DICTIONARY_FILE = "frf.txt";
        }
        else if (Objects.equals(ling, "pt")) {
            DICTIONARY_FILE = "pt_PTf.txt";
        }

        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DICTIONARY_FILE))) {
            String word;
            while ((word = reader.readLine()) != null) {
                if (word.length() == wordLength) {
                    words.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (words.isEmpty()) {
            System.out.println("No words found with the specified length.");
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(words.size());
        return words.get(randomIndex);
    }

    public int containsWord (String answer) {
        String DICTIONARY_FILE = "";
        if (Objects.equals(ling, "en")) {
            DICTIONARY_FILE = "en_GBf.txt";
        }
        else if (Objects.equals(ling, "fr")) {
            DICTIONARY_FILE = "frf.txt";
        }
        else if (Objects.equals(ling, "pt")) {
            DICTIONARY_FILE = "pt_PTf.txt";
        }

        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DICTIONARY_FILE))) {
            String word;
            while ((word = reader.readLine()) != null) {
                    words.add(word.toUpperCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (words.contains(answer)) {
            return 1;
        }
        else {
            return 0;
        }
    }
}