package com.mycoolcar.dtos;


import java.util.List;
import java.util.Objects;

public record CarCreationDto(String brand, String model,
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
        if (!(other instanceof CarCreationDto)) {
            return false;
        }
        CarCreationDto carCreationDto = (CarCreationDto) other;
        return carCreationDto.brand.equals(this.brand)
                && carCreationDto.model.equals(this.model)
                && carCreationDto.productYear == this.productYear
                && carCreationDto.description.equals(this.description)
                && carCreationDto.mainImageUrl.equals(this.mainImageUrl)
                && Objects.equals(carCreationDto.imagesUrl, this.imagesUrl);
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
