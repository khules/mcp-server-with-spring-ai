package com.portal.mcp_server.tools;

import com.portal.mcp_server.entity.SellerAccount;
import com.portal.mcp_server.repository.SellerAccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerAccountTools implements McpTool {
    private static final Logger logger = LoggerFactory.getLogger(SellerAccountTools.class);
    private final SellerAccountRepository sellerAccountRepository;

    public SellerAccountTools(SellerAccountRepository sellerAccountRepository) {
        this.sellerAccountRepository = sellerAccountRepository;
    }

    /**
     * Search all seller accounts by name
     * 
     * @param name Seller Account Name
     * @return List of Seller Accounts
     */
    @Tool(description = "Find all Seller Accounts by name")
    public Account getAccountByName(
            @ToolParam(description = "Seller Account Name") String name) {
        List<SellerAccount> accountList = sellerAccountRepository.findByName(name);
        StringBuilder result = new StringBuilder();
        for (SellerAccount account : accountList) {
            result.append(account.toString()).append("\n");
        }
        Account account = Account.builder()
                .name(result.toString())
                .build();
        logger.info("Found Seller Account by name: {}", account);
        return account;
    }

    /**
     * Search all seller accounts by owner
     * 
     * @param name Seller account owner
     * @return List of Seller Accounts
     */
    @Tool(description = "Find all Seller Accounts by owner")
    public SellerAccounts getAccountByOwner(
            @ToolParam(description = "Seller Account owner") String owner) {
        List<SellerAccount> accountList = sellerAccountRepository.findByOwner(owner);
        StringBuilder result = new StringBuilder();

        SellerAccounts sellerAccounts = SellerAccounts.builder()
                .accounts(new ArrayList<>())
                .build();
        for (SellerAccount account : accountList) {
            sellerAccounts.getAccounts().add(Account.builder()
                    .name(account.toString())
                    .build());
            logger.info("Account: {}", account.toString());
        }

        logger.info("Found Seller Account by owner {} accounts: {}", owner, sellerAccounts);
        return sellerAccounts;
    }
}
