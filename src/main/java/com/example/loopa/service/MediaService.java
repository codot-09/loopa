package com.example.loopa.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final Cloudinary cloudinary;

    public String uploadFile(String user,MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", generateFolder(user),
                        "resource_type", "auto" // rasm va videolar uchun
                ));
        return (String) uploadResult.get("secure_url"); // Cloudinary URL
    }

    private String generateFolder(String user){
        return "folder_" + user;
    }
}
