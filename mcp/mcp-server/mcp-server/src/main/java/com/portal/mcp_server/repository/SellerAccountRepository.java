package com.portal.mcp_server.repository;

import com.portal.mcp_server.entity.SellerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerAccountRepository extends JpaRepository<SellerAccount, Integer> {

    /**
     * Search all seller accounts by name
     * @param name Seller Account Name
     * @return List of Seller Accounts
     */
    List<SellerAccount> findByName(String name);

    /**
     * Search all seller accounts by owner
     * @param owner Seller account owner
     * @return List of Seller Accounts
     */
    List<SellerAccount> findByOwner(String owner);
}
