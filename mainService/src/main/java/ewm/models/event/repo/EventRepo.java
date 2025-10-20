package ewm.models.event.repo;

import ewm.models.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepo extends JpaRepository<Event, Long> {

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.eventState = 'PUBLISHED'
              AND (:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))
                   OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))
              AND (:paid IS NULL OR e.paid = :paid)
              AND (:categories IS NULL OR e.category.id IN :categories)
              AND (COALESCE(:rangeStart, CURRENT_TIMESTAMP) IS NULL OR e.eventDate >= COALESCE(:rangeStart, CURRENT_TIMESTAMP))
              AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)
              AND (
                  :onlyAvailable = false OR
                  e.participantLimit = 0 OR
                  e.confirmedRequests < e.participantLimit
              )
            """)
    Page<Event> findPublicEvents(@Param("text") String text,
                                 @Param("categories") List<Integer> categories,
                                 @Param("paid") Boolean paid,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                 @Param("onlyAvailable") Boolean onlyAvailable,
                                 Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.initiator.id = :initiatorId")
    Page<Event> findByInitiatorId(@Param("initiatorId") Long initiatorId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.initiator.id = :initiatorId AND e.id = :eventId")
    Optional<Event> findEventByUserIdAndEventId(@Param("initiatorId") Long initiatorId, @Param("eventId") Long eventId);

    @Query("SELECT e FROM Event e WHERE e.id = :eventId AND e.eventState = 'PUBLISHED' AND e.initiator.id != :userId")
    Optional<Event> findByIdWithChecks(@Param("eventId") Long eventId, @Param("userId") Long userId);

    boolean existsByCategoryId(Long categoryId);

    @Query("SELECT e FROM Event e WHERE " +
            "(:users IS NULL OR e.initiator.id IN :users) AND " +
            "(:states IS NULL OR e.eventState IN :states) AND " +
            "(:categories IS NULL OR e.category.id IN :categories) AND " +
            "(:rangeStart IS NULL OR e.eventDate >= :rangeStart) AND " +
            "(:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)")
    Page<Event> findEventsByAdminFilters(@Param("users") List<Long> users,
                                         @Param("states") List<String> states,
                                         @Param("categories") List<Long> categories,
                                         @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd,
                                         Pageable pageable);

    List<Event> findByIdIn(List<Long> ids);
}

