import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpService } from '../../../core/services/http.service';
import { MedicalRecord } from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-doctor-patient-history',
  template: `
    <div class="page-header fade-in">
      <div>
        <h1 class="page-title">Patient Medical History</h1>
        <p class="page-subtitle">Historical records for Patient ID: {{patientId}}</p>
      </div>
      <div style="display: flex; gap: 1rem;">
        <button class="btn btn-outline" style="padding: 0.5rem 1rem; border: 1px solid #ccc; border-radius: 6px; background: transparent; cursor: pointer;" (click)="goBack()">Back to Appointments</button>
      </div>
    </div>

    <div class="history-list fade-in">
      <div *ngFor="let record of history; let i = index" class="card history-card" [style.animation-delay]="(i * 0.1) + 's'">
        <div class="history-header">
          <span class="record-date">{{record.recordDate | date:'medium'}}</span>
          <span class="doctor-name">Dr. {{record.doctor.name}} ({{record.doctor.specialty}})</span>
        </div>
        
        <div class="history-grid">
          <div class="history-item">
            <label>Symptoms</label>
            <p>{{record.symptoms || '-'}}</p>
          </div>
          <div class="history-item">
            <label>Diagnosis</label>
            <p class="important">{{record.diagnosis || '-'}}</p>
          </div>
        </div>

        <div class="history-item">
          <label>Prescription</label>
          <div *ngIf="record.prescription && record.prescription.length > 0; else noPrescription" class="code-block" style="padding: 0;">
            <table style="width: 100%; border-collapse: collapse; font-family: 'Inter', sans-serif;">
              <tr *ngFor="let item of record.prescription" style="border-bottom: 1px solid #e2e8f0;">
                <td style="padding: 0.5rem; font-weight: 600;">{{item.medicationName}}</td>
                <td style="padding: 0.5rem;">{{item.dosage}} ({{item.frequency}})</td>
              </tr>
            </table>
          </div>
          <ng-template #noPrescription>
            <p class="code-block" style="font-family: inherit;">No prescription recorded</p>
          </ng-template>
        </div>

        <div class="history-item" *ngIf="record.notes">
          <label>Notes</label>
          <p>{{record.notes}}</p>
        </div>
        
        <div style="margin-top: 1rem; text-align: right;">
          <button class="btn btn-primary btn-sm" style="padding: 0.5rem 1rem; border: none; border-radius: 6px; background: var(--primary-color); color: white; cursor: pointer;" [routerLink]="['/print/prescription', record.id]">
            🖨️ Print Prescription
          </button>
        </div>
      </div>

      <div *ngIf="history.length === 0" class="text-center no-history card">
        <p>No previous history found for this patient.</p>
      </div>
    </div>
  `,
  styles: [`
    .history-card { margin-bottom: 2rem; border-left: 5px solid var(--primary-color); }
    .history-header { display: flex; justify-content: space-between; margin-bottom: 1.5rem; color: var(--text-muted); }
    .record-date { font-weight: 700; color: var(--primary-color); }
    .history-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; margin-bottom: 1rem; }
    .history-item label { display: block; font-size: 0.875rem; color: var(--text-muted); margin-bottom: 0.5rem; text-transform: uppercase; letter-spacing: 0.05em; }
    .history-item p { margin: 0; line-height: 1.6; }
    .important { font-weight: 600; color: var(--text-primary); }
    .code-block { background: var(--bg-secondary); padding: 1rem; border-radius: 8px; font-family: monospace; white-space: pre-wrap; }
    .no-history { padding: 4rem; color: var(--text-muted); }
  `]
})
export class DoctorPatientHistoryComponent implements OnInit {
  patientId!: number;
  history: MedicalRecord[] = [];
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpService
  ) {}

  ngOnInit(): void {
    this.patientId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadHistory();
  }

  loadHistory(): void {
    this.http.getPatientHistory(this.patientId).subscribe({
      next: (data) => {
        this.history = data.reverse();
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  goBack(): void {
    this.router.navigate(['/doctor/appointments']);
  }
}
