import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditAnnouncementComponent } from './edit-announcement.component';

describe('EditAnnouncementComponent', () => {
  let component: EditAnnouncementComponent;
  let fixture: ComponentFixture<EditAnnouncementComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditAnnouncementComponent]
    });
    fixture = TestBed.createComponent(EditAnnouncementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
