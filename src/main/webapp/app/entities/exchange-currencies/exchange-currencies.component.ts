import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICurrency } from '../../shared/model/currency.model';

import { ExchangeCurrenciesConfirmationDialogComponent } from './exchange-currencies-confirmation-dialog.component';
import { OrderService } from '../order/order.service';
import { ExchangeCurrenciesService } from '../exchange-currencies/exchange-currencies.service';
import { FormBuilder } from '@angular/forms';
import { map } from 'rxjs/operators';
import { IOrder, Order } from 'app/shared/model/order.model';
import { ICurrencyPair } from 'app/shared/model/currency-pair.model';
import * as moment from 'moment';
import { JhiEventManager } from 'ng-jhipster';
import { CurrencyPairService } from '../currency-pair/currency-pair.service';

@Component({
  selector: 'jhi-exchange-currencies',
  templateUrl: './exchange-currencies.component.html'
})
export class ExchangeCurrenciesComponent implements OnInit {
  isSaving = false;

  selectedCurrency: ICurrency = {};
  currenciesToBuy: ICurrency[] = [];
  currencyPairs: ICurrencyPair[] = [];
  creationDateDp: any;

  exchangeRate = 0;
  amountToBuy: any;
  amountToSell: any;
  surcharge = 0;
  surchargeAmount = 0;
  discount = 0;
  discountAmount = 0;
  lockFields = true;

  exchangeForm = this.fb.group({
    id: [],
    surchargePercentage: [],
    surchargeAmount: [],
    purchasedAmount: [],
    paidAmount: [],
    discountPercentage: [],
    discountAmount: [],
    exchangeRate: [],
    creationDate: [],
    purchasedCurrency: []
  });

  constructor(
    protected orderService: OrderService,
    protected currencyPairService: CurrencyPairService,
    protected exchangeCurrenciesService: ExchangeCurrenciesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected modalService: NgbModal,
    protected eventManager: JhiEventManager
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(() => {

      this.exchangeCurrenciesService.findForUSDollarSelling()
        .pipe(
          map((res: HttpResponse<ICurrencyPair[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICurrencyPair[]) => {
          this.currencyPairs = resBody;
          this.currencyPairs.forEach(currencyPair => {
            if (currencyPair.currencyToBuy)
              this.currenciesToBuy.push(currencyPair.currencyToBuy);
          })   
        });
      });
  }

  currencyChanged(): void {
    if (this.selectedCurrency?.id) {
      this.exchangeRate = this.currencyPairs[this.currenciesToBuy.indexOf(this.selectedCurrency)]!.exchangeRate || 0;
      this.surcharge = this.selectedCurrency.fee || 0;
      this.discount = this.selectedCurrency.discount || 0;
      this.lockFields = false;
    } else {
      this.amountToBuy = 0;
    }
    this.surchargeAmount = this.amountToBuy / this.exchangeRate * this.surcharge / 100;
    this.amountToSell = this.amountToBuy / this.exchangeRate * (100 - this.surcharge) / 100;     
    this.discountAmount = this.amountToSell * this.discount / 100; 
    this.amountToSell *= (100 - this.discount) / 100
  }

  amountToBuyChanged() : void {
    this.surchargeAmount = this.amountToBuy / this.exchangeRate * this.surcharge / 100;
    this.amountToSell = this.amountToBuy / this.exchangeRate * (100 - this.surcharge) / 100;     
    this.discountAmount = this.amountToSell * this.discount / 100; 
    this.amountToSell *= (100 - this.discount) / 100
  }

  refreshRates(): void {
    this.exchangeCurrenciesService.getLatestCurrencyRates().subscribe(res => {
        const exchangeRates = res?.body?.quotes;
        this.currencyPairs.forEach(currencyPair => {
          const exchangeRateName = currencyPair.name || "";
          currencyPair.exchangeRate = exchangeRates[exchangeRateName] || currencyPair.exchangeRate;      
          this.currencyPairService.update(currencyPair).subscribe(
            () => this.onSaveSuccess(),
            () => this.onSaveError()
          );
        });
    });
  }

  resetForm(): void {
    this.exchangeForm.patchValue({
      id: [],
      surchargePercentage: [],
      surchargeAmount: [],
      purchasedAmount: [],
      paidAmount: [],
      discountPercentage: [],
      discountAmount: [],
      exchangeRate: [],
      creationDate: [],
      purchasedCurrency: []
    });
  }

  confirmOrder(): void {
    this.isSaving = true;
    const modalRef = this.modalService.open(ExchangeCurrenciesConfirmationDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.order = this.createFromForm();
    modalRef.result.then((order: IOrder) =>  this.subscribeToSaveResponse(this.orderService.create(order)));
  }

  private createFromForm(): IOrder {
    return {
      ...new Order(),
      id: this.exchangeForm.get(['id'])!.value,
      surchargePercentage: this.surcharge,
      surchargeAmount: this.surchargeAmount,
      purchasedAmount: this.exchangeForm.get(['purchasedAmount'])!.value,
      paidAmount: this.exchangeForm.get(['paidAmount'])!.value,
      discountPercentage: this.discount,
      discountAmount: this.discountAmount,
      exchangeRate: this.exchangeRate,
      creationDate: moment(),
      purchasedCurrency: this.exchangeForm.get(['purchasedCurrency'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.resetForm();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICurrency): any {
    return item.id;
  }
}
