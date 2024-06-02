import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnouncementFormComponent } from './announcement-form.component';

describe('NewAnnouncementComponent', () => {
  let component: AnnouncementFormComponent;
  let fixture: ComponentFixture<AnnouncementFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnnouncementFormComponent]
    });
    fixture = TestBed.createComponent(AnnouncementFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
