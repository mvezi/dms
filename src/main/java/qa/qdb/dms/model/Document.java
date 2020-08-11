package qa.qdb.dms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Data
@Table(name = "document")
public class Document extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id", updatable = false, nullable = false)
    private long documentId;

    @Column
    @NotNull
    private String name;

    @Column(name = "post_id")
    private Post post;

    @Column(name = "file")
    @Lob
    private byte[] file;

}
