
package finalproject.gopaynew.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentRequest {
    @JsonProperty("user_id")
    private int userId;

    private int amount;

    private String description;
    
    @JsonProperty("phone_number")
    private String phoneNumber;
}
