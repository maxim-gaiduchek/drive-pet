package cz.cvut.fit.ejk.gaidumax.drive.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "folder")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Folder extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder parentFolder;
}
