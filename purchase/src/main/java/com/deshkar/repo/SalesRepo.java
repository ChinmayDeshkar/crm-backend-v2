package com.deshkar.repo;

import com.deshkar.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesRepo extends JpaRepository<Sales, Long> {
    List<Sales> findByCreatedDate(LocalDateTime date);

    List<Sales> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT p FROM Sales p WHERE p.createdDate BETWEEN :start AND :end")
    List<Sales> findByDateRange(LocalDateTime start, LocalDateTime end);

    @Query("SELECT p FROM Sales p WHERE MONTH(p.createdDate) = MONTH(CURRENT_DATE) AND YEAR(p.createdDate) = YEAR(CURRENT_DATE)")
    List<Sales> findPurchasesThisMonth();

    // Revenues

    // 📅 Daily revenue (last 7 or 30 days)
    @Query("SELECT FUNCTION('DATE', c.createdDate) AS date, SUM(c.price), SUM(c.balance), COUNT(c) " +
            "FROM Sales c GROUP BY FUNCTION('DATE', c.createdDate) ORDER BY date DESC")
    List<Object[]> getRevenuePerDay();

    // 📆 Monthly revenue
    @Query("SELECT FUNCTION('MONTH', c.createdDate) AS month, FUNCTION('YEAR', c.createdDate) AS year, " +
            "SUM(c.price), SUM(c.balance), COUNT(c) " +
            "FROM Sales c GROUP BY year, month ORDER BY year DESC, month DESC")
    List<Object[]> getRevenuePerMonth();

    // 📅 Yearly revenue
    @Query("SELECT FUNCTION('YEAR', c.createdDate) AS year, SUM(c.price), SUM(c.balance), COUNT(c) " +
            "FROM Sales c GROUP BY year ORDER BY year DESC")
    List<Object[]> getRevenuePerYear();

    // 📆 Revenue in a custom range
    @Query("SELECT FUNCTION('DATE', c.createdDate) AS date, SUM(c.price), SUM(c.balance), COUNT(c) " +
            "FROM Sales c WHERE c.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', c.createdDate) ORDER BY date ASC")
    List<Object[]> getRevenueBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 💳 Transactions by payment method
    @Query("SELECT c.paymentMethod, COUNT(c) FROM Sales c GROUP BY c.paymentMethod")
    List<Object[]> getTransactionCountByPaymentMethod();

//    @Query("SELECT p FROM CustomerPurchases p " +
//            "WHERE p.orderStatus NOT IN ('COMPLETED', 'DELIVERED', 'CANCELLED') " +
//            "OR p.paymentStatus <> 'PAID'")
//    List<CustomerPurchases> findPendingTasks();

    @Query("SELECT c FROM Sales c " +
            "WHERE c.orderStatus IN :statuses " +
            "AND c.createdDate >= :fromDate")
    List<Sales> findRecentCompletedOrders(
            @Param("statuses") List<String> statuses,
            @Param("fromDate") LocalDateTime fromDate
    );

    List<Sales> findByCustomerId(long customerId);

    @Query("SELECT c FROM Sales c WHERE LOWER(c.customer.customerName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Sales> findByCustomerName(@Param("name") String name);

    @Query("SELECT c FROM Sales c WHERE c.createdDate >= :start AND c.createdDate < :end")
    List<Sales> findTodaysPurchases(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(c) FROM Sales c WHERE c.createdDate >= :start AND c.createdDate < :end")
    Long countCustomersVisitedToday(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT c FROM Sales c  WHERE c.orderStatus NOT IN ('DELIVERED', 'CANCELLED')")
    List<Sales> findPendingTasks();
}
