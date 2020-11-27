import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICurrencyPair, CurrencyPair } from 'app/shared/model/currency-pair.model';
import { CurrencyPairService } from './currency-pair.service';
import { CurrencyPairComponent } from './currency-pair.component';
import { CurrencyPairDetailComponent } from './currency-pair-detail.component';
import { CurrencyPairUpdateComponent } from './currency-pair-update.component';

@Injectable({ providedIn: 'root' })
export class CurrencyPairResolve implements Resolve<ICurrencyPair> {
  constructor(private service: CurrencyPairService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICurrencyPair> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((currencyPair: HttpResponse<CurrencyPair>) => {
          if (currencyPair.body) {
            return of(currencyPair.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CurrencyPair());
  }
}

export const currencyPairRoute: Routes = [
  {
    path: '',
    component: CurrencyPairComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'foreignExchangeApp.currencyPair.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CurrencyPairDetailComponent,
    resolve: {
      currencyPair: CurrencyPairResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'foreignExchangeApp.currencyPair.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CurrencyPairUpdateComponent,
    resolve: {
      currencyPair: CurrencyPairResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'foreignExchangeApp.currencyPair.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CurrencyPairUpdateComponent,
    resolve: {
      currencyPair: CurrencyPairResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'foreignExchangeApp.currencyPair.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
