package cz.cvut.fit.ejk.gaidumax.drive.mapper;

import cz.cvut.fit.ejk.gaidumax.drive.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemSearchDto;
import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface ItemMapper {

    ItemFilter searchDtoToFilter(ItemSearchDto searchDto);
}
