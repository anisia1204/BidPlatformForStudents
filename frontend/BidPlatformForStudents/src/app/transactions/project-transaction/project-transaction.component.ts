import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {TransactionDtoModel} from "../domain/transaction-dto.model";
import {ProjectTransactionService} from "./project-transaction.service";
import {Subject, takeUntil} from "rxjs";
import {SkillVoModel} from "../../announcements/domain/skill-vo.model";
import {GoBackService} from "../../utils/go-back.service";
import {MessageService} from "primeng/api";
import {SkillStatusModel} from "../../announcements/domain/skill-status.model";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-project-transaction',
  templateUrl: './project-transaction.component.html',
  styleUrls: ['./project-transaction.component.css'],
  providers: [MessageService]
})
export class ProjectTransactionComponent implements OnInit, OnDestroy{
  projectId: number | undefined
  skillVos: SkillVoModel[] = []
  selectedSkillIds: number[] = []
  totalPrice: number = 0;
  destroy$: Subject<boolean> = new Subject<boolean>()
  projectTransactionService = inject(ProjectTransactionService)
  goBackService = inject(GoBackService)
  messageService = inject(MessageService)
  router = inject(Router)
  activatedRoute = inject(ActivatedRoute)
  ngOnInit(): void {
    this.projectId = history.state.id
    this.projectTransactionService
      .getSkillsByProjectId(this.projectId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(skills => {
        this.skillVos = skills
      })
  }
  onBuyProject() {
    const transactionDto = new TransactionDtoModel();
    transactionDto.announcementId = this.projectId
    transactionDto.skillIds = this.selectedSkillIds
    this.projectTransactionService.onBuyProject(transactionDto).subscribe(
      res => {
        this.router.navigate(['./'], {relativeTo: this.activatedRoute})
        this.messageService.add({ severity: 'info', summary: 'Success', detail: 'Anunt cumparat cu succes' })
      },
        error => {
          this.messageService.add({ severity: 'error', summary: 'Tranzactie esuata', detail: error.error.skillIds })
        }
    )
  }

  ngOnDestroy(): void {
    this.destroy$.next(true)
  }

  isSelected(skill: any): boolean {
    return this.selectedSkillIds.some(id => id === skill.id);
  }

  toggleItemSelection(skill: any) {
    const index = this.selectedSkillIds.findIndex(id => id === skill.id);
    if (index > -1) {
      this.selectedSkillIds.splice(index, 1);
      this.totalPrice = this.totalPrice - skill.skillPoints
    } else {
      this.selectedSkillIds.push(skill.id);
      this.totalPrice = this.totalPrice + skill.skillPoints
    }
  }

  protected readonly SkillStatusModel = SkillStatusModel;
}
