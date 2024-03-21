package com.mycoolcar.services;

import com.mycoolcar.entities.CarBrand;
import com.mycoolcar.entities.CarModel;
import com.mycoolcar.repositories.CarBrandRepository;
import com.mycoolcar.repositories.CarModelRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CarModelListService {

    private final CarBrandRepository carBrandRepository;
    private final CarModelRepository carModelRepository;

    @Autowired
    public CarModelListService(CarBrandRepository carBrandRepository,
                               CarModelRepository carModelRepository) {
        this.carBrandRepository = carBrandRepository;
        this.carModelRepository = carModelRepository;
    }

    @PostConstruct
    public void importToDB() {
        try (CSVReader reader = new CSVReader(new FileReader(
                "MyCoolCarJava/src/main/resources/car_models_list.csv", StandardCharsets.UTF_8))) {
            String[] carBrandModels;
            while ((carBrandModels = reader.readNext()) != null) {
                CarBrand carBrand = new CarBrand();
                carBrand.setName(carBrandModels[0]);
                List<CarModel> carModels = new ArrayList<>();
                for (int i = 1; i < carBrandModels.length; i++) {
                    CarModel carModel = new CarModel(carBrandModels[i]);
                    carModelRepository.save(carModel);
                    carModels.add(carModel);
                }
                carBrand.setCarModels(carModels);
                carBrandRepository.save(carBrand);
            }
            log.info("Data was successfully imported from CSV file to DataBase");
        } catch (FileNotFoundException e) {
            log.error("There is no file to read {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Can't read file by path");
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }
}


