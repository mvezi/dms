package qa.qdb.dms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import qa.qdb.dms.model.Document;
import qa.qdb.dms.model.Post;
import qa.qdb.dms.model.User;
import qa.qdb.dms.pojo.Comment;
import qa.qdb.dms.pojo.CommentList;
import qa.qdb.dms.pojo.DmsError;
import qa.qdb.dms.service.DocumentService;
import qa.qdb.dms.service.PostService;
import qa.qdb.dms.service.UserService;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@RestController
@Api("Api for Posts management")
@RequestMapping("/credit/dms/posts")
@RequiredArgsConstructor
public class PostController {

    private static Logger LOG = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    private final PlaceHolderClient placeHolderClient;

    @ApiOperation("This operation is for finding all posts")
    @GetMapping("/findall")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }

    @ApiOperation("This operation is used to create a post")
    @PostMapping(value = "/create")
    public ResponseEntity<Post> createPost(@NotNull @RequestParam("userId") String userId,
                                           @RequestParam(value = "documentId", required = false) String documentId,
                                           @NotNull @RequestParam("postTitle") String title,
                                           @NotNull @RequestParam("content") String content) {
        Post post = new Post();
        User user = userService.findUserById(userId);
        if (user != null) {
            if (!documentId.equals("")) {
                Document document = documentService.findById(Long.parseLong(documentId));
                if (document != null) {
                    post.setDocument(document.getFile());
                }
            }
            post.setTitle(title);
            post.setAuthor(user.getFirstName());
            post.setUser(user);
            post.setContent(content);
            Post savedPost = postService.savePost(post);
            return new ResponseEntity<>(postService.savePost(savedPost), HttpStatus.OK);
        } else {
            DmsError error = new DmsError("User not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("This operation is used to unlink a document from a post")
    @GetMapping(value = "/unlink")
    public ResponseEntity unlinkDocument(@RequestParam("postTitle") String postTitle, @RequestParam("userId") String userId) {
        User user = userService.findUserById(userId);
        if (user != null) {
            if (!postTitle.equals("")) {
                Post post = postService.findPostByTitle(postTitle);
                if (post != null) {
                    LOG.info("Unlinking document...");
                    if (postService.unlinkDocument(postTitle) > 0) {
                        return ResponseEntity.ok("Document unlinked");
                    } else {
                        DmsError error = new DmsError("Failed to unlink document!!", HttpStatus.NOT_MODIFIED.value());
                        return new ResponseEntity(error, HttpStatus.NOT_MODIFIED);
                    }
                } else {
                    DmsError error = new DmsError("Post not found!!", HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity(error, HttpStatus.NOT_FOUND);
                }
            } else {
                DmsError error = new DmsError("Post title cannot be empty", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity(error, HttpStatus.BAD_GATEWAY);
            }
        } else {
            DmsError error = new DmsError("User not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("This operation is used to link a document to a post")
    @GetMapping("/link")
    public ResponseEntity linkDocument(@RequestParam("postTitle") String postTitle,
                                       @RequestParam("documentId") String documentId,
                                       @RequestParam("userId") String userId) {
        User user = userService.findUserById(userId);
        if (user != null) {
            if (!postTitle.equals("") && !documentId.equals("")) {
                Post post = postService.findPostByTitle(postTitle);
                Document document = documentService.findById(Long.parseLong(documentId));
                if (post != null) {
                    LOG.info("Linking document...");
                    if (postService.linkDocument(postTitle, document.getFile()) > 0) {
                        return ResponseEntity.ok("Document linked");
                    } else {
                        DmsError error = new DmsError("Failed to link document!!", HttpStatus.NOT_MODIFIED.value());
                        return new ResponseEntity(error, HttpStatus.NOT_MODIFIED);
                    }
                } else {
                    DmsError error = new DmsError("Post not found!!", HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity(error, HttpStatus.NOT_FOUND);
                }
            } else {
                DmsError error = new DmsError("Post title or documentId cannot be empty", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity(error, HttpStatus.BAD_GATEWAY);
            }
        } else {
            DmsError error = new DmsError("User not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("We use this operation to view a particular post")
    @GetMapping("/{id}")
    public ResponseEntity<Post> viewPost(@PathVariable("id") String id) {

        final String url = "https://jsonplaceholder.typicode.com/posts/" + id +"/comments";
        List<Comment> rateResponse = placeHolderClient.getCommentsById(Long.parseLong(id));
        List<qa.qdb.dms.model.Comment> savedComments = new ArrayList<>();
        Optional<Post> post = postService.findPostById(id);

        if(post.isPresent()) {
            for(Comment r: rateResponse) {
                qa.qdb.dms.model.Comment c = new qa.qdb.dms.model.Comment();
                c.setId(r.getId());
                c.setPostId(r.getPostId());
                c.setBody(r.getBody());
                c.setEmail(r.getEmail());
                c.setName(r.getName());
                savedComments.add(c);
                    }
            post.get().setComment(savedComments);
            return new ResponseEntity<>(post.get(), HttpStatus.OK);
        } else {
            DmsError error = new DmsError("Post not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
    }
}
