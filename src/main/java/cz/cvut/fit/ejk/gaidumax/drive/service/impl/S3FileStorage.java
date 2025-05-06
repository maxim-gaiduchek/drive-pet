package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileStorage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@ApplicationScoped
public class S3FileStorage implements FileStorage {

    private static final String BUCKET_NAME = "drive-dev";
    private static final String FILE_PATH_TEMPLATE = "/files/%d/%s";

    @Inject
    S3Client client;

    @Override
    public String upload(InputStream in, Long userId, String folderPath) {
        try {
            var path = FILE_PATH_TEMPLATE.formatted(userId, folderPath);
            var request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(path)
                    .build();
            var body = RequestBody.fromInputStream(in, in.available());
            client.putObject(request, body);
            return path;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
