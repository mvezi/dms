package qa.qdb.dms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qa.qdb.dms.model.Document;
import qa.qdb.dms.model.User;
import qa.qdb.dms.pojo.DmsError;
import qa.qdb.dms.pojo.DocumentDTO;
import qa.qdb.dms.response.DocumentResponse;
import qa.qdb.dms.service.DocumentService;
import qa.qdb.dms.service.UserService;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@Api(value = "Document API")
@RequestMapping("/credit/dms/documents")
public class DocumentController {

    private static Logger LOG = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @ApiOperation(value = "This operation is used to upload documents to the system", response = DocumentResponse.class)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> uploadDocument(@RequestParam MultipartFile file,
                                                           @RequestParam String userId) {
        User user = userService.findUserById(userId);
        if (user != null) {
            DocumentDTO document = new DocumentDTO();
            DocumentResponse response = new DocumentResponse();
            String documentName = StringUtils.cleanPath(file.getOriginalFilename());
            if(!documentName.contains(".pdf")) {
                DmsError error = new DmsError("Please upload pdf documents only", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            }
            document.setName(documentName);
            try {
                document.setFile(file.getBytes());
                document.setUser(user);
                LOG.info("Saving document: {}", documentName);
                Document doc = documentService.uploadDocument(document);
                if (doc != null) {
                    DocumentDTO savedDocument = mapper.map(doc, DocumentDTO.class);
                    response.setDocument(savedDocument);
                    return new ResponseEntity<>(response, HttpStatus.CREATED);
                } else {
                    DmsError error = new DmsError("Error saving document", HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }

            } catch (IOException e) {
                LOG.error("IOException for file: {}", StringUtils.cleanPath(file.getOriginalFilename()));
                DmsError error = new DmsError(e.getMessage(), HttpStatus.NO_CONTENT.value());
                return new ResponseEntity(error, HttpStatus.NO_CONTENT);
            }
        } else {
            DmsError error = new DmsError("User not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/download")
    @ApiOperation(value = "This operation is used to download documents from the system")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Document not found", response = DmsError.class)
    })
    public ResponseEntity downloadDocument(@RequestParam String documentId) {
        Document doc = documentService.findById(Long.parseLong(documentId));
        if (doc != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                    .body(doc.getFile());
        } else {
            DmsError error = new DmsError("Document not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("This operation is to update an existing document independently from a post")
    @PostMapping(path = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> updateDocument(@NotNull @RequestParam MultipartFile file,
                                                 @NotNull @RequestParam String documentId,
                                                 @NotNull @RequestParam String userId) {
        DocumentDTO document = new DocumentDTO();
        DocumentResponse response = new DocumentResponse();
        String documentName = StringUtils.cleanPath(file.getOriginalFilename());
        document.setName(documentName);

        try {
            User user = userService.findUserById(userId);
            document.setUser(user);
            Document foundDoc = documentService.findById(Long.parseLong(documentId));
            if (foundDoc != null && user != null) {
                document.setFile(file.getBytes());
                LOG.info("Saving document: {}", documentName);
                documentService.updateDocument(file.getBytes(), Long.parseLong(documentId), StringUtils.cleanPath(file.getOriginalFilename()));
                return new ResponseEntity<>("Document updated", HttpStatus.CREATED);
            } else {
                LOG.error("IOException for file: {}", StringUtils.cleanPath(file.getOriginalFilename()));
                DmsError error = new DmsError("NOT FOUND", HttpStatus.NO_CONTENT.value());
                return new ResponseEntity(error, HttpStatus.NO_CONTENT);
            }

        } catch (IOException e) {
            LOG.error("IOException for file: {}", StringUtils.cleanPath(file.getOriginalFilename()));
            DmsError error = new DmsError(e.getMessage(), HttpStatus.NO_CONTENT.value());
            return new ResponseEntity(error, HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation("This operation is used to delete a document")
    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> updateDocument(@NotNull @RequestParam("documentId") String documentId,
                                                 @NotNull @RequestParam("userId") String userId) {
        Document foundDoc = documentService.findById(Long.parseLong(documentId));
        if (foundDoc != null && userId != "") {
            documentService.deleteById(Long.parseLong(documentId));
            return new ResponseEntity<>("Document deleted", HttpStatus.CREATED);
        } else {
            LOG.error("IOException for file: {}", documentId);
            DmsError error = new DmsError("NOT FOUND", HttpStatus.NO_CONTENT.value());
            return new ResponseEntity(error, HttpStatus.NO_CONTENT);
        }
    }
}
