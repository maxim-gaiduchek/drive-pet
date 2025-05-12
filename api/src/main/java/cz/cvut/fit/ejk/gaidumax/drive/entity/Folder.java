package cz.cvut.fit.ejk.gaidumax.drive.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "folder")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Folder extends UuidBaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<UserFolderAccess> accesses;
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder parentFolder;

    public List<UserFolderAccess> getAccesses() {
        if (accesses == null) {
            accesses = new ArrayList<>();
        }
        return accesses;
    }
}
