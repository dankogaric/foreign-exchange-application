import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IOrder, Order } from 'app/shared/model/order.model';
import { OrderService } from './order.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/entities/currency/currency.service';

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html'
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;

  currencies: ICurrency[] = [];
  creationDateDp: any;

  editForm = this.fb.group({
    id: [],
    surchargeAmount: [],
    purchasedAmount: [],
    paidAmount: [],
    discountPercentage: [],
    discountAmount: [],
    creationDate: [],
    purchasedCurrency: []
  });

  constructor(
    protected orderService: OrderService,
    protected currencyService: CurrencyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.updateForm(order);

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

  updateForm(order: IOrder): void {
    this.editForm.patchValue({
      id: order.id,
      surchargeAmount: order.surchargeAmount,
      purchasedAmount: order.purchasedAmount,
      paidAmount: order.paidAmount,
      discountPercentage: order.discountPercentage,
      discountAmount: order.discountAmount,
      creationDate: order.creationDate,
      purchasedCurrency: order.purchasedCurrency
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.createFromForm();
    if (order.id !== undefined) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  private createFromForm(): IOrder {
    return {
      ...new Order(),
      id: this.editForm.get(['id'])!.value,
      surchargeAmount: this.editForm.get(['surchargeAmount'])!.value,
      purchasedAmount: this.editForm.get(['purchasedAmount'])!.value,
      paidAmount: this.editForm.get(['paidAmount'])!.value,
      discountPercentage: this.editForm.get(['discountPercentage'])!.value,
      discountAmount: this.editForm.get(['discountAmount'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      purchasedCurrency: this.editForm.get(['purchasedCurrency'])!.value
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
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICurrency): any {
    return item.id;
  }
}
