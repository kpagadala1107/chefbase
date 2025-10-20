package com.kp.chefbase.service;

import com.azure.storage.blob.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class AzureBlobService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    private BlobContainerClient containerClient;

    @PostConstruct
    public void init() {
        String containerName = "images";
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.containerClient = serviceClient.getBlobContainerClient(containerName);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        return blobClient.getBlobUrl(); // returns public URL
    }
}
