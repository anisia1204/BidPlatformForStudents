import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavouritesListComponent } from './favourites-list.component';

describe('FavouritesListComponent', () => {
  let component: FavouritesListComponent;
  let fixture: ComponentFixture<FavouritesListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FavouritesListComponent]
    });
    fixture = TestBed.createComponent(FavouritesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
