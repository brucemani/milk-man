package com.milkman.api.model;

import com.milkman.api.util.converter.ListPrivilegeToString;
import com.milkman.api.util.converter.ListToStringConvertor;
import com.milkman.api.util.enums.Privilege;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.EnumType.ORDINAL;
import static jakarta.persistence.GenerationType.SEQUENCE;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/24/2023
 * @Time: 10:10 PM
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ROLE")
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long roleId;
    @NotNull(message = "User Id shouldn't null!")
    private Long customerId;
    @NotNull(message = "Role list shouldn't null!")
    @Convert(converter = ListToStringConvertor.class)
    private Set<String> roleList;
    @NotNull(message = "Privilege list shouldn't null!")
    @Convert(converter = ListPrivilegeToString.class)
    private List<Privilege> privilegeList;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastUpdateDate;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return getRoleId() != null && Objects.equals(getRoleId(), role.getRoleId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
