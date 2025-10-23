package com.example.report.repository;

import com.example.report.dto.AnimalHealthCheckDto;
import com.example.report.entity.*;
import com.example.report.repository.interfaces.HealthRepositoryQueryDSL;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class HealthRepositoryImpl implements HealthRepositoryQueryDSL {
    
    private final JPAQueryFactory queryFactory;

    public HealthRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<AnimalHealthCheckDto> findHealthChecks(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        QHealth health = QHealth.health;
        QAnimal animal = QAnimal.animal;
        QEnclosure enclosure = QEnclosure.enclosure;
        QStaff staff = QStaff.staff;

        LocalDateTime from = fromDateTime != null ? fromDateTime : LocalDateTime.now().minusDays(7);
        LocalDateTime to = toDateTime != null ? toDateTime : LocalDateTime.now();

        return queryFactory
            .select(Projections.constructor(
                AnimalHealthCheckDto.class,
                health.id,
                animal.name,
                animal.species,
                enclosure.name,
                Expressions.stringTemplate("CONCAT({0}, ' kg')", health.weight),
                health.status,
                health.activity,
                Expressions.stringTemplate("CONCAT({0}, ' (', {1}, ')')", 
                    staff.fullName, staff.code),
                health.checkTime
            ))
            .from(health)
            .innerJoin(health.animal, animal)
            .leftJoin(animal.enclosure, enclosure)
            .leftJoin(health.staff, staff)
            .where(health.checkTime.between(from, to))
            .orderBy(health.checkTime.desc())
            .fetch();
    }
}
