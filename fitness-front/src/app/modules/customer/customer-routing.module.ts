import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CartComponent } from './components/cart/cart.component';
import { MyOrdersComponent } from './components/my-orders/my-orders.component';
import { ViewOrderedProductsComponent } from './components/view-ordered-products/view-ordered-products.component';
import { ReviewOrderedProductsComponent } from './components/review-ordered-products/review-ordered-products.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';

const routes: Routes = [
  {path: 'dashboard', component: DashboardComponent},
  {path: 'cart', component: CartComponent},
  {path: 'profile', component: UserProfileComponent},
  {path: 'my_orders', component: MyOrdersComponent},
  {path: 'ordered_products/:orderId', component: ViewOrderedProductsComponent},
  {path: 'review/:productId', component: ReviewOrderedProductsComponent},
  {path: 'product_details/:productId', component: ProductDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule {
  


 }
