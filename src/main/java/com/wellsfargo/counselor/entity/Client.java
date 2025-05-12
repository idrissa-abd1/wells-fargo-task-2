package com.wellsfargo.counselor.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Client associated with a Financial Advisor.
 * Maps to the 'Clients' table.
 */
@Entity
@Table(name = "Clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String contactDetails;

    @Column(columnDefinition = "TEXT") // Map to TEXT type in SQL
    private String address;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // --- Relationships ---

    // Many Clients belong to One Advisor
    // FetchType.LAZY ensures Advisor is only loaded when needed
    // @JoinColumn specifies the foreign key column in the 'Clients' table.
    @ManyToOne(fetch = FetchType.LAZY) // EAGER is default for ManyToOne
    @JoinColumn(name = "AdvisorID", nullable = false) // FK column name, matches DDL.
    private Advisor advisor;

    // One Client has One Portfolio
    // mappedBy="client" indicates the 'client' field in the Portfolio entity owns the relationship.
    // CascadeType.ALL ensures portfolio is managed along with the client.
    // orphanRemoval=true ensures portfolio is deleted if the client is deleted or the link is broken.
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Portfolio portfolio;

    // --- Constructors ---

    protected Client() {
    }

    // Constructor for creating new Clients (without ID, timestamps, portfolio)
    public Client(String name, String contactDetails, String address, Advisor advisor) {
        this.name = name;
        this.contactDetails = contactDetails;
        this.address = address;
        this.advisor = advisor;
    }

    // --- Getters and Setters ---

    public Long getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        // Ensure bidirectional link is maintained
        if (portfolio != null) {
            portfolio.setClientInternal(this); // Use internal setter if needed to avoid loops
        }
        this.portfolio = portfolio;
    }

    // --- equals(), hashCode(), toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return clientId != null ? clientId.equals(client.clientId) : Objects.equals(name, client.name) && Objects.equals(advisor, client.advisor);
    }

    @Override
    public int hashCode() {
        return clientId != null ? Objects.hash(clientId) : Objects.hash(name, advisor);
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", advisorId=" + (advisor != null ? advisor.getAdvisorId() : "null") +
                '}';
    }
}
