import { Moment } from 'moment';
import { ICurrency } from 'app/shared/model/currency.model';

export interface IOrder {
  id?: number;
  surchargeAmount?: number;
  purchasedAmount?: number;
  paidAmount?: number;
  discountPercentage?: number;
  discountAmount?: number;
  creationDate?: Moment;
  purchasedCurrency?: ICurrency;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public surchargeAmount?: number,
    public purchasedAmount?: number,
    public paidAmount?: number,
    public discountPercentage?: number,
    public discountAmount?: number,
    public creationDate?: Moment,
    public purchasedCurrency?: ICurrency
  ) {}
}
