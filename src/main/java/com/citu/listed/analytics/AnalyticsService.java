package com.citu.listed.analytics;

import com.citu.listed.analytics.dtos.SummaryResponse;

public interface AnalyticsService {
    SummaryResponse getSummary(Integer id);
}
