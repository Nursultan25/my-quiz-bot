package kg.megacom.myquizbot.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    private Long id;
}
