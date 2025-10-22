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

    @Query(value = """
    SELECT e.* FROM events e
    WHERE e.state = 'PUBLISHED'
      AND (:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))
           OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))
      AND (:categories IS NULL OR e.category_id IN :categories)
      AND (:paid IS NULL OR e.paid = :paid)
      AND e.event_date >= COALESCE(:rangeStart, CURRENT_TIMESTAMP)
      AND e.event_date <= COALESCE(:rangeEnd, e.event_date)
      AND (:onlyAvailable = false OR e.participant_limit = 0 OR e.confirmed_requests < e.participant_limit)
    """,
            countQuery = """
    SELECT COUNT(*) FROM events e
    WHERE e.state = 'PUBLISHED'
      AND (:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))
           OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))
      AND (:categories IS NULL OR e.category_id IN :categories)
      AND (:paid IS NULL OR e.paid = :paid)
      AND e.event_date >= COALESCE(:rangeStart, CURRENT_TIMESTAMP)
      AND e.event_date <= COALESCE(:rangeEnd, e.event_date)
      AND (:onlyAvailable = false OR e.participant_limit = 0 OR e.confirmed_requests < e.participant_limit)
    """,
            nativeQuery = true)
    Page<Event> findPublicEvents(@Param("text") String text,
                                 @Param("categories") List<Long> categories,
                                 @Param("paid") Boolean paid,
                                 @Param("rangeStart") LocalDateTime rangeStart,
                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                 @Param("onlyAvailable") Boolean onlyAvailable,
                                 Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.initiator.id = :initiatorId")
    Page<Event> findByInitiatorId(@Param("initiatorId") Long initiatorId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.initiator.id = :initiatorId AND e.id = :eventId")
    Optional<Event> findEventByUserIdAndEventId(@Param("initiatorId") Long initiatorId, @Param("eventId") Long eventId);

    boolean existsByCategoryId(Long categoryId);

    @Query("SELECT e FROM Event e WHERE " +
            "e.initiator.id IN :users AND " +
            "e.eventState IN :states AND " +
            "e.category.id IN :categories AND " +
            "e.eventDate >= COALESCE(:rangeStart, e.eventDate) AND " +
            "e.eventDate <= COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> findWithAllFilters(@Param("users") List<Long> users,
                                   @Param("states") List<String> states,
                                   @Param("categories") List<Long> categories,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                   Pageable pageable);

    @Query("SELECT e FROM Event e WHERE " +
            "e.initiator.id IN :users AND " +
            "e.eventState IN :states AND " +
            "e.eventDate >= COALESCE(:rangeStart, e.eventDate) AND " +
            "e.eventDate <= COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> findWithUsersAndStates(@Param("users") List<Long> users,
                                       @Param("states") List<String> states,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);

    @Query("SELECT e FROM Event e WHERE " +
            "e.initiator.id IN :users AND " +
            "e.category.id IN :categories AND " +
            "e.eventDate >= COALESCE(:rangeStart, e.eventDate) AND " +
            "e.eventDate <= COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> findWithUsersAndCategories(@Param("users") List<Long> users,
                                           @Param("categories") List<Long> categories,
                                           @Param("rangeStart") LocalDateTime rangeStart,
                                           @Param("rangeEnd") LocalDateTime rangeEnd,
                                           Pageable pageable);

    @Query("SELECT e FROM Event e WHERE " +
            "e.eventState IN :states AND " +
            "e.category.id IN :categories AND " +
            "e.eventDate >= COALESCE(:rangeStart, e.eventDate) AND " +
            "e.eventDate <= COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> findWithStatesAndCategories(@Param("states") List<String> states,
                                            @Param("categories") List<Long> categories,
                                            @Param("rangeStart") LocalDateTime rangeStart,
                                            @Param("rangeEnd") LocalDateTime rangeEnd,
                                            Pageable pageable);

    @Query("SELECT e FROM Event e WHERE " +
            "e.initiator.id IN :users AND " +
            "e.eventDate >= COALESCE(:rangeStart, e.eventDate) AND " +
            "e.eventDate <= COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> findWithUsers(@Param("users") List<Long> users,
                              @Param("rangeStart") LocalDateTime rangeStart,
                              @Param("rangeEnd") LocalDateTime rangeEnd,
                              Pageable pageable);

    @Query("SELECT e FROM Event e WHERE " +
            "e.eventState IN :states AND " +
            "e.eventDate >= COALESCE(:rangeStart, e.eventDate) AND " +
            "e.eventDate <= COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> findWithStates(@Param("states") List<String> states,
                               @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd,
                               Pageable pageable);

    @Query("SELECT e FROM Event e WHERE " +
            "e.category.id IN :categories AND " +
            "e.eventDate >= COALESCE(:rangeStart, e.eventDate) AND " +
            "e.eventDate <= COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> findWithCategories(@Param("categories") List<Long> categories,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                   Pageable pageable);

    @Query("SELECT e FROM Event e WHERE " +
            "e.eventDate >= COALESCE(:rangeStart, e.eventDate) AND " +
            "e.eventDate <= COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> findWithDateRange(@Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd,
                                  Pageable pageable);


    List<Event> findByIdIn(List<Long> ids);
}

