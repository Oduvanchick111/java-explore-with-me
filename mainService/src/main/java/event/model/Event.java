package event.model;

import category.model.Category;
import jakarta.persistence.*;
import location.model.Location;
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
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "description")
    private String description;

    @Column(name = "category_id")
    private Category category;

    @Column(name = "location_id")
    private Location location;

    @Column(name = "initiator_id")
    private User initiator;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "views")
    private int views;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
}
