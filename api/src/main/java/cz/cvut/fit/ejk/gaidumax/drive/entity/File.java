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
@Table(name = "file")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class File extends UuidBaseEntity {

    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "file_type", nullable = false)
    private String fileType;
    @Column(name = "s3_file_path", length = 1023)
    private String s3FilePath;
    @Column(name = "size", nullable = false)
    private Long size;
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<UserFileAccess> accesses;
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder parentFolder;

    public List<UserFileAccess> getAccesses() {
        if (accesses == null) {
            accesses = new ArrayList<>();
        }
        return accesses;
    }
}
