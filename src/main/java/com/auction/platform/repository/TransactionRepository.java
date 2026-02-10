package com.auction.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.auction.platform.entity.Transaction;
import com.auction.platform.entity.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findByUserOrderByTimeDesc(User user);
	
	@Query("""
			SELECT MONTH(t.time), SUM(t.amount)
			FROM Transaction t
			WHERE t.user.id = :sellerId
			GROUP BY MONTH(t.time)
			ORDER BY MONTH(t.time)
			""")
			List<Object[]> getMonthlyEarnings(long sellerId);
			
			
			//Calculates total platform earnings from all credit transactions and safely returns 0 if none exist.
			@Query("""
					   SELECT COALESCE(SUM(t.amount), 0)
					   FROM Transaction t
					   WHERE t.type = 'CREDIT'
					""")
					double getPlatformRevenue();
			
			

			@Query("""
				    SELECT MONTH(t.time), SUM(t.amount)
				    FROM Transaction t
				    WHERE t.type = 'CREDIT'
				    GROUP BY MONTH(t.time)
				    ORDER BY MONTH(t.time)
				""")
				List<Object[]> getMonthlyPlatformRevenue();

	
			

	
}
