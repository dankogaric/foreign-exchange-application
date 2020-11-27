import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { ForeignExchangeTestModule } from '../../../test.module';
import { CurrencyPairComponent } from 'app/entities/currency-pair/currency-pair.component';
import { CurrencyPairService } from 'app/entities/currency-pair/currency-pair.service';
import { CurrencyPair } from 'app/shared/model/currency-pair.model';

describe('Component Tests', () => {
  describe('CurrencyPair Management Component', () => {
    let comp: CurrencyPairComponent;
    let fixture: ComponentFixture<CurrencyPairComponent>;
    let service: CurrencyPairService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ForeignExchangeTestModule],
        declarations: [CurrencyPairComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: {
                subscribe: (fn: (value: Data) => void) =>
                  fn({
                    pagingParams: {
                      predicate: 'id',
                      reverse: false,
                      page: 0
                    }
                  })
              }
            }
          }
        ]
      })
        .overrideTemplate(CurrencyPairComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CurrencyPairComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CurrencyPairService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CurrencyPair(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.currencyPairs && comp.currencyPairs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CurrencyPair(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.currencyPairs && comp.currencyPairs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
