import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateUserPointsComponent } from './update-user-points.component';

describe('UpdateUserPointsComponent', () => {
  let component: UpdateUserPointsComponent;
  let fixture: ComponentFixture<UpdateUserPointsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateUserPointsComponent]
    });
    fixture = TestBed.createComponent(UpdateUserPointsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
