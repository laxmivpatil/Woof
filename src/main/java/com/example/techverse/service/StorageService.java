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
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.example.techverse.Repository.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@Slf4j
public class StorageService {

 

	 
	// @Value("${azure.storage.account-name}")
	    private String storageAccountName="satyaprofilestorage";

	   // @Value("${azure.storage.container-string}")
	    private String container_string="DefaultEndpointsProtocol=https;AccountName=satyaprofilestorage;AccountKey=TRfxdipcJMaK5LMJPmv9z5Xmzj/34I5eKI/p/pTyrnOaqZKSlPQnPS4RA1b7n5tK5ml9BAcA7+B0+ASt4ObNkw==;EndpointSuffix=core.windows.net";

	   // @Value("${azure.storage.container-name}")
	    private String containerName="container";
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
	 
	 public String uploadFileOnAzure(MultipartFile file) {
	      try {
	          // Construct Azure Blob Storage URL
	          String blobServiceUrl = "https://satyaprofilestorage.blob.core.windows.net";
	          String blobContainerUrl = String.format("%s/%s", blobServiceUrl, containerName);

	          // Get the original file extension
	          String originalFileName = file.getOriginalFilename();
	          String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));

	          // Generate a unique name using timestamp and UUID
	          String uniqueBlobName = Instant.now().toEpochMilli() + "_" + UUID.randomUUID().toString() + fileExtension;

	          // Create BlobClient with connection string
	          BlobClientBuilder blobClientBuilder = new BlobClientBuilder().connectionString(container_string)
	                  .containerName(containerName).blobName(uniqueBlobName);

	          // Upload the file to Azure Blob Storage with the unique blob name
	          try (InputStream inputStream = file.getInputStream()) {
	              blobClientBuilder.buildClient().upload(inputStream, file.getSize(), true);
	          }

	          // Create a SAS token that's valid for 1 hour (adjust duration as needed)
	          // Create a SAS token without expiration time
	          OffsetDateTime expiryTime = OffsetDateTime.of(2099, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

	          
	          // Assign read permissions to the SAS token
	          BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);

	          // Set the start time for the SAS token (optional)
	          OffsetDateTime startTime = OffsetDateTime.now().minusMinutes(5);

	          BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
	                  .setStartTime(startTime);

	          // Generate SAS token for the blob
	          String sasToken = blobClientBuilder.buildClient().generateSas(sasSignatureValues);

	          // Return the URL of the uploaded file with the SAS token
	          String fileUrlWithSas = String.format("%s/%s?%s", blobContainerUrl, uniqueBlobName, sasToken);
	          return fileUrlWithSas;
	      } catch (IOException e) {
	          e.printStackTrace();
	          return null;
	      }
	  } 
	 
	 //cloud
	  

}
