package com.example.report.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "feeding")
public class Feeding {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;
    
    @Column(name = "food_type", nullable = false, length = 100)
    private String foodType;
    
    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;
    
    @Column(name = "feeding_time", nullable = false)
    private LocalDateTime feedingTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keeper_id")
    private Staff keeper;

    public Feeding() {
    }

    public Feeding(Long id, Animal animal, String foodType, BigDecimal quantity, LocalDateTime feedingTime, Staff keeper) {
        this.id = id;
        this.animal = animal;
        this.foodType = foodType;
        this.quantity = quantity;
        this.feedingTime = feedingTime;
        this.keeper = keeper;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getFeedingTime() {
        return feedingTime;
    }

    public void setFeedingTime(LocalDateTime feedingTime) {
        this.feedingTime = feedingTime;
    }

    public Staff getKeeper() {
        return keeper;
    }

    public void setKeeper(Staff keeper) {
        this.keeper = keeper;
    }
}
