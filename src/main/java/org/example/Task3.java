package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task3 {
    public static void main(String[] args) {
        int userId = 1;
        String apiUrl = "https://jsonplaceholder.typicode.com/users/" + userId + "/todos";

        try {
            String response = sendGetRequest(apiUrl);
            printOpenTasks(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sendGetRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public static void printOpenTasks(String response) {

        String trimmedResponse = response.substring(1, response.length() - 1);


        String[] tasks = trimmedResponse.split("\\},\\{");

        for (String taskStr : tasks) {

            if (taskStr.charAt(0) != '{') {
                taskStr = "{" + taskStr;
            }
            if (taskStr.charAt(taskStr.length() - 1) != '}') {
                taskStr = taskStr + "}";
            }


            String[] keyValuePairs = taskStr.split(",");
            int taskId = -1;
            String taskTitle = null;

            for (String pair : keyValuePairs) {
                String[] keyValue = pair.split(":");
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();


                if (key.equals("\"id\"")) {
                    taskId = Integer.parseInt(value);
                } else if (key.equals("\"title\"")) {
                    taskTitle = value.substring(1, value.length() - 1);
                }
            }


            boolean completed = taskStr.contains("\"completed\":false");
            if (!completed && taskId != -1 && taskTitle != null) {
                System.out.println(taskId + " " + taskTitle);
            }
        }
    }
}