package cz.cvut.fit.ejk.gaidumax.drive.mapper;

import cz.cvut.fit.ejk.gaidumax.drive.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemSearchDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Item;
import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface ItemMapper {

    ItemDto toDtos(Item item);
    
    List<ItemDto> toDtos(List<Item> items);

    ItemFilter searchDtoToFilter(ItemSearchDto searchDto);
}
