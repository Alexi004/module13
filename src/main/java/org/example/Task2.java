package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task2 {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        Task2 apiClient = new Task2();
        apiClient.getAndSaveCommentsForLastPostOfUser(1);
    }

    public void getAndSaveCommentsForLastPostOfUser(int userId) {
        try {

            String userPostsUrl = BASE_URL + "/users/" + userId + "/posts";
            HttpURLConnection postsConnection = (HttpURLConnection) new URL(userPostsUrl).openConnection();
            postsConnection.setRequestMethod("GET");

            int postsResponseCode = postsConnection.getResponseCode();
            if (postsResponseCode == HttpURLConnection.HTTP_OK) {
                InputStreamReader postsReader = new InputStreamReader(postsConnection.getInputStream());

                String lastPostId = "0";

                while (true) {
                    int data = postsReader.read();
                    if (data == -1)
                        break;
                    char current = (char) data;
                    if (current == '{') {
                        String postId = getNextValue(postsReader, "id");
                        if (postId != null && Integer.parseInt(postId) > Integer.parseInt(lastPostId)) {
                            lastPostId = postId;
                        }
                    }
                }
                postsReader.close();


                String postCommentsUrl = BASE_URL + "/posts/" + lastPostId + "/comments";
                HttpURLConnection commentsConnection = (HttpURLConnection) new URL(postCommentsUrl).openConnection();
                commentsConnection.setRequestMethod("GET");

                int commentsResponseCode = commentsConnection.getResponseCode();
                if (commentsResponseCode == HttpURLConnection.HTTP_OK) {
                    InputStreamReader commentsReader = new InputStreamReader(commentsConnection.getInputStream());


                    String fileName = "user-" + userId + "-post-" + lastPostId + "-comments.json";
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                    char[] buffer = new char[4096];
                    int bytesRead;
                    while ((bytesRead = commentsReader.read(buffer)) != -1) {
                        writer.write(buffer, 0, bytesRead);
                    }
                    writer.close();

                    System.out.println("Comments saved to file: " + fileName);
                } else {
                    System.out.println("Failed to retrieve comments. Response code: " + commentsResponseCode);
                }
            } else {
                System.out.println("Failed to retrieve user posts. Response code: " + postsResponseCode);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while retrieving and saving comments: " + e.getMessage());
        }
    }


    private String getNextValue(InputStreamReader reader, String property) throws IOException {
        StringBuilder value = new StringBuilder();
        boolean readingValue = false;
        boolean isInString = false;
        int curlyBraceCount = 0;
        int squareBracketCount = 0;

        while (true) {
            int data = reader.read();
            if (data == -1)
                return null;

            char current = (char) data;
            if (current == '"') {
                isInString = !isInString;
            } else if (!isInString) {
                if (current == '{') {
                    curlyBraceCount++;
                } else if (current == '}') {
                    curlyBraceCount--;
                    if (curlyBraceCount == 0) {
                        return null;
                    }
                } else if (current == '[') {
                    squareBracketCount++;
                } else if (current == ']') {
                    squareBracketCount--;
                } else if (current == ':' && !readingValue) {
                    String currentProperty = value.toString().trim();
                    if (currentProperty.equals("\"" + property + "\"")) {
                        readingValue = true;
                        value = new StringBuilder();
                    }
                } else if (current == ',' && readingValue && curlyBraceCount == 1 && squareBracketCount == 0) {
                    return value.toString().trim();
                }
            }

            if (readingValue) {
                value.append(current);
            }
        }
    }
}