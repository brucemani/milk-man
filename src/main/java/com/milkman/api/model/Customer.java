package com.milkman.api.model;

import com.milkman.api.util.enums.Gender;
import jakarta.persistence.*;
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

import static jakarta.persistence.EnumType.ORDINAL;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 11:57 AM
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CUSTOMER")
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_pk_seq")
    @SequenceGenerator(name = "customer_pk_seq", sequenceName = "customer_pk_seq", allocationSize = 1, initialValue = 1000)
    private Long customerId;
    @NotNull(message = "Customer name shouldn't null or empty!")
    private String customerName;
    @NotNull(message = "Customer email shouldn't null or empty!")
    private String customerEmail;
    @NotNull(message = "Customer mobile shouldn't be null or empty!")
    private String customerMobile;
    @NotNull(message = "Milk rate shouldn't null!")
    private Double ratePerLit;
    @Enumerated(ORDINAL)
    private Gender gender;
    @Embedded
    private Address address;
    private Boolean isAccountActive;
    private Boolean isUseDefaultPass;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;
        return getCustomerId() != null && Objects.equals(getCustomerId(), customer.getCustomerId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
