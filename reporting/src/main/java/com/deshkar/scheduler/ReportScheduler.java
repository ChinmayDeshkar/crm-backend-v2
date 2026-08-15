package com.deshkar.scheduler;

import com.deshkar.service.SalesReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@AllArgsConstructor
public class ReportScheduler {
    private final SalesReportService reportService;

    // Schedule every day at 10 PM
    @Scheduled(cron = "0 4 * * * *")
    public void sendDailyReport() {
        log.info("Daily job for sending an sales report....");
        reportService.sendDailySalesReport();
    }
}
