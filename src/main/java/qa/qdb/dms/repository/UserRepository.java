package qa.qdb.dms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import qa.qdb.dms.model.User;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findAll();

    User findByFirstName(String firstName);

    User findByUserId(long userId);

    User save(User user);
}
