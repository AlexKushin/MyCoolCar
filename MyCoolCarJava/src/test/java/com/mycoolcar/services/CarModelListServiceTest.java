package com.mycoolcar.services;

import com.mycoolcar.entities.CarBrand;
import com.mycoolcar.entities.CarModel;
import com.mycoolcar.repositories.CarBrandRepository;
import com.mycoolcar.repositories.CarModelRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarModelListServiceTest {


    @Mock
    private CarBrandRepository carBrandRepository;

    @Mock
    private CarModelRepository carModelRepository;

    @Mock
    private CSVReader reader;

    private CarModelListService carModelListService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        carModelListService = new CarModelListService(carBrandRepository, carModelRepository, reader);
    }

    @Test
    void testImportCarModelsToDB() throws IOException, CsvValidationException {
        // Mock CSV data
        String[] carBrandModels1 = {"Brand1", "Model1", "Model2"};
        String[] carBrandModels2 = {"Brand2", "Model3", "Model4"};

        // Mock CSVReader behavior
        when(reader.readNext()).thenReturn(carBrandModels1, carBrandModels2, null); // null indicates end of file

        // Mock CarBrand and CarModel behavior
        CarBrand carBrand1 = new CarBrand("Brand1");
        CarBrand carBrand2 = new CarBrand("Brand2");
        CarModel carModel1 = new CarModel("Model1");
        CarModel carModel2 = new CarModel("Model2");
        CarModel carModel3 = new CarModel("Model3");
        CarModel carModel4 = new CarModel("Model4");

        // Mock save methods in repositories
        when(carBrandRepository.save(any())).thenReturn(carBrand1, carBrand2);
        when(carModelRepository.save(any())).thenReturn(carModel1, carModel2, carModel3, carModel4);

        // Call the method under test
        carModelListService.importCarModelsToDB();

        // Verify that repositories' save methods were called with correct parameters
        verify(carBrandRepository, times(4)).save(any());
        verify(carModelRepository, times(4)).save(any());
    }
}