package myfiles.GC.config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PayPalConfig {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public APIContext apiContext() {
        // Validate the mode
        if (!"sandbox".equalsIgnoreCase(mode) && !"live".equalsIgnoreCase(mode)) {
            throw new IllegalArgumentException("PayPal mode must be either 'sandbox' or 'live'");
        }

        // Set up PayPal API context
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);

        APIContext context = new APIContext(clientId, clientSecret, mode);
        context.setConfigurationMap(configMap);
        return context;
    }
}