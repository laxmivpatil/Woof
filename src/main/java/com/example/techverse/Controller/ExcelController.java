package com.example.techverse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.techverse.service.ExcelReaderService;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelReaderService excelReaderService;

    @PostMapping("/read")
    public void readExcelAndStoreData(@RequestParam String filePath) {
    	System.out.println(filePath);
        excelReaderService.readAndStoreData(filePath);
    }
}
