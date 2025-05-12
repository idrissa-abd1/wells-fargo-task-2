package com.wellsfargo.counselor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Client's Portfolio containing Securities.
 * Maps to the 'Portfolios' table.
 */
@Entity
@Table(name = "Portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolioId;

    // Renamed from creationDate/lastUpdatedDate in DDL to match entity naming conventions
    // Using LocalDateTime as per DDL DATETIME type
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate; // Matches DDL column name

    @Column(nullable = false)
    private LocalDateTime lastUpdatedDate; // Matches DDL column name

    // --- Relationships ---

    // One Portfolio belongs to One Client
    // This side owns the relationship (defines the foreign key column).
    // The @JoinColumn name 'ClientID' must match the FK column in the 'Portfolios' table.
    // unique=true enforces the OneToOne relationship at the DB level.
    @OneToOne(fetch = FetchType.LAZY) // EAGER is default for OneToOne
    @JoinColumn(name = "ClientID", unique = true, nullable = false) // FK column, must be unique and not null
    private Client client;

    // One Portfolio has Many Securities
    // mappedBy="portfolio" indicates the 'portfolio' field in Security entity owns the relationship.
    // CascadeType.ALL ensures securities are managed with the portfolio.
    // orphanRemoval=true ensures securities are deleted if removed from this list or portfolio is deleted.
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Security> securities;

    // --- Constructors ---

    protected Portfolio() {
    }

    // Constructor for creating new Portfolios (without ID, timestamps, securities)
    public Portfolio(Client client) {
        this.client = client;
        this.creationDate = LocalDateTime.now(); // Set creation date on instantiation
        this.lastUpdatedDate = LocalDateTime.now(); // Set initial update date
    }

    // --- Getters and Setters ---

    public Long getPortfolioId() {
        return portfolioId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    // Setter for lastUpdatedDate might be needed if manually updating
    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Client getClient() {
        return client;
    }

    // Internal setter to avoid potential infinite loops when setting bidirectional relationship
    protected void setClientInternal(Client client) {
        this.client = client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public List<Security> getSecurities() {
        return securities;
    }

    public void setSecurities(List<Security> securities) {
        this.securities = securities;
    }

    // --- equals(), hashCode(), toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        // A portfolio is uniquely identified by its ID or the client it belongs to
        return portfolioId != null ? portfolioId.equals(portfolio.portfolioId) : Objects.equals(client, portfolio.client);
    }

    @Override
    public int hashCode() {
        return portfolioId != null ? Objects.hash(portfolioId) : Objects.hash(client);
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "portfolioId=" + portfolioId +
                ", clientId=" + (client != null ? client.getClientId() : "null") +
                '}';
    }
}
