package com.virima.ProductManagement.Repository;

import com.virima.ProductManagement.Entity.User;
import com.virima.ProductManagement.Entity.WalletAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletAuditRepository extends JpaRepository<WalletAudit,Long> {

    List<WalletAudit> findByUserId(int user);
}
