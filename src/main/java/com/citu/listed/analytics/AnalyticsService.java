package com.citu.listed.analytics;

import com.citu.listed.analytics.dtos.OutgoingValueResponse;
import com.citu.listed.analytics.dtos.RevenueResponse;
import com.citu.listed.analytics.dtos.SummaryResponse;
import com.citu.listed.analytics.enums.AnalyticsPeriodicity;

import java.util.List;

public interface AnalyticsService {
    SummaryResponse getSummary(Integer id);
    List<RevenueResponse> getRevenue(
            Integer id,
            AnalyticsPeriodicity periodicity,
            int pageNumber,
            int pageSize
    );
    List<OutgoingValueResponse> getOutgoingValue(
            Integer id,
            AnalyticsPeriodicity periodicity,
            int pageNumber,
            int pageSize
    );
}
