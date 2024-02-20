package com.example.techverse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.service.AzureStorageService;
import com.example.techverse.service.ExcelReaderService;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelReaderService excelReaderService;
    @Autowired
    private AzureStorageService azureStorageService;

    @PostMapping("/read")
    public void readExcelAndStoreData(@RequestParam String filePath) {
    	    excelReaderService.readAndStoreData(filePath);
    }
    @PostMapping("/readImg")
    public String readExcelAndStoreData1(@RequestPart MultipartFile filePath) {
    	System.out.println(filePath);
    	
    	return azureStorageService.uploadFileOnAzure(filePath);
       // excelReaderService.readAndStoreData(filePath);
    }
}
