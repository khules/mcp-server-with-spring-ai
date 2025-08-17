package com.portal.mcp_server.service.whatsapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks")
public class WhatsappController {
    private static final Logger logger = LoggerFactory.getLogger(WhatsappController.class);
    // This token is a secret value that you set in your Meta App Dashboard
    private static final String VERIFY_TOKEN = "luckysleven";

    /**
     * Handles the GET request for webhook verification.
     * Meta will send a GET request to this endpoint to verify the webhook URL.
     * The method checks the 'hub.mode' and 'hub.verify_token' and returns the
     * 'hub.challenge'
     * to confirm the endpoint is working.
     *
     * @param mode      The mode parameter from the verification request.
     * @param token     The verification token from the request.
     * @param challenge The challenge string to be echoed back.
     * @return The challenge string if verification is successful, otherwise an
     *         error message.
     */
    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {

        logger.info("Received verification request. Mode: {}, Token: {}, Challenge: {}", mode, token, challenge);

        // Check if the mode and token match what we expect
        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            logger.info("Webhook verification successful.");
            // Echo the challenge back to Meta
            return ResponseEntity.ok(challenge);
        } else {
            logger.warn("Webhook verification failed. Mode or token mismatch.");
            // Return 403 Forbidden for a failed verification
            return ResponseEntity.status(403).body("Verification token mismatch");
        }
    }

    @PostMapping
    public ResponseEntity<String> handleWebhookEvent(@RequestBody String payload) {
        logger.info("Received webhook event: {}", payload);
        // Process the webhook event payload
        return ResponseEntity.ok("Webhook event received");
    }
}
