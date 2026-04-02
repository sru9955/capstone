import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpService } from '../../core/services/http.service';
import { MedicalRecord, PrescriptionItem } from '../../shared/models/appointment.model';

@Component({
  selector: 'app-prescription-print',
  templateUrl: './prescription-print.component.html',
  styleUrls: ['./prescription-print.component.scss']
})
export class PrescriptionPrintComponent implements OnInit {
  record: MedicalRecord | null = null;
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private http: HttpService
  ) { }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.http.getMedicalRecordById(Number(idParam)).subscribe({
        next: (data) => {
          this.record = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Failed to load record for printing', err);
          this.isLoading = false;
        }
      });
    }
  }

  printPrescription(): void {
    window.print();
  }
}
