package participationRequest.model;

import event.model.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "participation_request")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "event_id")
    private Event event;

    @Column(name = "requester_id")
    private User requester;

    @Column(name = "status")
    private Status status;
}
