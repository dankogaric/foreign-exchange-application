import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'exchange',
        loadChildren: () => import('./exchange-currencies/exchange-currencies.module').then(m => m.ExchangeCurrenciesModule)
      },
      {
        path: 'currency',
        loadChildren: () => import('./currency/currency.module').then(m => m.CurrencyModule)
      },
      {
        path: 'currency-pair',
        loadChildren: () => import('./currency-pair/currency-pair.module').then(m => m.CurrencyPairModule)
      },
      {
        path: 'order',
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ForeignExchangeEntityModule {}
