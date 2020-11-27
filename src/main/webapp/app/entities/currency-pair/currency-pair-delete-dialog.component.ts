import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICurrencyPair } from 'app/shared/model/currency-pair.model';
import { CurrencyPairService } from './currency-pair.service';

@Component({
  templateUrl: './currency-pair-delete-dialog.component.html'
})
export class CurrencyPairDeleteDialogComponent {
  currencyPair?: ICurrencyPair;

  constructor(
    protected currencyPairService: CurrencyPairService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.currencyPairService.delete(id).subscribe(() => {
      this.eventManager.broadcast('currencyPairListModification');
      this.activeModal.close();
    });
  }
}
