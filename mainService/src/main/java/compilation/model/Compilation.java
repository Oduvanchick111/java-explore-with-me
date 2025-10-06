package compilation.model;

import event.model.Event;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "compilations")
public class Compilation {
    private Long id;
    private Boolean pinned;
    private String title;
    List<Event> events;
}
