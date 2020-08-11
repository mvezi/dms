package qa.qdb.dms.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qa.qdb.dms.model.Post;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long userId;
    private String firstName;
    Set<Post> posts;
}
