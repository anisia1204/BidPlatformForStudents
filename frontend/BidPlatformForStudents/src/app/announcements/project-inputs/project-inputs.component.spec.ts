import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectInputsComponent } from './project-inputs.component';

describe('ProjectInputsComponent', () => {
  let component: ProjectInputsComponent;
  let fixture: ComponentFixture<ProjectInputsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProjectInputsComponent]
    });
    fixture = TestBed.createComponent(ProjectInputsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
