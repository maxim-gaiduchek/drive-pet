package cz.cvut.fit.ejk.gaidumax.drive.utils;

import cz.cvut.fit.ejk.gaidumax.drive.entity.UuidBaseEntity;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class UuidUtils {

    public UUID generateNewUuid(UuidBaseEntity... entities) {
        var ids = Arrays.stream(entities)
                .map(UuidBaseEntity::getId)
                .collect(Collectors.toSet());
        UUID id;
        do {
            id = UUID.randomUUID();
        } while (ids.contains(id));
        return id;
    }
}
