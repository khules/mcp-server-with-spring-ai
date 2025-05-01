package com.portal.mcp_server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seller_account")
public class SellerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "owner")
    private String owner;

    @Column(name = "is_test")
    private Boolean isTest;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private Integer status;

    @Override
    public String toString() {
        return "SellerAccount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", isTest=" + isTest +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }
}
