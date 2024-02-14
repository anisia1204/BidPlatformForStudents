import {Component, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {FileRemoveEvent, FileSelectEvent, FileUploadEvent, FileUploadHandlerEvent} from "primeng/fileupload";
import {MessageService} from "primeng/api";
import {Subject} from "rxjs";

@Component({
  selector: 'app-teaching-material-form',
  templateUrl: './teaching-material-form.component.html',
  styleUrls: ['./teaching-material-form.component.scss'],
  providers: [MessageService]
})
export class TeachingMaterialFormComponent implements OnInit, OnDestroy{
  @Input() form: FormGroup | any;
  localFiles: File[] = [];
  @Output() attachmentsUploaded: Subject<File[]> = new Subject<File[]>();

  ngOnInit() {
    console.log(this.form.value)
  }

  onSelect(event: FileSelectEvent) {
    console.log(event)
    this.localFiles = event.currentFiles;
    this.form.patchValue({ attachmentDTOs: this.localFiles });
    console.log(this.form.get('attachmentDTOs').value)
  }

  ngOnDestroy(): void {
    this.form.reset(new FormGroup({}))
  }

  onRemove($event: FileRemoveEvent) {

  }

  uploadHandler(event: FileUploadHandlerEvent) {
    this.localFiles = event.files as File[]
    this.attachmentsUploaded.next(this.localFiles)
  }
}
