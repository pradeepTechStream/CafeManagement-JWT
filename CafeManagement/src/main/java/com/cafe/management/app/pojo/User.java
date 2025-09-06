package com.cafe.management.app.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name="User.findByEmailId",query="Select u from User u where u.email=:email")

@NamedQuery(name="User.getAllUser",query="Select new com.cafe.management.app.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'")

@NamedQuery(name="User.updateStatus",query="Update User u set u.status=:status where u.id=:id")

@NamedQuery(name="User.getAllAdmin",query="Select u.email from User u where u.role='admin'")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contactNumber", nullable = false)
    private String contactNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "role", nullable = false)
    private String role;


}
