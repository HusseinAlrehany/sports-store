import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PostCategoryComponent } from './components/post-category/post-category.component';
import { PostProductComponent } from './components/post-product/post-product.component';
import { AddCouponComponent } from './components/add-coupon/add-coupon.component';
import { CouponsComponent } from './components/coupons/coupons.component';
import { OrdersComponent } from './components/orders/orders.component';
import { PostFaqComponent } from './components/post-faq/post-faq.component';
import { UpdateProductComponent } from './components/update-product/update-product.component';
import { AnalyticsComponent } from './components/analytics/analytics.component';

const routes: Routes = [
  {path: 'dashboard', component: DashboardComponent},
  {path: 'category', component: PostCategoryComponent},
  {path: 'product', component: PostProductComponent},
  {path: 'add-coupon', component: AddCouponComponent},
  {path: 'coupons', component: CouponsComponent},
  {path: 'orders', component: OrdersComponent},
  {path: 'analytics', component: AnalyticsComponent},
  {path: 'faq/:productId', component: PostFaqComponent},
  {path: 'product/:productId', component: UpdateProductComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
