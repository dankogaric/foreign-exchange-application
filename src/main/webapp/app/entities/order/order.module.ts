import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ForeignExchangeSharedModule } from 'app/shared/shared.module';
import { OrderComponent } from './order.component';
import { OrderDetailComponent } from './order-detail.component';
import { OrderDeleteDialogComponent } from './order-delete-dialog.component';
import { orderRoute } from './order.route';

@NgModule({
  imports: [ForeignExchangeSharedModule, RouterModule.forChild(orderRoute)],
  declarations: [OrderComponent, OrderDetailComponent, OrderDeleteDialogComponent],
  entryComponents: [OrderDeleteDialogComponent]
})
export class OrderModule {}
