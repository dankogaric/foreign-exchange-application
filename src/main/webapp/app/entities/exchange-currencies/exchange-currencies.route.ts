import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ExchangeCurrenciesComponent } from './exchange-currencies.component';

export const exchangeCurrenciesRoute: Routes = [
  {
    path: '',
    component: ExchangeCurrenciesComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'foreignExchangeApp.exchange.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
