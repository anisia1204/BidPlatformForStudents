import {Component, inject, ViewChild} from '@angular/core';
import {NgxScannerQrcodeComponent, ScannerQRCodeResult} from "ngx-scanner-qrcode";
import {Router} from "@angular/router";

@Component({
  selector: 'app-qr-code-scanner',
  templateUrl: './qr-code-scanner.component.html',
  styleUrls: ['./qr-code-scanner.component.css'],
})
export class QrCodeScannerComponent {
  @ViewChild('action') action: NgxScannerQrcodeComponent | undefined;
  router = inject(Router)
  handleQRCodeResult(event: ScannerQRCodeResult[]) {
    const userId = event[0].value
    this.action?.stop()
    this.router.navigate(['update', userId])
  }
}
