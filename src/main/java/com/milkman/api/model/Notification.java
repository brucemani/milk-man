package com.milkman.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static jakarta.persistence.GenerationType.SEQUENCE;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/28/2023
 * @Time: 10:13 PM
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "NOTIFICATION")
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Notification implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    @NotNull(message = "CustomerId shouldn't null!")
    private Long customerId;
    @NotNull(message = "Subject shouldn't null!")
    @NotBlank(message = "Subject shouldn't blank!")
    @NotEmpty(message = "Subject shouldn't empty!")
    private String subject;
    @NotNull(message = "Message shouldn't null!")
    @NotBlank(message = "Message shouldn't blank!")
    @NotEmpty(message = "Message shouldn't empty!")
    private String message;
    private Boolean isRead;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Notification that = (Notification) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
