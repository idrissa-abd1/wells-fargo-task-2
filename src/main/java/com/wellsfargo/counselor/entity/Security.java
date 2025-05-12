package com.wellsfargo.counselor.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Security held within a Client's Portfolio.
 * Maps to the 'Securities' table.
 */
@Entity
@Table(name = "Securities")
public class Security {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long securityId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    private LocalDate purchaseDate; // Use LocalDate for SQL DATE type

    // Use BigDecimal for precise financial values (matches DECIMAL SQL type)
    // Define precision and scale matching the DDL
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal purchasePrice;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // --- Relationships ---

    // Many Securities belong to One Portfolio
    @ManyToOne(fetch = FetchType.LAZY) // EAGER is default
    @JoinColumn(name = "PortfolioID", nullable = false) // FK column name from DDL
    private Portfolio portfolio;

    // --- Constructors ---

    protected Security() {
    }

    public Security(String name, String category, LocalDate purchaseDate, BigDecimal purchasePrice, BigDecimal quantity, Portfolio portfolio) {
        this.name = name;
        this.category = category;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.portfolio = portfolio;
    }

    // --- Getters and Setters ---

    public Long getSecurityId() {
        return securityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    // --- equals(), hashCode(), toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Security security = (Security) o;
        // Use ID if available, otherwise a combination of fields that make it unique within its context
        return securityId != null ? securityId.equals(security.securityId) : Objects.equals(portfolio, security.portfolio) && Objects.equals(name, security.name) && Objects.equals(purchaseDate, security.purchaseDate);
    }

    @Override
    public int hashCode() {
        return securityId != null ? Objects.hash(securityId) : Objects.hash(portfolio, name, purchaseDate);
    }

    @Override
    public String toString() {
        return "Security{" +
                "securityId=" + securityId +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", portfolioId=" + (portfolio != null ? portfolio.getPortfolioId() : "null") +
                '}';
    }
}

