package ewm.models.comments.repo;

import ewm.models.comments.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentRepo extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.event.id = :eventId ORDER BY c.created DESC")
    Page<Comment> findByEventId(@Param("eventId") Long eventId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.parent.id = :parentId ORDER BY c.created ASC")
    Page<Comment> findByParentId(@Param("parentId") Long parentId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId ORDER BY c.created ASC")
    Page<Comment> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
