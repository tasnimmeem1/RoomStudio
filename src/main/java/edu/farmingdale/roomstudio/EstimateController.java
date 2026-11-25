package edu.farmingdale.roomstudio;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EstimateController {
    private final RoomEstimator estimator;

    public EstimateController(RoomEstimator estimator) {
        this.estimator = estimator;
    }

    @GetMapping("/api/estimate")
    public ResponseEntity<String> estimate(@RequestParam MultiValueMap<String, String> parameters) {
        Map<String, String> form = new HashMap<>();
        parameters.forEach((name, values) -> form.put(name, values.getLast()));
        try {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(estimator.estimate(form));
        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body(error.getMessage());
        }
    }
}
