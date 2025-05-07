package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileStorage;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class S3FileStorage implements FileStorage {

    @ConfigProperty(name = "quarkus.s3.endpoint-override")
    String s3Host;

    private static final String BUCKET_NAME = "drive-dev";
    private static final String FULL_PATH_TEMPLATE = "%s/%s/%s";
    private static final String FILE_PATH_TEMPLATE = "files/%d/%s";

    @Inject
    S3Client client;

    @PostConstruct
    void init() {
        client.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build());
    }

    @Override
    public String upload(InputStream in, Long userId, String filePath) throws IOException {
        var path = FILE_PATH_TEMPLATE.formatted(userId, filePath);
        var request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(path)
                .build();
        var body = RequestBody.fromInputStream(in, in.available());
        client.putObject(request, body);
        return FULL_PATH_TEMPLATE.formatted(s3Host, BUCKET_NAME, path);
    }
}
