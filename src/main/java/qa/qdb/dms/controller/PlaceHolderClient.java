package qa.qdb.dms.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import qa.qdb.dms.pojo.Comment;

import java.util.List;

@FeignClient(value = "placeHolderClient", url = "https://jsonplaceholder.typicode.com/")
public interface PlaceHolderClient {

    @RequestMapping(method = RequestMethod.GET, value = "/posts/{postId}/comments", produces = "application/json")
    List<Comment> getCommentsById(@PathVariable("postId") Long postId);
}
