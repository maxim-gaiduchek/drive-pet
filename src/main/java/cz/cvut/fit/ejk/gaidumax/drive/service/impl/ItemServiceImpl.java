package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;
import cz.cvut.fit.ejk.gaidumax.drive.repository.ItemNaiveRepository;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.ItemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ItemServiceImpl implements ItemService {

    @Inject
    ItemNaiveRepository itemNaiveRepository;

    @Override
    public List<ItemDto> findAll(ItemFilter filter) {
        return itemNaiveRepository.findAll(filter);
    }
}
