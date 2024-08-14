import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ViewappsService } from '../viewapps/viewapps.service';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-view-application',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './view-application.component.html',
  styleUrl: './view-application.component.scss'
})
export class ViewApplicationComponent implements OnInit {
  application: any = {};
  declineRules: string[] = [];
  applicantAge!: number
  applicantSalary!: number
  applicantExp!: number

  constructor(
    private route: ActivatedRoute,
    private viewappsService: ViewappsService,
  ) {

  }

 
  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.viewappsService.getApplicationById(Number(id)).subscribe(data => {
        this.application = data;
      });
    }
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.updateStatusColors();
    }, 100); 
  }

  updateStatusColors(): void {
    const status = document.getElementById('status');
    if (status) {
      if (status.textContent === ' Approved') {
        status.setAttribute('style', 'color: #4fc048');
      } else {
        status.setAttribute('style', 'color: red');
      }
    }
  }
}