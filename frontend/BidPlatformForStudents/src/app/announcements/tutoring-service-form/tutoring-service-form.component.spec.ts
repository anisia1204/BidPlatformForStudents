import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TutoringServiceFormComponent } from './tutoring-service-form.component';

describe('TutoringServiceFormComponent', () => {
  let component: TutoringServiceFormComponent;
  let fixture: ComponentFixture<TutoringServiceFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TutoringServiceFormComponent]
    });
    fixture = TestBed.createComponent(TutoringServiceFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
