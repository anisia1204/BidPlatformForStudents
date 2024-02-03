import { Injectable } from '@angular/core';
import {AbstractControl, FormArray, FormGroup, ValidatorFn} from "@angular/forms";
import {ProjectInputsComponent} from "../project-inputs/project-inputs.component";

@Injectable({
  providedIn: 'root'
})
export class NewAnnouncementValidatorHandlerServiceTsService {

  constructor() { }

  updateValidatorsForTutoringService(controls: FormGroup | null, validator: ValidatorFn) {
    if (controls) {
      this.getNameControlEntries(controls.controls)
        .filter(({name, control}) => name === 'subject' || name === 'startDate' || name === 'tutoringType')
        .forEach(({name, control}) => {
          control.setValidators([validator]);
          control.updateValueAndValidity();
        });
    }
  }

  updateValidatorsForProject(controls: FormGroup | null, validator: ValidatorFn, totalPoints: number | undefined, ) {
    if (controls) {
      this.getNameControlEntries(controls.controls)
        .forEach(({name, control}) => {
          if(!(control instanceof FormArray)){
            control.setValidators([validator]);
            control.updateValueAndValidity();
          }
          else {
            const formArray = control as FormArray;
            formArray.controls.forEach(group => {
              const formGroup = group as FormGroup;
              this.getNameControlEntries(formGroup.controls)
                .filter(({name, control}) => name === 'skill' || name === 'skillPoints')
                .forEach(({name, control}) => {
                  if(name === 'skillPoints') {
                    control.setValidators([validator, this.validatePointsSum.bind(ProjectInputsComponent, totalPoints, formArray)]);
                  }
                  else {
                    control.setValidators([validator]);
                  }
                  control.updateValueAndValidity();
                })
            })
          }
        });
    }
  }

  getNameControlEntries(controls: {[key: string]: AbstractControl<any, any>}) {
    return Object.entries(controls)
      .map(([name, control]) => ({
        name,
        control
      }))
  }

  clearValidatorsForTutoringService(controls: FormGroup | null) {
    if (controls) {
      Object.values(controls.controls).forEach((control) => {
        control.clearValidators();
        control.updateValueAndValidity();
      });
    }
  }
  clearValidatorsForProject(controls: FormGroup | null) {
    if (controls) {
      Object.values(controls.controls).forEach((control) => {
        if(!(control instanceof FormArray)){
          control.clearValidators();
          control.updateValueAndValidity();
        }
        else {
          const formArray = control as FormArray;
          formArray.controls.forEach(group => {
            const formGroup = group as FormGroup;
            Object.values(formGroup.controls).forEach(control => {
              control.clearValidators();
              control.updateValueAndValidity();
            })
          })
        }
      });
    }
  }

  validatePointsSum(totalPoints: number | undefined, requiredSkills: FormArray | undefined) {
    const pointsControl = totalPoints;

    if (pointsControl && requiredSkills) {
      const initialPoints = pointsControl;
      const requiredSkillsSum = requiredSkills.controls.reduce((sum, control) => sum + control.get('skillPoints')?.value, 0);

      console.log(requiredSkillsSum)

      if(requiredSkillsSum > initialPoints || (requiredSkillsSum < initialPoints && requiredSkillsSum != 0)){
        return {pointsSumExceeded: true};
      }
      else {
        requiredSkills.controls.map(group => group.get('skillPoints')).forEach(skillPoints => skillPoints?.setErrors(null));
        return null;
      }

    }

    return null;
  }
}
