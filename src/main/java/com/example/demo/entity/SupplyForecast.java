package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "supply_forecasts")
public class SupplyForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double availableSupplyMW;

    @Column(nullable = false)
    private Instant forecastStart;

    @Column(nullable = false)
    private Instant forecastEnd;

    private Instant generatedAt;

    @PrePersist
    protected void onCreate() {
        generatedAt = Instant.now();
    }

    public SupplyForecast() {}

    public SupplyForecast(Double availableSupplyMW, Instant forecastStart, Instant forecastEnd) {
        this.availableSupplyMW = availableSupplyMW;
        this.forecastStart = forecastStart;
        this.forecastEnd = forecastEnd;
    }

    public static SupplyForecastBuilder builder() {
        return new SupplyForecastBuilder();
    }

    public static class SupplyForecastBuilder {
        private Long id;
        private Double availableSupplyMW;
        private Instant forecastStart;
        private Instant forecastEnd;

        public SupplyForecastBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SupplyForecastBuilder availableSupplyMW(Double availableSupplyMW) {
            this.availableSupplyMW = availableSupplyMW;
            return this;
        }

        public SupplyForecastBuilder forecastStart(Instant forecastStart) {
            this.forecastStart = forecastStart;
            return this;
        }

        public SupplyForecastBuilder forecastEnd(Instant forecastEnd) {
            this.forecastEnd = forecastEnd;
            return this;
        }

        public SupplyForecast build() {
            SupplyForecast forecast = new SupplyForecast(availableSupplyMW, forecastStart, forecastEnd);
            forecast.setId(id);
            return forecast;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAvailableSupplyMW() { return availableSupplyMW; }
    public void setAvailableSupplyMW(Double availableSupplyMW) { this.availableSupplyMW = availableSupplyMW; }

    public Instant getForecastStart() { return forecastStart; }
    public void setForecastStart(Instant forecastStart) { this.forecastStart = forecastStart; }

    public Instant getForecastEnd() { return forecastEnd; }
    public void setForecastEnd(Instant forecastEnd) { this.forecastEnd = forecastEnd; }

    public Instant getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(Instant generatedAt) { this.generatedAt = generatedAt; }
}