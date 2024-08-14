import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ViewappsService, Application } from './viewapps.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-viewapps',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './viewapps.component.html',
  styleUrl: './viewapps.component.scss'
})
export class ViewappsComponent implements OnInit {
  applications: Application[] = [];

  constructor(private viewappsService: ViewappsService) { }


  ngOnInit(): void {
    this.viewappsService.getApplications().subscribe(data => {
      this.applications = data;
    });
  }
  
  ngAfterViewInit(): void {
    setTimeout(() => {
      this.updateStatusColors();
    }, 7000); // Wait for 5 seconds before executing the function
  }

  updateStatusColors(): void {
    const statuses = document.getElementsByClassName('status');
    console.log("here update", statuses.length)
    // console.log(statuses)
    for (let i = 0; i < statuses.length; i++) {
      
      const statusElement = statuses[i];
      // console.log("here mfor loop", statusElement.textContent)
      if (statusElement.textContent === 'Approved') {
        
        statusElement.setAttribute('style', 'color: #4fc048');
      }
      else{
        statusElement.setAttribute('style', 'color: red');
      }
    }
  }
}
