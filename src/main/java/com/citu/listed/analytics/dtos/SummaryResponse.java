package com.citu.listed.analytics.dtos;

public record SummaryResponse(
        Long totalLowStock,
        Double totalNearExpiry,
        Double totalDailyRevenue,
        Double totalDailyItemsSold
) {}
