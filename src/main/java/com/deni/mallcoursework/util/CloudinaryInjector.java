package com.deni.mallcoursework.util;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryInjector {
    private static Cloudinary cloudinary;

    @Autowired
    public CloudinaryInjector(Cloudinary cloudinary) {
        CloudinaryInjector.cloudinary = cloudinary;
    }

    public static void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
