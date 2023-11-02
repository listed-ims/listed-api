package com.citu.listed.analytics;

import com.citu.listed.analytics.dtos.RevenueResponse;
import com.citu.listed.analytics.dtos.SummaryResponse;
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
        List<Map<String, Object>> result =
                periodicity == AnalyticsPeriodicity.WEEKLY
                        ? outgoingRepository.getWeeklyDateRange(id, pageable)
                        : outgoingRepository.getMonthlyDateRange(id, pageable);

        for (Map<String, Object> row : result) {
            RevenueResponse revenueResponse = new RevenueResponse();

            LocalDate startDate =
                    periodicity == AnalyticsPeriodicity.WEEKLY
                            ? getWeekStartDate(String.valueOf(row.get("year")), String.valueOf(row.get("week")))
                            : getYearMonthDate(String.valueOf(row.get("year")), String.valueOf(row.get("month"))).atDay(1);
            LocalDate endDate =
                    periodicity == AnalyticsPeriodicity.WEEKLY
                            ? getWeekStartDate(String.valueOf(row.get("year")), String.valueOf(row.get("week"))).plusDays(6)
                            : getYearMonthDate(String.valueOf(row.get("year")), String.valueOf(row.get("month"))).atEndOfMonth();

            revenueResponse.setStartDate(startDate);
            revenueResponse.setEndDate(endDate);
            revenueResponse.setRevenue(outgoingRepository.getTotalRevenueByStoreId(id, startDate, endDate));

            revenueResponses.add(revenueResponse);
        }

        return revenueResponses;
    }

    private LocalDate getWeekStartDate(String year, String week) {
        WeekFields weekFields = WeekFields.of(Locale.US);

        return LocalDate.of(Integer.parseInt(year), 1, 1)
                .with(weekFields.weekOfYear(), Integer.parseInt(week))
                .with(DayOfWeek.SUNDAY);
    }

    private YearMonth getYearMonthDate(String year, String month) {
        return YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
    }

}
