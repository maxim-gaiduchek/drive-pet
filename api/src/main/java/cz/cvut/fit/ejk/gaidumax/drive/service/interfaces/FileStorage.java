package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorage {

    String upload(InputStream in, Long userId, String filePath) throws IOException;

    void delete(String filePath);
}
