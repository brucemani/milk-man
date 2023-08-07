package com.milkman.api.model;

import com.milkman.api.util.converter.LocalDateTimeConverter;
import jakarta.persistence.*;
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
 * @Project: milky-man
 * @Date: 8/2/2023
 * @Time: 9:04 PM
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "VALIDATIONS")
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class ApplicationValidation implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "validation_pk_seq")
    @SequenceGenerator(name = "validation_pk_seq", sequenceName = "validation_pk_seq", allocationSize = 1, initialValue = 1000)
    private Long id;

    private String mobileNumber;

    private Long otp;

    private String token;

    @Convert(converter = LocalDateTimeConverter.class)
    private String expireTime;

    private Boolean isVerified;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date lastUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationValidation that = (ApplicationValidation) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
