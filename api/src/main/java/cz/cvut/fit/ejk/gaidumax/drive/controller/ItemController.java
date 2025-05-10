package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemSearchDto;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.ItemMapper;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.ItemService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/items")
public class ItemController {

    @Inject
    ItemService itemService;
    @Inject
    ItemMapper itemMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public List<ItemDto> findAll(@BeanParam ItemSearchDto searchDto) {
        var filter = itemMapper.searchDtoToFilter(searchDto);
        var items = itemService.findAll(filter);
        return itemMapper.toDtos(items);
    }
}
