package com.kiranaregister.repositories;

import com.kiranaregister.models.Transaction;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  @Query(
      value =
          "select * from transaction where created_at >= :startTimestamp and created_at< :endTimestamp and is_deleted = :isDeleted and account_id = :accountId ",
      nativeQuery = true)
  List<Transaction> findByCreatedAtAndIsDeleted(
      @Param(value = "startTimestamp") Timestamp startTimestamp,
      @Param(value = "endTimestamp") Timestamp endTimestamp,
      @Param(value = "isDeleted") Boolean isDeleted,
      @Param(value = "accountId") Long accountId);
}
