import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectTransactionComponent } from './project-transaction.component';

describe('ProjectTransactionComponent', () => {
  let component: ProjectTransactionComponent;
  let fixture: ComponentFixture<ProjectTransactionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProjectTransactionComponent]
    });
    fixture = TestBed.createComponent(ProjectTransactionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
