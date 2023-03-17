package com.fmicodes.comm.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class OpenAIService {

    private static String openAIKey = "";
    private static String openAIHost = "https://api.openai.com/v1/chat/";


    public String sendVacationDescriptionToOpenAI(String vacationDescription) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(openAIHost + "completions"))
                .header("Authorization", "Bearer " + openAIKey)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\n" +
                        "    \"model\": \"gpt-3.5-turbo\",\n" +
                        "    \"messages\": [{\"role\": \"user\", \"content\": \"The perfect vacation for me includes: a luxurious SPA hotel in the mountains in a resort full of mineral water. Suggest me some destinations on the old continent\"}]\n" +
                        "  }"))
                .build();


        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(response.body());

        return "Yessir";
    }


}
