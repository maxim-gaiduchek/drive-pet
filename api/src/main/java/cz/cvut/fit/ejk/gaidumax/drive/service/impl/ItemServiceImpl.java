package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.entity.Item;
import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;
import cz.cvut.fit.ejk.gaidumax.drive.repository.ItemRepository;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.ItemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ItemServiceImpl implements ItemService {

    @Inject
    ItemRepository itemRepository;

    @Override
    public List<Item> findAll(ItemFilter filter) {
        return itemRepository.findAll(filter);
    }
}
