import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpService } from '../../../core/services/http.service';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-doctor-add-record',
  template: `
    <div class="page-header fade-in">
      <div>
        <h1 class="page-title">Add Medical Record</h1>
        <p class="page-subtitle">Patient: {{patientName}}</p>
      </div>
    </div>

    <div class="card fade-in">
      <form [formGroup]="recordForm" (ngSubmit)="onSubmit()">
        <div class="form-grid">
          <div class="form-group">
            <label>Age</label>
            <input type="number" formControlName="age" class="form-control">
          </div>
          <div class="form-group">
            <label>Weight (kg)</label>
            <input type="number" formControlName="weight" class="form-control">
          </div>
          <div class="form-group">
            <label>Height (cm)</label>
            <input type="number" formControlName="height" class="form-control">
          </div>
          <div class="form-group">
            <label>Blood Pressure</label>
            <input type="text" formControlName="bp" class="form-control" placeholder="120/80">
          </div>
        </div>

        <div class="form-group">
          <label>Allergies</label>
          <input type="text" formControlName="allergies" class="form-control" placeholder="Any known allergies (Penicillin, Peanuts, etc.)">
        </div>

        <div class="form-group">
          <label>Symptoms</label>
          <textarea formControlName="symptoms" class="form-control" rows="3"></textarea>
        </div>

        <div class="form-group">
          <label>Diagnosis</label>
          <textarea formControlName="diagnosis" class="form-control" rows="3"></textarea>
        </div>

        <!-- PRESCRIPTION FORM ARRAY -->
        <div class="prescription-section">
          <div class="d-flex justify-content-between align-items-center mb-2">
            <label class="mb-0">Medication Prescription</label>
            <button type="button" class="btn btn-sm btn-outline-primary" (click)="addPrescriptionItem()">+ Add Medication</button>
          </div>
          
          <div formArrayName="prescription">
            <div class="prescription-item" *ngFor="let item of prescription.controls; let i=index" [formGroupName]="i">
              <div class="prescription-header">
                <span>Medication #{{i + 1}}</span>
                <button *ngIf="prescription.length > 1" type="button" class="btn btn-sm text-danger" (click)="removePrescriptionItem(i)">Remove</button>
              </div>
              <div class="form-grid">
                <div class="form-group">
                  <label>Name</label>
                  <input type="text" formControlName="medicationName" class="form-control" placeholder="e.g. Amoxicillin">
                </div>
                <div class="form-group">
                  <label>Dosage</label>
                  <input type="text" formControlName="dosage" class="form-control" placeholder="e.g. 500mg">
                </div>
                <div class="form-group">
                  <label>Frequency</label>
                  <input type="text" formControlName="frequency" class="form-control" placeholder="e.g. Twice Daily">
                </div>
                <div class="form-group">
                  <label>Route</label>
                  <input type="text" formControlName="route" class="form-control" placeholder="e.g. Oral">
                </div>
                <div class="form-group" style="grid-column: 1 / -1;">
                  <label>Purpose</label>
                  <input type="text" formControlName="purpose" class="form-control" placeholder="e.g. Infection">
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="form-group">
          <label>Additional Notes</label>
          <textarea formControlName="notes" class="form-control" rows="2"></textarea>
        </div>

        <div class="form-actions">
          <button type="button" class="btn btn-secondary" (click)="goBack()">Cancel</button>
          <button type="submit" class="btn btn-primary" [disabled]="recordForm.invalid || isLoading">
            {{isLoading ? 'Saving...' : 'Save Record'}}
          </button>
        </div>
      </form>
    </div>
  `,
  styles: [`
    .form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; margin-bottom: 1rem; }
    .btn { padding: 0.75rem 1.5rem; border-radius: 8px; font-weight: 600; cursor: pointer; transition: all 0.2s; border: none;}
    .btn-secondary { background: var(--bg-secondary); color: var(--text-primary); border: 1px solid var(--border-color); }
    .btn-primary { background: var(--primary-color); color: white; border: none; }
    .btn-sm { padding: 0.25rem 0.5rem; font-size: 0.85rem; }
    .btn-outline-primary { background: transparent; color: var(--primary-color); border: 1px solid var(--primary-color); }
    .text-danger { color: #ef4444; background: transparent; }
    .form-actions { display: flex; gap: 1rem; justify-content: flex-end; margin-top: 2rem; }
    
    .prescription-section { margin-top: 1.5rem; margin-bottom: 1.5rem; padding: 1.5rem; border: 1px solid var(--border-color); border-radius: 8px; background: #f8fafc; }
    .prescription-item { padding: 1rem; background: white; border: 1px solid #e2e8f0; border-radius: 6px; margin-bottom: 1rem; }
    .prescription-item:last-child { margin-bottom: 0; }
    .prescription-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; font-weight: 600; font-size: 0.9rem; color: var(--text-muted); }
    .d-flex { display: flex; } .justify-content-between { justify-content: space-between; } .align-items-center { align-items: center; } .mb-2 { margin-bottom: 0.5rem; } .mb-0 { margin-bottom: 0; }
  `]
})
export class DoctorAddRecordComponent implements OnInit {
  recordForm!: FormGroup;
  appointmentId!: number;
  patientName!: string;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpService,
    private alert: AlertService
  ) {}

  ngOnInit(): void {
    this.appointmentId = Number(this.route.snapshot.paramMap.get('id'));
    this.patientName = this.route.snapshot.queryParamMap.get('patientName') ?? 'Unknown Patient';
    
    this.recordForm = this.fb.group({
      age: [null],
      weight: [null],
      height: [null],
      bp: [''],
      allergies: [''],
      symptoms: ['', Validators.required],
      diagnosis: ['', Validators.required],
      prescription: this.fb.array([
        this.createPrescriptionItem()
      ]),
      notes: ['']
    });
  }

  get prescription(): FormArray {
    return this.recordForm.get('prescription') as FormArray;
  }

  createPrescriptionItem(): FormGroup {
    return this.fb.group({
      medicationName: ['', Validators.required],
      dosage: ['', Validators.required],
      frequency: ['', Validators.required],
      route: ['', Validators.required],
      purpose: ['', Validators.required]
    });
  }

  addPrescriptionItem(): void {
    this.prescription.push(this.createPrescriptionItem());
  }

  removePrescriptionItem(index: number): void {
    if (this.prescription.length > 1) {
      this.prescription.removeAt(index);
    }
  }

  onSubmit(): void {
    if (this.recordForm.invalid) {
      this.recordForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    this.http.addMedicalRecord(this.appointmentId, this.recordForm.value).subscribe({
      next: () => {
        this.alert.success('Medical record added successfully');
        this.router.navigate(['/doctor/appointments']);
      },
      error: () => { this.isLoading = false; }
    });
  }

  goBack(): void {
    this.router.navigate(['/doctor/appointments']);
  }
}
