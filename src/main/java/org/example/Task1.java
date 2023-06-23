package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task1 {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        Task1 apiClient = new Task1();
        
        apiClient.createPost();
        apiClient.updatePost();
        apiClient.deletePost();
        apiClient.getAllUsers();
        apiClient.getUserById(1);
        apiClient.getUserByUsername("Bret");
    }

    public void createPost() {
        String url = BASE_URL + "/users";
        String jsonInput = "{\"name\":\"John Doe\",\"username\":\"johndoe\",\"email\":\"johndoe@example.com\"}";

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonInput.getBytes());
            outputStream.flush();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                System.out.println("New post created:\n" + response);
            } else {
                System.out.println("Failed to create post. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePost() {
        String url = BASE_URL + "/users/1";
        String jsonInput = "{\"name\":\"Updated Name\",\"username\":\"updatedusername\",\"email\":\"updatedemail@example.com\"}";

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonInput.getBytes());
            outputStream.flush();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                System.out.println("Post updated:\n" + response);
            } else {
                System.out.println("Failed to update post. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePost() {
        String url = BASE_URL + "/users/1";

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("DELETE");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Post deleted successfully");
            } else {
                System.out.println("Failed to delete post. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllUsers() {
        String url = BASE_URL + "/users";

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("All users:\n" + response.toString());
            } else {
                System.out.println("Failed to retrieve users. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserById(int userId) {
        String url = BASE_URL + "/users/" + userId;

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("User with ID " + userId + ":\n" + response.toString());
            } else {
                System.out.println("Failed to retrieve user. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserByUsername(String username) {
        String url = BASE_URL + "/users?username=" + username;

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("User with username " + username + ":\n" + response.toString());
            } else {
                System.out.println("Failed to retrieve user. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAndSaveCommentsForLastPostOfUser(int i) {
    }
}