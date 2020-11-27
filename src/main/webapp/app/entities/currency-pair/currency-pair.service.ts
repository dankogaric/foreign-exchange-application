import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICurrencyPair } from 'app/shared/model/currency-pair.model';

type EntityResponseType = HttpResponse<ICurrencyPair>;
type EntityArrayResponseType = HttpResponse<ICurrencyPair[]>;

@Injectable({ providedIn: 'root' })
export class CurrencyPairService {
  public resourceUrl = SERVER_API_URL + 'api/currency-pairs';

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

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICurrencyPair[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
