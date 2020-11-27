import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ForeignExchangeTestModule } from '../../../test.module';
import { CurrencyPairDetailComponent } from 'app/entities/currency-pair/currency-pair-detail.component';
import { CurrencyPair } from 'app/shared/model/currency-pair.model';

describe('Component Tests', () => {
  describe('CurrencyPair Management Detail Component', () => {
    let comp: CurrencyPairDetailComponent;
    let fixture: ComponentFixture<CurrencyPairDetailComponent>;
    const route = ({ data: of({ currencyPair: new CurrencyPair(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ForeignExchangeTestModule],
        declarations: [CurrencyPairDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CurrencyPairDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CurrencyPairDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load currencyPair on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.currencyPair).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
