package qa.qdb.dms.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user")
public class User extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", updatable = false, nullable = false)
    private long userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @OneToMany(mappedBy = "user")
    Set<Post> posts;

    // OneToMany Comments

}
