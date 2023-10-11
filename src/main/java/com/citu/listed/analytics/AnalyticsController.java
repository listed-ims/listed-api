package com.citu.listed.analytics;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Analytics")
@RestController
@CrossOrigin
@RequestMapping("/listed/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/summary")
    public ResponseEntity<Object> getSummary(@RequestParam Integer storeId) {
        return new ResponseEntity<>(analyticsService.getSummary(storeId), HttpStatus.OK);
    }
}
