
package finalproject.gopaynew.service;

import finalproject.gopaynew.dto.GenericResponse;
import finalproject.gopaynew.dto.PaymentRequest;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class GopayService {

    private static final Logger LOGGER = Logger.getLogger(GopayService.class.getName());

    // This acts as our "dummy database" of registered GoPay accounts.
    // In a real application, this would be a database query.
    private static final Set<String> registeredGopayNumbers = Set.of(
        "898081234567", // Valid GoPay number
        "898089876543", // Another valid GoPay number
        "898081112223"  // A third valid GoPay number
    );

    public GenericResponse processPayment(PaymentRequest request) {
        // Business logic is now here.
        // Authentication has already been handled by JwtAuthFilter.

        // We now check if the number exists in our "dummy database".
        boolean isValidGopayNumber = request.getPhoneNumber() != null && registeredGopayNumbers.contains(request.getPhoneNumber());

        LOGGER.info("Processing payment for number: " + request.getPhoneNumber() + ". Is number registered in GoPay? " + isValidGopayNumber);

        if (isValidGopayNumber) {
            // Success
            return new GenericResponse("SUCCESS", null);
        } else {
            // Failure
            return new GenericResponse("FAILED", "The provided phone number is not a registered GoPay number.");
        }
    }
}
