package com.example.report.repository;

import com.example.report.dto.DailyFeedingDto;
import com.example.report.entity.*;
import com.example.report.repository.interfaces.FeedingRepositoryQueryDSL;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class FeedingRepositoryImpl implements FeedingRepositoryQueryDSL {
    
    private final JPAQueryFactory queryFactory;

    public FeedingRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<DailyFeedingDto> findDailyFeeding(LocalDate targetDate) {
        QFeeding feeding = QFeeding.feeding;
        QAnimal animal = QAnimal.animal;
        QEnclosure enclosure = QEnclosure.enclosure;
        QStaff staff = QStaff.staff;

        LocalDate date = targetDate != null ? targetDate : LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        return queryFactory
            .select(Projections.constructor(
                DailyFeedingDto.class,
                feeding.id,
                animal.name,
                animal.species,
                enclosure.name,
                feeding.foodType,
                feeding.quantity.doubleValue(),
                Expressions.stringTemplate("CONCAT({0}, ' (', {1}, ')')", 
                    staff.fullName, staff.code),
                feeding.feedingTime
            ))
            .from(feeding)
            .innerJoin(feeding.animal, animal)
            .leftJoin(animal.enclosure, enclosure)
            .leftJoin(feeding.keeper, staff)
            .where(feeding.feedingTime.between(startOfDay, endOfDay))
            .orderBy(feeding.feedingTime.desc())
            .fetch();
    }
}
