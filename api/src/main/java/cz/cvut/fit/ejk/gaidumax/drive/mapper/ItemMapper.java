package cz.cvut.fit.ejk.gaidumax.drive.mapper;

import cz.cvut.fit.ejk.gaidumax.drive.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemSearchDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Item;
import cz.cvut.fit.ejk.gaidumax.drive.entity.ItemType;
import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.AuthService;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.ZonedDateTime;
import java.util.List;

@Mapper(config = MapperConfiguration.class)
public abstract class ItemMapper {

    @ConfigProperty(name = "drive-pet.item.access.token.age")
    long accessTokenAge;

    @Inject
    AuthService authService;

    public abstract ItemDto toDto(Item item);

    public abstract List<ItemDto> toDtos(List<Item> items);

    public abstract ItemFilter searchDtoToFilter(ItemSearchDto searchDto);

    @AfterMapping
    protected void setupAccessToken(@MappingTarget ItemDto itemDto, Item item) {
        if (item.getAccessTokenCreatedAt() == null
                || item.getAccessTokenCreatedAt().plusMinutes(accessTokenAge).isBefore(ZonedDateTime.now())) {
            itemDto.setAccessToken(null);
            return;
        }
        if (ItemType.FILE.equals(item.getType()) && !authService.isUserOwnerOfFile(item.getId())) {
            itemDto.setAccessToken(null);
            return;
        }
        if (ItemType.FOLDER.equals(item.getType()) && !authService.isUserOwnerOfFolder(item.getId())) {
            itemDto.setAccessToken(null);
        }
    }
}
