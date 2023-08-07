package com.milkman.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import static jakarta.persistence.GenerationType.SEQUENCE;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/6/2023
 * @Time: 12:57 AM
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ACCOUNT")
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "account_pk_seq")
    @SequenceGenerator(name = "account_pk_seq", sequenceName = "account_pk_seq", allocationSize = 1, initialValue = 1000)
    private Long id;

    @NotNull(message = "Customer ID shouldn't be null or empty.")
    private Long customerId;

    @NotNull(message = "AM liter value shouldn't be null or empty.")
    private Double amLiter;

    @NotNull(message = "PM liter value shouldn't be null or empty.")
    private Double pmLiter;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date lastUpdateDate;

}
