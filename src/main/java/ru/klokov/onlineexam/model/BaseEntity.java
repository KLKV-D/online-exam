package ru.klokov.onlineexam.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {
    @CreatedDate
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDate createdDate;
    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}
