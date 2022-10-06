package com.finalproject.everrent_be.domain.imageupload.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final UploadService s3Service;

    public String uploadImage(MultipartFile file)
    {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try(InputStream inputStream=file.getInputStream()){
            s3Service.uploadFile(inputStream,objectMetadata,fileName);
        }catch (IOException e){
            throw new IllegalArgumentException(String.format("파일 변환 중 에러 발생(%s)"));
        }
        return s3Service.getFileUrl(fileName);
    }


    private String createFileName(String originalFileName){
        return UUID.randomUUID().toString().substring(25).concat((getFileExtension(originalFileName)));
    }
    private String getFileExtension(String fileName)
    {
        try {
            String str=fileName.substring(fileName.lastIndexOf(".")); //파일.png -> 파일
            return str;
        }catch (StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다."));
        }
    }

}
