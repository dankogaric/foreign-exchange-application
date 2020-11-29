import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ForeignExchangeSharedModule } from '../../shared/shared.module';
import { ExchangeCurrenciesComponent } from './exchange-currencies.component';
import { ExchangeCurrenciesConfirmationDialogComponent } from './exchange-currencies-confirmation-dialog.component';
import { exchangeCurrenciesRoute } from './exchange-currencies.route';

@NgModule({
  imports: [ForeignExchangeSharedModule, RouterModule.forChild(exchangeCurrenciesRoute)],
  declarations: [ExchangeCurrenciesComponent, ExchangeCurrenciesConfirmationDialogComponent],
  entryComponents: [ExchangeCurrenciesConfirmationDialogComponent]
})
export class ExchangeCurrenciesModule {}
