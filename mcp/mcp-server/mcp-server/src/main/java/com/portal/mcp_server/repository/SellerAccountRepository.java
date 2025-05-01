package com.portal.mcp_server.repository;

import com.portal.mcp_server.entity.SellerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerAccountRepository extends JpaRepository<SellerAccount, Integer> {
    List<SellerAccount> findByName(String name);
    List<SellerAccount> findByOwner(String owner);
}
