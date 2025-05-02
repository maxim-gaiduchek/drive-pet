package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemSearchDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> findAll(ItemSearchDto searchDto);
}
