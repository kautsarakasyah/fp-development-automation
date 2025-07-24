package finalproject.gopaynew.controller;

import finalproject.gopaynew.dto.PaymentRequest;
import finalproject.gopaynew.dto.GenericResponse;
import finalproject.gopaynew.service.GopayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gopay")
public class GopayController {

    @Autowired
    private GopayService gopayService;

    // Endpoint ini akan dipanggil oleh layanan BNI.
    // Keamanan (validasi token) ditangani secara otomatis oleh JwtAuthFilter.
    @PostMapping("/pay")
    public ResponseEntity<GenericResponse> processPayment(@RequestBody PaymentRequest request) {
        GenericResponse response = gopayService.processPayment(request);
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Endpoint ini tidak memerlukan otentikasi karena sudah diizinkan di SecurityConfig
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("GoPay Spring Boot Service is running!");
    }
}
