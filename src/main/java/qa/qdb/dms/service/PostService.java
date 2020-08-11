package qa.qdb.dms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qdb.dms.model.Post;
import qa.qdb.dms.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post findPostByTitle(String title) {
        return postRepository.findByTitle(title);
    }

    public Optional<Post> findPostById(String id) {
        return postRepository.findById(Long.parseLong(id));
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public int unlinkDocument(String postTitle) {
        return postRepository.unlinkDocument(postTitle);
    }

    public int linkDocument(String postTitle, byte[] file) {
        return postRepository.linkDocument(postTitle, file);
    }
}
