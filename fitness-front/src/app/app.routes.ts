import { Routes } from '@angular/router';
import { SignupComponent } from './components/signup/signup.component';
import { SigninComponent } from './components/signin/signin.component';
import { TrackOrderComponent } from './components/track-order/track-order.component';

export const routes: Routes = [
    {path: 'signup', component: SignupComponent},
    {path: 'signin', component: SigninComponent},
    {path: 'order', component: TrackOrderComponent},
    {path: 'admin', loadChildren: ()=>import("./modules/admin/admin.module")
        .then(e=>e.AdminModule)
    },
    {path: 'customer', loadChildren: ()=>import("./modules/customer/customer.module")
        .then(e=>e.CustomerModule)
    }
    
]
