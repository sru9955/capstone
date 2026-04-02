import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../core/services/http.service';
import { AlertService } from '../../core/services/alert.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  profileForm!: FormGroup;
  profileData: any;
  isLoading = true;
  isSaving = false;
  isUploading = false;
  role = '';

  imagePreview: string | null = null;

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private alert: AlertService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.role = this.auth.getRole() || '';
    this.initForm();
    this.loadProfile();
  }

  initForm(): void {
    const controls: any = {
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['']
    };

    if (this.role === 'DOCTOR') {
      controls['specialty'] = [''];
      controls['consultationFee'] = [''];
    }

    if (this.role === 'PATIENT') {
      controls['age'] = [''];
      controls['address'] = [''];
    }

    this.profileForm = this.fb.group(controls);
  }

  loadProfile(): void {
    this.http.getProfile().subscribe({
      next: (res) => {
        this.profileData = res;
        this.profileForm.patchValue(res);
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.alert.error('Failed to load profile');
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid) return;
    this.isSaving = true;
    this.http.updateProfile(this.profileForm.value).subscribe({
      next: (res) => {
        this.profileData = res;
        this.alert.success('Profile updated successfully');
        this.isSaving = false;
      },
      error: () => {
        this.alert.error('Failed to update profile');
        this.isSaving = false;
      }
    });
  }

  onImageUpload(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result as string;
    };
    reader.readAsDataURL(file);

    this.isUploading = true;
    this.http.uploadProfileImage(file).subscribe({
      next: (res: any) => {
        if(this.profileData) {
          this.profileData.profileImageUrl = res.url;
        }
        this.alert.success('Profile image updated');
        this.isUploading = false;
      },
      error: () => {
        this.alert.error('Failed to upload image');
        this.isUploading = false;
        this.imagePreview = null; // Revert on failure
      }
    });
  }
}
