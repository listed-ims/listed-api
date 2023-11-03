package com.citu.listed.analytics;

import com.citu.listed.analytics.enums.AnalyticsPeriodicity;
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

    @GetMapping("/revenue")
    public ResponseEntity<Object> getRevenue(
            @RequestParam Integer storeId,
            @RequestParam AnalyticsPeriodicity periodicity,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "6") int pageSize
    ) {
        return new ResponseEntity<>(analyticsService.getRevenue(storeId, periodicity, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/outgoing-value")
    public ResponseEntity<Object> getOugoingValue(
            @RequestParam Integer storeId,
            @RequestParam AnalyticsPeriodicity periodicity,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "1") int pageSize
    ) {
        return new ResponseEntity<>(analyticsService.getOutgoingValue(storeId, periodicity, pageNumber, pageSize), HttpStatus.OK);
    }
}
