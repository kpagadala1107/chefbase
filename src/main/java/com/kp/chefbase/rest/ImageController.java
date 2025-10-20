package com.kp.chefbase.rest;

import com.kp.chefbase.service.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private AzureBlobService azureBlobService;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        return azureBlobService.uploadFile(file);
    }
}
