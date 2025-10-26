package ewm.models.participationRequest.repo;

import ewm.models.participationRequest.model.ParticipationRequest;
import ewm.models.participationRequest.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepo extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findByIdIn(List<Long> ids);

    List<ParticipationRequest> findByEventIdAndStatus(Long eventId, Status status);

    long countByEventIdAndStatus(Long eventId, Status status);

    List<ParticipationRequest> findByRequesterId(Long userId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);
}
