package cz.cvut.fit.ejk.gaidumax.drive.config;

import org.mapstruct.Builder;
import org.mapstruct.MappingConstants;
import org.mapstruct.MapperConfig;

@MapperConfig(
        componentModel = MappingConstants.ComponentModel.JAKARTA,
        builder = @Builder(disableBuilder = true)
)
public interface MapperConfiguration {
}
