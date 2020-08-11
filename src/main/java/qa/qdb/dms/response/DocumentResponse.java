package qa.qdb.dms.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qa.qdb.dms.pojo.DocumentDTO;
import qa.qdb.dms.pojo.DmsError;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {
    private DocumentDTO document;
    private DmsError dmsError;
}
