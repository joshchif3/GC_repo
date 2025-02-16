package myfiles.GC.controller;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "http://localhost:5173")
public class CheckoutController {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private APIContext apiContext;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> data) {
        double amount = Double.parseDouble(data.get("amount").toString()); // Amount in USD
        String currency = "USD"; // Change to your preferred currency

        try {
            // Create a PayPal payment
            Payment payment = createPayment(amount, currency);
            logger.info("PayPal payment created: " + payment.getId());

            // Return the approval URL to the frontend
            String approvalUrl = payment.getLinks().stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No approval URL found"))
                    .getHref();

            Map<String, String> response = new HashMap<>();
            response.put("approvalUrl", approvalUrl);
            return ResponseEntity.ok(response);
        } catch (PayPalRESTException e) {
            logger.error("PayPal API error: ", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Payment failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected error: ", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private Payment createPayment(double amount, String currency) throws PayPalRESTException {
        // Set up payment details
        Amount paymentAmount = new Amount();
        paymentAmount.setCurrency(currency);
        paymentAmount.setTotal(String.format("%.2f", amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(paymentAmount);
        transaction.setDescription("Payment for order");

        // Set up payer
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Create payment
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(List.of(transaction));

        // Set redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:5173/cancel");
        redirectUrls.setReturnUrl("http://localhost:5173/success");
        payment.setRedirectUrls(redirectUrls);

        // Execute payment
        return payment.create(apiContext);
    }
}