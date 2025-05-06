package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import java.io.InputStream;

public interface FileStorage {

    String upload(InputStream in, Long userId, String folderPath);
}
