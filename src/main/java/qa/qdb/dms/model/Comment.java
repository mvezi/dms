package qa.qdb.dms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id", updatable = false, nullable = false)
    private long id;

    private long postId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String body;

    private String email;
}
