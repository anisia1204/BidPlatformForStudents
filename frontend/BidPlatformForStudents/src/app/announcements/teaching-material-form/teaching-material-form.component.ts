import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {FileUploadEvent} from "primeng/fileupload";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-teaching-material-form',
  templateUrl: './teaching-material-form.component.html',
  styleUrls: ['./teaching-material-form.component.scss'],
  providers: [MessageService]
})
export class TeachingMaterialFormComponent implements OnInit, OnDestroy{
  @Input() form: FormGroup | any;
  uploadedFiles: any[] = [];

  ngOnInit() {
  }

  onUpload($event: FileUploadEvent) {

  }

  ngOnDestroy(): void {
    this.form.reset(new FormGroup({}))
  }
}
