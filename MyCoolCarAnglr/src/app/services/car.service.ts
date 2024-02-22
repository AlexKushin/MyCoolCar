import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Car} from "../models/car";
import {API_URL} from "../app.constants";

@Injectable({
  providedIn: 'root'
})
export class CarService {


  constructor(
    private http: HttpClient
  ) {
  }

  getTopCars() {
    return this.http.get<Car[]>(`${API_URL}/api/cars`);
  }

  addNewCar(formData: any){
    console.log(formData.data)
    return this.http.post(`${API_URL}/api/cars`, formData);
  }
}
