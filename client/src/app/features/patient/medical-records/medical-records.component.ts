import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../../core/services/http.service';
import { MedicalRecord } from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-medical-records',
  templateUrl: './medical-records.component.html',
  styleUrls: ['./medical-records.component.scss']
})
export class MedicalRecordsComponent implements OnInit {
  records: MedicalRecord[] = [];
  isLoading = true;

  constructor(private http: HttpService) {}

  ngOnInit(): void {
    this.loadRecords();
  }

  loadRecords(): void {
    this.isLoading = true;
    this.http.getMedicalRecords().subscribe({
      next: (data) => {
        this.records = data.reverse();
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }
}
