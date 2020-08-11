package qa.qdb.dms.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import qa.qdb.dms.model.Post;
import qa.qdb.dms.model.User;

@Data
@NoArgsConstructor
public class DocumentDTO {
    private String name;

    private Post post;

    private byte[] file;

    private User user;
}
