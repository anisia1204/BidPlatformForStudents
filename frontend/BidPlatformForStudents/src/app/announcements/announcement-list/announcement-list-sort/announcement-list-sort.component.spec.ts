import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnouncementListSortComponent } from './announcement-list-sort.component';

describe('AnnouncementListSortComponent', () => {
  let component: AnnouncementListSortComponent;
  let fixture: ComponentFixture<AnnouncementListSortComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnnouncementListSortComponent]
    });
    fixture = TestBed.createComponent(AnnouncementListSortComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
