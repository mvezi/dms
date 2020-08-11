package qa.qdb.dms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CommentList {
    private List<Comment> comments;

    public CommentList(List<Comment> comments) {
        this.comments = comments;
    }

    public CommentList() {
    }
}
