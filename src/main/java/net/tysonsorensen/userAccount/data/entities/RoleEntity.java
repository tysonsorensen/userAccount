package net.tysonsorensen.userAccount.data.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Data
public class RoleEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "roleName")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    public void addUser(final UserEntity user) {
        users.add(user);
    }

    public void removeUser(final UserEntity user) {
        users.remove(user);
    }
}
