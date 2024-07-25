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

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Slf4j
@Service
public class CarModelListService {
    private final CarBrandRepository carBrandRepository;
    private final CarModelRepository carModelRepository;
    private final CSVReader reader;

    @Autowired
    public CarModelListService(CarBrandRepository carBrandRepository,
                               CarModelRepository carModelRepository,
                               CSVReader reader) {
        this.carBrandRepository = carBrandRepository;
        this.carModelRepository = carModelRepository;
        this.reader = reader;
    }

    @PostConstruct
    public void importCarModelsToDB() throws IOException, CsvValidationException {
        log.info("Starting import of car models to database.");
        String[] carBrandModels;
        while ((carBrandModels = reader.readNext()) != null) {
            importCarBrandWithModels(carBrandModels);
        }
        log.info("Data was successfully imported from CSV file to DataBase");
    }

    private void importCarBrandWithModels(String[] carBrandModels) {
        if (carBrandModels.length < 2) {
            log.warn("Skipping invalid entry in CSV: " + Arrays.toString(carBrandModels));
            return;
        }
        String carBrandName = carBrandModels[0];
        log.debug("Processing car brand: {}", carBrandName);

        CarBrand carBrand = new CarBrand(carBrandName);
        carBrandRepository.save(carBrand);
        log.debug("Saved car brand: {}", carBrandName);

        Set<CarModel> carModels = carBrand.getCarModels();
        for (int i = 1; i < carBrandModels.length; i++) {
            String carModelName = carBrandModels[i];
            log.debug("Processing car model: {}", carModelName);

            CarModel carModel = new CarModel(carModelName);
            carModel.setCarBrand(carBrand);
            carModelRepository.save(carModel);
            carModels.add(carModel);
            log.debug("Saved car model: {} for brand: {}", carModelName, carBrandName);
        }
        carBrand.setCarModels(carModels);
        carBrandRepository.save(carBrand);
        log.info("Successfully imported car brand: {} with models: {}", carBrandName, carModels);
    }
}


