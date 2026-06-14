package ua.opnu.labwork2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator")
@Tag(name = "Стан сервера", description = "Технічні ендпоінти моніторингу")
public class ActuatorController {

    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok("GET /actuator/health OK");
    }

    @GetMapping("/metrics")
    public ResponseEntity<String> getMetrics() {
        return ResponseEntity.ok("GET /actuator/metrics OK");
    }

    @GetMapping("/prometheus")
    public ResponseEntity<String> getPrometheus() {
        return ResponseEntity.ok("GET /actuator/prometheus OK");
    }
}
