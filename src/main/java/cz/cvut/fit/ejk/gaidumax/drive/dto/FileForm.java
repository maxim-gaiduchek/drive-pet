package cz.cvut.fit.ejk.gaidumax.drive.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FileForm {

    @NotNull(message = "File content must not be null")
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private FileUpload file;

    @NotNull(message = "File DTO must not be null")
    @FormParam("dto")
    @PartType(MediaType.APPLICATION_JSON)
    private FileDto fileDto;
}
