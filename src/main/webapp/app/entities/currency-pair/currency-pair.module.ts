import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ForeignExchangeSharedModule } from 'app/shared/shared.module';
import { CurrencyPairComponent } from './currency-pair.component';
import { CurrencyPairDetailComponent } from './currency-pair-detail.component';
import { CurrencyPairUpdateComponent } from './currency-pair-update.component';
import { CurrencyPairDeleteDialogComponent } from './currency-pair-delete-dialog.component';
import { currencyPairRoute } from './currency-pair.route';

@NgModule({
  imports: [ForeignExchangeSharedModule, RouterModule.forChild(currencyPairRoute)],
  declarations: [CurrencyPairComponent, CurrencyPairDetailComponent, CurrencyPairUpdateComponent, CurrencyPairDeleteDialogComponent],
  entryComponents: [CurrencyPairDeleteDialogComponent]
})
export class ForeignExchangeCurrencyPairModule {}
