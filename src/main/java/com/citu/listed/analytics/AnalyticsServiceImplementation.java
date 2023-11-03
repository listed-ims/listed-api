package com.citu.listed.analytics;

import com.citu.listed.analytics.dtos.OutgoingValueResponse;
import com.citu.listed.analytics.dtos.RevenueResponse;
import com.citu.listed.analytics.dtos.SummaryResponse;
import com.citu.listed.analytics.dtos.TopProductResponse;
import com.citu.listed.analytics.enums.AnalyticsPeriodicity;
import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.outgoing.OutgoingRepository;
import com.citu.listed.product.ProductRepository;
import com.citu.listed.shared.exception.NotFoundException;
import com.citu.listed.store.Store;
import com.citu.listed.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImplementation implements AnalyticsService{

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final IncomingRepository incomingRepository;
    private final OutgoingRepository outgoingRepository;

    @Override
    public SummaryResponse getSummary(Integer id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        return new SummaryResponse(
                productRepository.countLowStockProductsByStoreId(store.getId()),
                incomingRepository.getTotalNearExpiryItemsByStoreId(store.getId(), LocalDate.now().plusDays(14)),
                outgoingRepository.getTotalRevenueByStoreId(store.getId(), LocalDate.now(), LocalDate.now()),
                outgoingRepository.getTotalItemsSoldByStoreId(store.getId(), LocalDate.now(), LocalDate.now())
        );
    }

    @Override
    public List<RevenueResponse> getRevenue(
            Integer id,
            AnalyticsPeriodicity periodicity,
            int pageNumber,
            int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        List<RevenueResponse> revenueResponses = new ArrayList<>();
        List<Map<String, Object>> dateRanges =
                periodicity == AnalyticsPeriodicity.WEEKLY
                        ? outgoingRepository.getWeeklyDateRange(id, pageable)
                        : outgoingRepository.getMonthlyDateRange(id, pageable);

        for (Map<String, Object> dateRange : dateRanges) {
            RevenueResponse revenueResponse = new RevenueResponse();

            LocalDate startDate = getStartDate(
                    periodicity,
                    String.valueOf(dateRange.get("year")),
                    String.valueOf(dateRange.get(periodicity == AnalyticsPeriodicity.WEEKLY ? "week" : "month"))
            );
            LocalDate endDate = getEndDate(
                    periodicity,
                    String.valueOf(dateRange.get("year")),
                    String.valueOf(dateRange.get(periodicity == AnalyticsPeriodicity.WEEKLY ? "week" : "month"))
            );

            revenueResponse.setStartDate(startDate);
            revenueResponse.setEndDate(endDate);
            revenueResponse.setRevenue(outgoingRepository.getTotalRevenueByStoreId(id, startDate, endDate));

            revenueResponses.add(revenueResponse);
        }

        return revenueResponses;
    }

    @Override
    public List<TopProductResponse> getTopProducts(
            Integer id,
            AnalyticsPeriodicity periodicity,
            int pageNumber,
            int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<TopProductResponse> topProductResponses = new ArrayList<>();
        List<Map<String, Object>> dateRanges =
                periodicity == AnalyticsPeriodicity.WEEKLY
                        ? outgoingRepository.getWeeklyDateRange(id, pageable)
                        : outgoingRepository.getMonthlyDateRange(id, pageable);

        for (Map<String, Object> dateRange : dateRanges) {
            TopProductResponse topProductResponse = new TopProductResponse();

            LocalDate startDate = getStartDate(
                    periodicity,
                    String.valueOf(dateRange.get("year")),
                    String.valueOf(dateRange.get(periodicity == AnalyticsPeriodicity.WEEKLY ? "week" : "month"))
            );
            LocalDate endDate = getEndDate(
                    periodicity,
                    String.valueOf(dateRange.get("year")),
                    String.valueOf(dateRange.get(periodicity == AnalyticsPeriodicity.WEEKLY ? "week" : "month"))
            );

            topProductResponse.setStartDate(startDate);
            topProductResponse.setEndDate(endDate);
            topProductResponse.setProducts(outgoingRepository.getTopSoldProductsByStoreId(id, startDate, endDate));

            topProductResponses.add(topProductResponse);
        }
        return topProductResponses;
    }

    @Override
    public List<OutgoingValueResponse> getOutgoingValue(
            Integer id,
            AnalyticsPeriodicity periodicity,
            int pageNumber,
            int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<OutgoingValueResponse> outgoingValueResponses = new ArrayList<>();
        List<Map<String, Object>> dateRanges =
                periodicity == AnalyticsPeriodicity.WEEKLY
                        ? outgoingRepository.getWeeklyDateRange(id, pageable)
                        : outgoingRepository.getMonthlyDateRange(id, pageable);

        for (Map<String, Object> dateRange : dateRanges) {
            OutgoingValueResponse outgoingValueResponse = new OutgoingValueResponse();

            LocalDate startDate = getStartDate(
                    periodicity,
                    String.valueOf(dateRange.get("year")),
                    String.valueOf(dateRange.get(periodicity == AnalyticsPeriodicity.WEEKLY ? "week" : "month"))
            );
            LocalDate endDate = getEndDate(
                    periodicity,
                    String.valueOf(dateRange.get("year")),
                    String.valueOf(dateRange.get(periodicity == AnalyticsPeriodicity.WEEKLY ? "week" : "month"))
            );

            outgoingValueResponse.setStartDate(startDate);
            outgoingValueResponse.setEndDate(endDate);
            outgoingValueResponse.setCategories(outgoingRepository.getTotalCategoryValueByStoreId(id, startDate, endDate));

            outgoingValueResponses.add(outgoingValueResponse);
        }
        return outgoingValueResponses;
    }

    private LocalDate getStartDate(AnalyticsPeriodicity periodicity, String year, String weekMonth) {
        WeekFields weekFields = WeekFields.of(Locale.US);

        if(periodicity == AnalyticsPeriodicity.WEEKLY)
            return LocalDate.of(Integer.parseInt(year), 1, 1)
                    .with(weekFields.weekOfYear(), Integer.parseInt(weekMonth))
                    .with(DayOfWeek.SUNDAY);
        else
            return YearMonth.of(Integer.parseInt(year), Integer.parseInt(weekMonth))
                    .atDay(1);
    }

    private LocalDate getEndDate(AnalyticsPeriodicity periodicity, String year, String weekMonth) {
        WeekFields weekFields = WeekFields.of(Locale.US);

        if(periodicity == AnalyticsPeriodicity.WEEKLY)
            return LocalDate.of(Integer.parseInt(year), 1, 1)
                    .with(weekFields.weekOfYear(), Integer.parseInt(weekMonth))
                    .with(DayOfWeek.SUNDAY)
                    .plusDays(6);
        else
            return YearMonth.of(Integer.parseInt(year), Integer.parseInt(weekMonth))
                    .atEndOfMonth();
    }
}
