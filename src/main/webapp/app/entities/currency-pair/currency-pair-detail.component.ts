import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICurrencyPair } from 'app/shared/model/currency-pair.model';

@Component({
  selector: 'jhi-currency-pair-detail',
  templateUrl: './currency-pair-detail.component.html'
})
export class CurrencyPairDetailComponent implements OnInit {
  currencyPair: ICurrencyPair | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currencyPair }) => {
      this.currencyPair = currencyPair;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
