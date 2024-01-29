import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeachingMaterialFormComponent } from './teaching-material-form.component';

describe('TeachingMaterialFormComponent', () => {
  let component: TeachingMaterialFormComponent;
  let fixture: ComponentFixture<TeachingMaterialFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeachingMaterialFormComponent]
    });
    fixture = TestBed.createComponent(TeachingMaterialFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
