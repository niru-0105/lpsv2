import { Routes } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SubmitloanComponent } from './submitloan/submitloan.component';
import { ViewappsComponent } from './viewapps/viewapps.component';
import { ViewApplicationComponent } from './view-application/view-application.component';

export const routes: Routes = [
    {
        path:'', 
        component: HomeComponent
    },
    {
        path:'submitloan',
        component: SubmitloanComponent
    },
    {
        path:'viewapps',
        component:ViewappsComponent
    },
    {
        path: 'viewapplication/:id',
        component:ViewApplicationComponent
    }
];
