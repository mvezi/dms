package qa.qdb.dms.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qa.qdb.dms.model.Document;

import java.util.List;

@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {

    Document findByName(String name);

    List<Document> findAll();

    @Query("UPDATE Document d set d.file=:document, d.name=:name where d.documentId=:id")
    @Modifying
    void updateDocument(@Param("document") byte[] document, @Param("id") long id, @Param("name") String name);

    Document save(Document document);

    Document findById(long id);

    @Query("DELETE from Document d where d.documentId=:documentId")
    @Modifying
    void deleteById(@Param("documentId") long documentId);
}
