import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'currency',
        loadChildren: () => import('./currency/currency.module').then(m => m.ForeignExchangeCurrencyModule)
      },
      {
        path: 'currency-pair',
        loadChildren: () => import('./currency-pair/currency-pair.module').then(m => m.ForeignExchangeCurrencyPairModule)
      },
      {
        path: 'order',
        loadChildren: () => import('./order/order.module').then(m => m.ForeignExchangeOrderModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ForeignExchangeEntityModule {}
