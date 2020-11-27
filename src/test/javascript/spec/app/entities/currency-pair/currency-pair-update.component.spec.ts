import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ForeignExchangeTestModule } from '../../../test.module';
import { CurrencyPairUpdateComponent } from 'app/entities/currency-pair/currency-pair-update.component';
import { CurrencyPairService } from 'app/entities/currency-pair/currency-pair.service';
import { CurrencyPair } from 'app/shared/model/currency-pair.model';

describe('Component Tests', () => {
  describe('CurrencyPair Management Update Component', () => {
    let comp: CurrencyPairUpdateComponent;
    let fixture: ComponentFixture<CurrencyPairUpdateComponent>;
    let service: CurrencyPairService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ForeignExchangeTestModule],
        declarations: [CurrencyPairUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CurrencyPairUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CurrencyPairUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CurrencyPairService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CurrencyPair(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CurrencyPair();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
