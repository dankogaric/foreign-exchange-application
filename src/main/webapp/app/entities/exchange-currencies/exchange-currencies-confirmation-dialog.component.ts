import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrder } from 'app/shared/model/order.model';

@Component({
  templateUrl: './exchange-currencies-confirmation-dialog.component.html'
})
export class ExchangeCurrenciesConfirmationDialogComponent {
  order?: IOrder;

  constructor(public activeModal: NgbActiveModal) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmOrder(order: IOrder): void {
      this.activeModal.close(order);
  }
}
