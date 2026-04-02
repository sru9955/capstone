export interface User {
  id: number;
  username: string;
  email: string;
  role: 'PATIENT' | 'DOCTOR' | 'RECEPTIONIST';
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  role: string;
  username: string;
  userId: number;
  message: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  name: string;
  phone?: string;
  address?: string;
  specialty?: string;
  availability?: string;
}
