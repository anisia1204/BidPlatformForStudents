import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnouncementListFilterComponent } from './announcement-list-filter.component';

describe('AnnouncementListFilterComponent', () => {
  let component: AnnouncementListFilterComponent;
  let fixture: ComponentFixture<AnnouncementListFilterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnnouncementListFilterComponent]
    });
    fixture = TestBed.createComponent(AnnouncementListFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
