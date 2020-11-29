import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from '../../app.constants';
import { createRequestOption } from '../../shared/util/request-util';
import { ICurrencyPair } from '../../shared/model/currency-pair.model';

type EntityResponseType = HttpResponse<ICurrencyPair>;
type EntityArrayResponseType = HttpResponse<ICurrencyPair[]>;

@Injectable({ providedIn: 'root' })
export class ExchangeCurrenciesService {
  public resourceUrl = SERVER_API_URL + 'api/exchange';
  public exchangeRatesApiUrl = "http://api.currencylayer.com/live?access_key=6e4dc30463667f6738966cf11d1c7707&format=1";

  constructor(protected http: HttpClient) {}

  create(currencyPair: ICurrencyPair): Observable<EntityResponseType> {
    return this.http.post<ICurrencyPair>(this.resourceUrl, currencyPair, { observe: 'response' });
  }

  update(currencyPair: ICurrencyPair): Observable<EntityResponseType> {
    return this.http.put<ICurrencyPair>(this.resourceUrl, currencyPair, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICurrencyPair>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  
  findForUSDollarSelling(): Observable<EntityArrayResponseType> {
    return this.http.get<ICurrencyPair[]>(`${this.resourceUrl}/from-usdollars`, { observe: 'response' });
  }

  getLatestCurrencyRates(): Observable<any> {
    return this.http.get<any>(`${this.exchangeRatesApiUrl}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICurrencyPair[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
