export interface Notification {
  id: number;
  message: string;
  timestamp: string;
  userId: number;
  isRead: boolean;
  type: string;
}
