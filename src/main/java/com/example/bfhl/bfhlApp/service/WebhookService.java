package com.example.bfhl.bfhlApp.service;

import com.example.bfhl.bfhlApp.dto.WebhookRequest;
import com.example.bfhl.bfhlApp.dto.WebhookResponse;
import com.example.bfhl.bfhlApp.utils.HttpUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void processWorkflow() {

        String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        String regNo = "0101IT233D04";
        String name = "Krishna Patidar";
        String email = "krishnapatidar4583@gmail.com";
        WebhookRequest requestPayload = new WebhookRequest(name, regNo, email);
        HttpEntity<WebhookRequest> requestEntity = new HttpEntity<>(requestPayload, HttpUtils.getJsonHeaders());

        ResponseEntity<WebhookResponse> response;
        try {
            response = restTemplate.postForEntity(generateUrl, requestEntity, WebhookResponse.class);
        } catch (Exception e) {
            System.err.println("Error while generating webhook: " + e.getMessage());
            return;
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            System.err.println("Failed to retrieve webhook info. Status: " + response.getStatusCode());
            return;
        }

        String webhookUrl = response.getBody().getWebhookUrl();
        String token = response.getBody().getAccessToken();

        System.out.println("Webhook: " + webhookUrl);
        System.out.println("Access Token: " + token);

        if (token == null || token.trim().isEmpty()) {
            System.err.println("Invalid or missing access token");
            return;
        }

        String finalQuery = "SELECT \n" +
                "    e1.EMP_ID,\n" +
                "    e1.FIRST_NAME,\n" +
                "    e1.LAST_NAME,\n" +
                "    d.DEPARTMENT_NAME,\n" +
                "    COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT\n" +
                "FROM \n" +
                "    EMPLOYEE e1\n" +
                "JOIN \n" +
                "    DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID\n" +
                "LEFT JOIN \n" +
                "    EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT \n" +
                "    AND e2.DOB > e1.DOB\n" +
                "GROUP BY \n" +
                "    e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME\n" +
                "ORDER BY \n" +
                "    e1.EMP_ID DESC;";

        HttpHeaders headers = HttpUtils.getJsonHeaders();
        headers.set("Authorization", token);

        Map<String, String> answerPayload = Map.of("finalQuery", finalQuery);
        HttpEntity<Map<String, String>> submission = new HttpEntity<>(answerPayload, headers);

        String submitUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

        try {
            ResponseEntity<String> result = restTemplate.postForEntity(submitUrl, submission, String.class);

            if (result.getStatusCode().is2xxSuccessful()) {
                System.out.println("Solution submitted successfully.");
                System.out.println("Response: " + result.getBody());
            } else {
                System.err.println("Failed to submit solution. Response: " + result.getStatusCode());
                System.err.println("Response body: " + result.getBody());
            }
        } catch (Exception e) {
            System.err.println("Error while submitting solution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
