import { ICurrency } from 'app/shared/model/currency.model';

export interface ICurrencyPair {
  id?: number;
  name?: string;
  exchangeRate?: number;
  currencyToSell?: ICurrency;
  currencyToBuy?: ICurrency;
}

export class CurrencyPair implements ICurrencyPair {
  constructor(
    public id?: number,
    public name?: string,
    public exchangeRate?: number,
    public currencyToSell?: ICurrency,
    public currencyToBuy?: ICurrency
  ) {}
}
