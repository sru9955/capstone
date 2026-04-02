export interface DashboardStats {
  totalAppointments?: number;
  upcomingAppointments?: number;
  todayAppointments?: number;
  totalPatients?: number;
  pendingAppointments?: number;
  totalDoctors?: number;
  cancelledAppointments?: number;
  completedAppointments?: number;
}

export interface ChatMessage {
  text: string;
  type: 'user' | 'bot';
  timestamp: Date;
  inputType?: 'select' | 'date';
  options?: { label: string, value: any }[];
}
