package com.example.wordle;

import java.io.*;

public class User {
    private String username;
    private String password;
    private int score;

    public User(String username, String password, int score) {
        this.username = username;
        this.password = password;
        this.score = score;

        if (!isDuplicateUsername(username)) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt", true))) {
                writer.println(username + "," + password + "," + score);
            } catch (IOException e) {
                System.out.println("Error registering user");
                e.printStackTrace();
            }
        } else {
            System.out.println("Username already exists. User registration failed.");
        }
    }

    public User() {
        this("", "", 0);
    }

    public static User getUser(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return new User(parts[0], parts[1], Integer.parseInt(parts[2]));
                }
            }
            return null;
        } catch (IOException e) {
            System.out.println("Error getting user");
            e.printStackTrace();
            return null;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        int score = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    score = Integer.parseInt(parts[2]);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error getting user score");
            e.printStackTrace();
        }

        return score;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(int score) {
        //change the score in the file
        this.score = score;
        updateScoreInFile(username, score);
    }

    public String toString() {
        return "User: " + username + ", " + password + ", " + score;
    }

    public static int checkUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return 0; // Login successful
                } else if (parts[0].equals(username) && !parts[1].equals(password)) {
                    return 1; // Incorrect password
                }
            }
            return 2; // User not found
        } catch (IOException e) {
            System.out.println("Error checking login");
            e.printStackTrace();
            return 3; // Error logging in
        }
    }

    private void updateScoreInFile(String username, int score) {
        File file = new File("users.txt");
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "rw");
            long position = -1;

            String line;
            while ((line = raf.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    position = raf.getFilePointer() - line.getBytes().length - System.lineSeparator().getBytes().length;
                    break;
                }
            }

            if (position >= 0) {
                raf.seek(position);
                raf.writeBytes(username + "," + getPassword() + "," + score);
            }
        } catch (IOException e) {
            System.out.println("Error updating user score");
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing RandomAccessFile");
                e.printStackTrace();
            }
        }

        System.out.println("User score updated successfully");
    }

    public static boolean isDuplicateUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return true; // Username already exists
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking duplicate username");
            e.printStackTrace();
        }
        return false; // Username is unique
    }
}
