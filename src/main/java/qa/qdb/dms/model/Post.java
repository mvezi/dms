package qa.qdb.dms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "post")
public class Post extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id", updatable = false, nullable = false)
    private long postId;

    @Column(name = "title", updatable = true, nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "content", updatable = true, nullable = false)
    @Lob
    private String content;

    @Column(nullable = true)
    @Lob
    private byte[] document;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comment;

}
