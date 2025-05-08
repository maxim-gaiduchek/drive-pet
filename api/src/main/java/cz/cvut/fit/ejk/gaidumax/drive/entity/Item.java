package cz.cvut.fit.ejk.gaidumax.drive.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "item_view")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Item extends UuidBaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType type;
    @Column(name = "size", nullable = false)
    private Long size;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder parentFolder;
}
