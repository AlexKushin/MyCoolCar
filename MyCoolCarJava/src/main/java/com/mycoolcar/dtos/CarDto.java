package com.mycoolcar.dtos;


import java.util.List;
//Todo  Think about Compact Constructors and overriding methods
public record CarDto(Long id, String brand, String model,
                     int productYear, String description,
                     String mainImageUrl, List<String> imagesUrl,
                     int rate, Long userId, String userFirstName ) {

    @Override
    public String toString() {
        return "CarDto{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", productYear=" + productYear +
                ", description='" + description + '\'' +
                ", mainImageUrl='" + mainImageUrl + '\'' +
                ", imagesUrl=" + imagesUrl +
                ", rate=" + rate +
                ", userId=" + userId +
                ", userFirstName='" + userFirstName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarDto carDto = (CarDto) o;
        return rate == carDto.rate && productYear == carDto.productYear
                && id.equals(carDto.id) && userId.equals(carDto.userId)
                && brand.equals(carDto.brand) && model.equals(carDto.model)
                && description.equals(carDto.description)
                && mainImageUrl.equals(carDto.mainImageUrl)
                && userFirstName.equals(carDto.userFirstName)
                && imagesUrl.equals(carDto.imagesUrl);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + brand.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + productYear;
        result = 31 * result + description.hashCode();
        result = 31 * result + mainImageUrl.hashCode();
        result = 31 * result + imagesUrl.hashCode();
        result = 31 * result + rate;
        result = 31 * result + userId.hashCode();
        result = 31 * result + userFirstName.hashCode();
        return result;
    }
}
