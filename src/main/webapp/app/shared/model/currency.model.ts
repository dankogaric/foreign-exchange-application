import { IOrder } from 'app/shared/model/order.model';

export interface ICurrency {
  id?: number;
  abbreviation?: string;
  name?: string;
  country?: string;
  fee?: number;
  discount?: number;
  orders?: IOrder[];
}

export class Currency implements ICurrency {
  constructor(
    public id?: number,
    public abbreviation?: string,
    public name?: string,
    public country?: string,
    public fee?: number,
    public discount?: number,
    public orders?: IOrder[]
  ) {}
}
