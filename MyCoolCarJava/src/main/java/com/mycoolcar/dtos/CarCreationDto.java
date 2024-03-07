package com.mycoolcar.dtos;


import java.util.Arrays;
import java.util.Objects;

public record CarCreationDto(String brand, String model,
                             int productYear, String description,
                             String mainImageUrl, String[] imagesUrl, int rate) {

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof CarCreationDto)) {
            return false;
        }
        CarCreationDto carCreationDto = (CarCreationDto) other;
        return carCreationDto.brand.equals(this.brand)
                && carCreationDto.model.equals(this.model)
                && carCreationDto.productYear == this.productYear
                && carCreationDto.description.equals(this.description)
                && carCreationDto.mainImageUrl.equals(this.mainImageUrl)
                && Arrays.equals(carCreationDto.imagesUrl, this.imagesUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, model,productYear,description,mainImageUrl, Arrays.hashCode(imagesUrl));
    }
    
    @Override
    public String toString() {
        return "CarCreationDto[" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", productYear=" + productYear +
                ", description='" + description + '\'' +
                ", mainImageUrl='" + mainImageUrl + '\'' +
                ", imagesUrl=" + Arrays.toString(imagesUrl) +
                ", rate=" + rate +
                ']';
    }
}
