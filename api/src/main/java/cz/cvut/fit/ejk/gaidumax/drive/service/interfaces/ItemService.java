package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.entity.Item;
import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;

import java.util.List;

public interface ItemService {

    List<Item> findAll(ItemFilter filter);
}
