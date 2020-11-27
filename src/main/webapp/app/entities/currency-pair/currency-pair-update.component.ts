import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ICurrencyPair, CurrencyPair } from 'app/shared/model/currency-pair.model';
import { CurrencyPairService } from './currency-pair.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/entities/currency/currency.service';

@Component({
  selector: 'jhi-currency-pair-update',
  templateUrl: './currency-pair-update.component.html'
})
export class CurrencyPairUpdateComponent implements OnInit {
  isSaving = false;

  currencies: ICurrency[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.maxLength(6)]],
    exchangeRate: [],
    currencyToSell: [],
    currencyToBuy: []
  });

  constructor(
    protected currencyPairService: CurrencyPairService,
    protected currencyService: CurrencyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currencyPair }) => {
      this.updateForm(currencyPair);

      this.currencyService
        .query()
        .pipe(
          map((res: HttpResponse<ICurrency[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICurrency[]) => (this.currencies = resBody));
    });
  }

  updateForm(currencyPair: ICurrencyPair): void {
    this.editForm.patchValue({
      id: currencyPair.id,
      name: currencyPair.name,
      exchangeRate: currencyPair.exchangeRate,
      currencyToSell: currencyPair.currencyToSell,
      currencyToBuy: currencyPair.currencyToBuy
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const currencyPair = this.createFromForm();
    if (currencyPair.id !== undefined) {
      this.subscribeToSaveResponse(this.currencyPairService.update(currencyPair));
    } else {
      this.subscribeToSaveResponse(this.currencyPairService.create(currencyPair));
    }
  }

  private createFromForm(): ICurrencyPair {
    return {
      ...new CurrencyPair(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      exchangeRate: this.editForm.get(['exchangeRate'])!.value,
      currencyToSell: this.editForm.get(['currencyToSell'])!.value,
      currencyToBuy: this.editForm.get(['currencyToBuy'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurrencyPair>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICurrency): any {
    return item.id;
  }
}
