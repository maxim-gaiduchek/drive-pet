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

import java.time.ZonedDateTime;

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
    @Column(name = "file_type", nullable = false)
    private String fileType;
    @Column(name = "path", nullable = false)
    private String path;
    @Column(name = "size", nullable = false)
    private Long size;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder parentFolder;
    @Column(name = "user_access_type")
    @Enumerated(EnumType.STRING)
    private UserAccessType userAccessType;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "access_token_created_at")
    private ZonedDateTime accessTokenCreatedAt;
}
