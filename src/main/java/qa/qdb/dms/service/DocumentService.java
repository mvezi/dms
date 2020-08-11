package qa.qdb.dms.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qdb.dms.controller.PostController;
import qa.qdb.dms.model.Document;
import qa.qdb.dms.pojo.DocumentDTO;
import qa.qdb.dms.repository.DocumentRepository;

@Service
@Transactional
public class DocumentService {

    private static Logger LOG = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DocumentRepository documentRepository;

    public Document uploadDocument(DocumentDTO documentDTO) {
        Document returnedDoc = documentRepository.save(mapper.map(documentDTO, Document.class));
        return returnedDoc;
    }

    public Document findById(long id) {
        Document document = documentRepository.findById(id);
        return document;
    }

    public void updateDocument(byte[] document, long id, String name) {
        LOG.info("Updating file...");
        documentRepository.updateDocument(document, id, name);
    }

    public void deleteById(long id) {
        LOG.info("Deleting file...");
        documentRepository.deleteById(id);
    }

}
