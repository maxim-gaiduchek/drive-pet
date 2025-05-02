package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemSearchDto;
import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> findAll(ItemSearchDto searchDto);
}
