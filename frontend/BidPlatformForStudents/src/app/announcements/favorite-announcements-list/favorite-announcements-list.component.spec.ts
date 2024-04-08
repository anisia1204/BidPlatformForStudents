import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoriteAnnouncementsListComponent } from './favorite-announcements-list.component';

describe('FavoriteAnnouncementsListComponent', () => {
  let component: FavoriteAnnouncementsListComponent;
  let fixture: ComponentFixture<FavoriteAnnouncementsListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FavoriteAnnouncementsListComponent]
    });
    fixture = TestBed.createComponent(FavoriteAnnouncementsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
