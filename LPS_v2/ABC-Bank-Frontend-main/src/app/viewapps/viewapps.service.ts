import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Application {
  applicationID: number;
  name:{
    firstName:string;
  middleName:string,
  lastName:string,
  }
  
  submittedDate: Date;
  applicationStatus: string;
}

@Injectable({
  providedIn: 'root'
})
export class ViewappsService {
  private apiUrl = 'http://localhost:8080/applications'; // Replace with your actual backend URL

  constructor(private http: HttpClient) { }

  getApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(this.apiUrl);
  }
  getApplicationById(id: number): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/application/${id}`);
  }
}
