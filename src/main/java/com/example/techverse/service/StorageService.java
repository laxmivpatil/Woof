package com.example.techverse.service;
 
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.techverse.Repository.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class StorageService {

	@Autowired
	UserRepository userRepository;
	
   /* @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(MultipartFile file ) {
    	
    	  File fileObj = convertMultiPartFileToFile(file);
        UUID uuid = UUID.randomUUID();
        

        // Convert the UUID to a string
        String uuidString = uuid.toString().substring(0, 5);
      
        String fileName = "profile_" + uuidString;
        String fileExtension = getFileExtension(file.getOriginalFilename());

        if (fileExtension != null) {
            fileName += "." + fileExtension;
        }

        PutObjectRequest request = new PutObjectRequest(bucketName, file.getOriginalFilename(), fileObj)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(request);
        fileObj.delete();

        // Construct the URL after the file is uploaded
        String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + file.getOriginalFilename();

        return fileUrl;
    }

  
    


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
          System.out.println("Error converting multipartFile to file"+ e);
        }
        return convertedFile;
    }
    
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return null;
    }
    */
	
	 public String uploadFile(MultipartFile file ) {
	    	
   
       return file.toString();
   }
	 
	 
	 
	 //cloud
	 
	 

}
