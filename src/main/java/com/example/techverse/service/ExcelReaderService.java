package com.example.techverse.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 
import org.springframework.stereotype.Service;

import com.example.techverse.Model.MonthlyDetails;
import com.example.techverse.Model.Pet;
import com.example.techverse.Repository.MonthlyDetailsRepository;
import com.example.techverse.Repository.PetRepository; 
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.IOException;
 

@Service
public class ExcelReaderService {

    private final PetRepository petRepository;
    private final MonthlyDetailsRepository monthlyDetailsRepository;

    public ExcelReaderService(PetRepository petRepository, MonthlyDetailsRepository monthlyDetailsRepository) {
        this.petRepository = petRepository;
        this.monthlyDetailsRepository = monthlyDetailsRepository;
    }

    public void readAndStoreData(String filePath) {
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {
     
            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet

            Iterator<Row> rowIterator = sheet.iterator();
            // Skip the header row if it exists
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            Row row = rowIterator.next();
            while (rowIterator.hasNext()) {
                
                 
                Pet pet = createPetFromRow(row);
                
                petRepository.save(pet);
                for (int cellInde = 0; cellInde <=7; cellInde++) {			
                createMonthlyDetailsFromRow(row, pet);
               // if(rowIterator.hasNext()) {
                row = rowIterator.next();
             //   }
              //  else {
               // 	break;
              //  }
                }
            }
        } catch (IOException  e ) {
            // Handle exceptions
            e.printStackTrace();
        }
    }

    private Pet createPetFromRow(Row row) {
        Pet pet = new Pet();
          pet.setPetName(row.getCell(1).getStringCellValue());
          pet.setPetCategory(row.getCell(2).getStringCellValue());
          pet.setGender(row.getCell(3).getStringCellValue());
        pet.setDescription(row.getCell(4).getStringCellValue());
        // Map other fields

        return pet;
    }

  
    
      private void createMonthlyDetailsFromRow(Row row, Pet pet) {
    	  
        MonthlyDetails monthlyDetails = new MonthlyDetails();
        int cellIndex=5;
        monthlyDetails.setMonth(String.valueOf(row.getCell(cellIndex).getNumericCellValue()));
        monthlyDetails.setFood(row.getCell(cellIndex + 1).getStringCellValue());
        monthlyDetails.setExercise(row.getCell(cellIndex + 2).getStringCellValue());
        monthlyDetails.setToysToPlay(row.getCell(cellIndex + 3).getStringCellValue());
        monthlyDetails.setColor(row.getCell(cellIndex + 4).getStringCellValue());
        monthlyDetails.setActivity(row.getCell(cellIndex + 5).getStringCellValue());
        monthlyDetails.setGrooming(row.getCell(cellIndex + 6).getStringCellValue());
        monthlyDetails.setEnclosure(row.getCell(cellIndex + 7).getStringCellValue());
        monthlyDetails.setClothes(row.getCell(cellIndex + 8).getStringCellValue());
        monthlyDetails.setVaccination(row.getCell(cellIndex + 9).getStringCellValue());
        monthlyDetails.setWeight(row.getCell(cellIndex + 10).getStringCellValue());
        monthlyDetails.setHealthCare(row.getCell(cellIndex + 11).getStringCellValue());
        monthlyDetails.setPrecautions(row.getCell(cellIndex + 12).getStringCellValue());
        if(row.getCell(cellIndex + 13)!=null) {
        monthlyDetails.setPregnancyPrecautions(row.getCell(cellIndex + 13).getStringCellValue());
        }
        else {
        	 monthlyDetails.setPregnancyPrecautions("");
             
        }
        monthlyDetails.setPet(pet);
         monthlyDetailsRepository.save(monthlyDetails);
      
}

}
