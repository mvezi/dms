package qa.qdb.dms.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qa.qdb.dms.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    Post findByTitle(String title);

    Post save(Post post);

    List<Post> findAll();

    @Query("UPDATE Post p set p.document = null where p.title =:title")
    @Modifying
    int unlinkDocument(@Param("title") String title);

    @Query("UPDATE Post p set p.document =:file where p.title =:postTitle")
    @Modifying
    int linkDocument(@Param("postTitle") String postTitle, @Param("file") byte[] file);
}
