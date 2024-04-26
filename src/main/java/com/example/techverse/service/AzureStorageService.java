package com.example.techverse.service;
 
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobServiceClientBuilder;  
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID; 
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile; 

 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class AzureStorageService {

	 // @Value("${azure.storage.account-name}")
	    private String storageAccountName="satyaprofilestorage";

	   // @Value("${azure.storage.container-string}")
	    private String container_string="DefaultEndpointsProtocol=https;AccountName=satyaprofilestorage;AccountKey=TRfxdipcJMaK5LMJPmv9z5Xmzj/34I5eKI/p/pTyrnOaqZKSlPQnPS4RA1b7n5tK5ml9BAcA7+B0+ASt4ObNkw==;EndpointSuffix=core.windows.net";

	    @Value("${azure.storage.container-name}")
	    private String containerName;
 
 
  private String uploadDir="F:\\MyProject\\SatyaAdminApp\\Images";
  
 public String uploadImgOnAzure(File file) {
	  
	  try {
          // Construct Azure Blob Storage URL
          String blobServiceUrl = "https://satyaprofilestorage.blob.core.windows.net";
          String blobContainerUrl = String.format("%s/%s", blobServiceUrl, containerName);

          // Get the original file extension
          String originalFileName = file.getName();
          String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));

          // Generate a unique name using timestamp and UUID
          String uniqueBlobName = originalFileName;
          System.out.println(originalFileName);

          // Create BlobClient with connection string
          BlobClientBuilder blobClientBuilder = new BlobClientBuilder().connectionString(container_string)
                  .containerName(containerName).blobName(uniqueBlobName);

          // Upload the file to Azure Blob Storage with the unique blob name
          try (InputStream inputStream = new FileInputStream(file)) {
              blobClientBuilder.buildClient().upload(inputStream, file.length(), true);
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
 
  
}
