package com.nua.core.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(transactionManager = "tokenTransactionManager")
public interface TokenRepository extends JpaRepository<Token, Long> {
}
