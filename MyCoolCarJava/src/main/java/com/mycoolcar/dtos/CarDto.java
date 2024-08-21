package com.mycoolcar.dtos;


import java.util.List;
import java.util.Objects;

public record CarDto(String brand, String model,
                     int productYear, String description,
                     String mainImageUrl, List<String> imagesUrl, int rate) {

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof CarDto)) {
            return false;
        }
        CarDto carDto = (CarDto) other;
        return carDto.brand.equals(this.brand)
                && carDto.model.equals(this.model)
                && carDto.productYear == this.productYear
                && carDto.description.equals(this.description)
                && carDto.mainImageUrl.equals(this.mainImageUrl)
                && Objects.equals(carDto.imagesUrl, this.imagesUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, model,productYear,description,mainImageUrl, Objects.hashCode(imagesUrl));
    }
    
    @Override
    public String toString() {
        return "CarCreationDto[" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", productYear=" + productYear +
                ", description='" + description + '\'' +
                ", mainImageUrl='" + mainImageUrl + '\'' +
                ", imagesUrl=" + imagesUrl.toString() +
                ", rate=" + rate +
                ']';
    }
}
