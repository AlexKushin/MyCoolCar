package com.mycoolcar.dtos;

public record CarEditDto(String brand,String model,int productYear,String description ) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarEditDto that)) return false;

        return productYear == that.productYear && brand.equals(that.brand) && model.equals(that.model) && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = brand.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + productYear;
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CarEditDto{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", productYear=" + productYear +
                ", description='" + description + '\'' +
                '}';
    }
}
