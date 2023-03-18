package com.fmicodes.comm.services;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class AnalyzerService {

    @Value("${analyzer.host}")
    private static String analyzerHost;


    public String analyzeMessage(String message) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String analyzedMessageResponse = null;
        try {
            Response response = client.prepare("POST",  "https://" + analyzerHost + "/api/v1/analyzer")
                    .setBody(message)
                    .execute()
                    .get();

            analyzedMessageResponse = response.getResponseBody();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return analyzedMessageResponse;
    }
}
