export interface Appointment {
  prescription: string;
  id: number;
  patient: Patient;
  doctor: Doctor;
  appointmentTime: string;
  // STRICT TYPING: Only these uppercase statuses are allowed
  status: 'SCHEDULED' | 'PENDING' | 'COMPLETED' | 'CANCELLED' | 'RESCHEDULED';
  notes?: string;
  problem?: string;
  fee?: number;
  createdAt?: string;
}

export interface Patient {
  id: number;
  name: string;
  phone?: string;
  address?: string;
}

export interface Doctor {
  id: number;
  name: string;
  specialty: string;
  availability?: string;
  email?: string;
}

export interface AppointmentDto {
  appointmentTime: string;
  notes?: string;
  doctorId?: number;
  patientId?: number;
}

export interface PrescriptionItem {
  medicationName: string;
  dosage: string;
  frequency: string;
  route: string;
  purpose: string;
}

export interface MedicalRecord {
  id: number;
  patient: any; 
  doctor: any;
  appointment?: Appointment;
  age?: number;
  weight?: number;
  height?: number;
  bp?: string;
  sugarLevel?: number;
  symptoms?: string;
  diagnosis: string;
  prescription: PrescriptionItem[];
  allergies?: string;
  notes?: string;
  recordDate: string;
}

export interface MedicalRecordDto {
  age?: number;
  weight?: number;
  height?: number;
  bp?: string;
  sugarLevel?: number;
  symptoms?: string;
  diagnosis: string;
  prescription: PrescriptionItem[];
  allergies?: string;
  notes?: string;
}