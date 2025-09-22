package com.example.bfhl.bfhlApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
public class WebhookResponse {

    @JsonProperty("webhook")
    private String webhookUrl;

    @JsonProperty("accessToken")
    private String accessToken;

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public WebhookResponse(String webhookUrl, String accessToken) {
        this.webhookUrl = webhookUrl;
        this.accessToken = accessToken;
    }

    public WebhookResponse() {
    }
}