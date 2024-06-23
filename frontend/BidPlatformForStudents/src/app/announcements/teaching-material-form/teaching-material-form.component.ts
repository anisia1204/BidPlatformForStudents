import {Component, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {FileRemoveEvent, FileUploadHandlerEvent} from "primeng/fileupload";
import {MessageService} from "primeng/api";
import {Subject} from "rxjs";
import {AttachmentDtoModel} from "../domain/attachment-dto.model";

@Component({
  selector: 'app-teaching-material-form',
  templateUrl: './teaching-material-form.component.html',
  styleUrls: ['./teaching-material-form.component.css'],
  providers: [MessageService]
})
export class TeachingMaterialFormComponent implements OnInit, OnDestroy{
  @Input() form: FormGroup | any;
  @Input() attachmentDTOs: AttachmentDtoModel[] | any;
  localFiles: File[] = [];
  @Output() attachmentsUploaded: Subject<File[]> = new Subject<File[]>();
  @Output() deleteAttachmentDTO: Subject<number> = new Subject<number>();

  ngOnInit() {
    console.log(this.attachmentDTOs)
  }

  onRemove(event: FileRemoveEvent) {
    const removedFile: File = event.file;
    this.localFiles = this.localFiles.filter(file => file !== removedFile);
    this.attachmentsUploaded.next(this.localFiles)
  }

  uploadHandler(event: FileUploadHandlerEvent) {
    this.localFiles = event.files as File[]
    this.attachmentsUploaded.next(this.localFiles)
  }

  onRemoveAttachmentDTO(id: number) {
    this.deleteAttachmentDTO.next(id)
  }

  ngOnDestroy(): void {
    this.form.reset(new FormGroup({}))
  }
}
