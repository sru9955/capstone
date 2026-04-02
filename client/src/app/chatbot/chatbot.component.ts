import { Component, OnInit } from '@angular/core';
import { HttpService } from '../core/services/http.service';
import { ChatMessage } from '../shared/models/dashboard.model';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.scss']
})
export class ChatbotComponent implements OnInit {
  isOpen = false;
  messageText = '';
  messages: ChatMessage[] = [];
  isTyping = false;
  isLoggedIn = false;

  conversationState = 'IDLE';
  pendingData: any = {};
  availableDoctors: any[] = [];

  constructor(private http: HttpService, private auth: AuthService) {}

  ngOnInit(): void {
    this.auth.currentUser$.subscribe(user => {
      const wasLoggedIn = this.isLoggedIn;
      this.isLoggedIn = !!user;
      
      if (user || wasLoggedIn) {
        this.messages = [];
        this.conversationState = 'IDLE';
        this.pendingData = {};
        
        if (this.isLoggedIn) {
          this.addBotMessage(this.getGreeting(user));
        }
      }
    });
  }

  private getGreeting(user: any): string {
    const name = user?.username || 'there';
    let base = `Hi ${name}! I'm your healthcare assistant. `;
    
    if (this.auth.isPatient()) {
      return base + "I can help you **book an appointment**, check doctor availability, or view your medical records. What would you like to do?";
    } else if (this.auth.isDoctor()) {
      return base + "I can help you **update your availability** or view your upcoming appointments. How can I assist you today?";
    } else if (this.auth.isReceptionist()) {
      return base + "I can help you **book an appointment for a patient** or oversee all system appointments. How can I help?";
    }
    return base + "How can I help you today?";
  }

  toggleChat(): void {
    if (!this.isLoggedIn) return;
    this.isOpen = !this.isOpen;
  }

  sendMessage(): void {
    if (!this.messageText.trim()) return;

    const text = this.messageText;
    this.messages.push({ text, type: 'user', timestamp: new Date() });
    this.messageText = '';
    this.isTyping = true;
    this.scrollToBottom();

    const input = text.toLowerCase().trim();

    // Cancel mechanism
    if (input === 'cancel' || input === 'stop' || input === 'exit') {
        if (this.conversationState !== 'IDLE') {
            this.conversationState = 'IDLE';
            this.pendingData = {};
            this.isTyping = false;
            this.addBotMessage('Got it, I cancelled that action. How else can I assist you?');
            return;
        }
    }

    // Stateful processing
    if (this.conversationState !== 'IDLE') {
        this.handleStatefulResponse(text);
        this.isTyping = false;
        return;
    }

    // Checking for Intents
    if (this.auth.isPatient() && (input.includes('book') || input.includes('schedule')) && input.includes('appointment')) {
       this.conversationState = 'PATIENT_BOOKING_SYMPTOMS';
       this.pendingData = {};
       this.isTyping = false;
       this.messages.push({
           text: "What symptoms are you experiencing?",
           type: 'bot',
           timestamp: new Date(),
           options: [
               { label: 'Fever / Cough', value: 'Fever / Cough' },
               { label: 'Headache', value: 'Headache' },
               { label: 'Body Pain', value: 'Body Pain' },
               { label: 'General Checkup', value: 'General Checkup' }
           ]
       });
       this.scrollToBottom();
       return;
    }
    
    if (this.auth.isReceptionist() && (input.includes('book') || input.includes('schedule')) && input.includes('appointment')) {
       this.conversationState = 'RECEPTIONIST_BOOKING_PATIENT';
       this.pendingData = {};
       this.isTyping = false;
       this.addBotMessage("Let's book an appointment for a patient. Please enter the **numeric ID** of the Patient:");
       return;
    }
    
    if (this.auth.isDoctor() && input.includes('availability') && (input.includes('update') || input.includes('change') || input.includes('set'))) {
       this.conversationState = 'DOCTOR_AVAILABILITY_TEXT';
       this.pendingData = {};
       this.isTyping = false;
       this.addBotMessage("Please enter your new availability schedule (e.g., 'Mon-Fri 9AM-5PM'):");
       return;
    }

    // Default: Fallback to Backend AI
    this.http.sendChatMessage(text, this.auth.getRole()).subscribe({
      next: (res) => {
        this.addBotMessage(res.reply);
        this.isTyping = false;
      },
      error: () => {
        this.addBotMessage('Sorry, I am having trouble connecting to the server. Please try again later.');
        this.isTyping = false;
      }
    });
  }

  handleOptionSelect(option: any, parentMsg: ChatMessage): void {
      parentMsg.options = []; // Clear options to prevent re-click
      this.messages.push({ text: option.label, type: 'user', timestamp: new Date() });
      this.isTyping = true;
      this.scrollToBottom();
      setTimeout(() => {
          this.handleStatefulResponse(option.value);
          this.isTyping = false;
      }, 500);
  }

  handleDateSelect(value: string, parentMsg: ChatMessage): void {
      if (!value) return;
      parentMsg.inputType = undefined;
      this.messages.push({ text: value.replace('T', ' '), type: 'user', timestamp: new Date() });
      this.isTyping = true;
      this.scrollToBottom();
      setTimeout(() => {
          this.handleStatefulResponse(value);
          this.isTyping = false;
      }, 500);
  }

  private handleStatefulResponse(text: string): void {
      // -- PATIENT BOOKING --
      if (this.conversationState === 'PATIENT_BOOKING_SYMPTOMS') {
         this.pendingData.notes = text;
         this.conversationState = 'PATIENT_BOOKING_DOCTOR';
         this.fetchDoctorsAndPrompt();
         return;
      }
      if (this.conversationState === 'PATIENT_BOOKING_DOCTOR') {
         this.pendingData.doctorId = Number(text);
         if (isNaN(this.pendingData.doctorId)) {
             this.fetchDoctorsAndPrompt("That doesn't look valid. Please select a doctor:");
             return;
         }
         this.conversationState = 'PATIENT_BOOKING_TIME';
         this.messages.push({
             text: "Great. Now please select your preferred **Date & Time**:",
             type: 'bot',
             timestamp: new Date(),
             inputType: 'date'
         });
         this.scrollToBottom();
         return;
      }
      if (this.conversationState === 'PATIENT_BOOKING_TIME') {
         this.pendingData.time = text.replace('T', ' ');
         if (this.pendingData.time.split(':').length === 2) {
             this.pendingData.time += ':00';
         }
         this.conversationState = 'IDLE';
         this.addBotMessage("Booking your appointment now...");
         
         const payload = {
            appointmentTime: this.pendingData.time,
            notes: this.pendingData.notes
         };
         this.http.scheduleAppointment(this.pendingData.doctorId, payload).subscribe({
            next: () => this.addBotMessage("✅ Appointment booked successfully!"),
            error: () => this.addBotMessage("❌ Failed to book appointment. Please make sure the Date & Time format is correct, and the Doctor ID exists.")
         });
         return;
      }
      
      // -- RECEPTIONIST BOOKING --
      if (this.conversationState === 'RECEPTIONIST_BOOKING_PATIENT') {
         this.pendingData.patientId = Number(text);
         if (isNaN(this.pendingData.patientId)) {
             this.addBotMessage("Invalid number. Please enter a valid numeric Patient ID:");
             return;
         }
         this.conversationState = 'RECEPTIONIST_BOOKING_DOCTOR';
         this.fetchDoctorsAndPrompt("OK. Now please select a **Doctor**:");
         return;
      }
      if (this.conversationState === 'RECEPTIONIST_BOOKING_DOCTOR') {
         this.pendingData.doctorId = Number(text);
         this.conversationState = 'RECEPTIONIST_BOOKING_TIME';
         this.messages.push({
             text: "Great. Now please select a **Date & Time**:",
             type: 'bot',
             timestamp: new Date(),
             inputType: 'date'
         });
         this.scrollToBottom();
         return;
      }
      if (this.conversationState === 'RECEPTIONIST_BOOKING_TIME') {
         this.pendingData.time = text.replace('T', ' ');
         if (this.pendingData.time.split(':').length === 2) {
             this.pendingData.time += ':00';
         }
         this.conversationState = 'RECEPTIONIST_BOOKING_NOTES';
         this.addBotMessage("Any **notes**? (Type 'none' if none):");
         return;
      }
      if (this.conversationState === 'RECEPTIONIST_BOOKING_NOTES') {
         this.pendingData.notes = text.toLowerCase() === 'none' ? '' : text;
         this.conversationState = 'IDLE';
         this.addBotMessage("Booking appointment in the background...");
         
         const payload = {
            patientId: this.pendingData.patientId,
            doctorId: this.pendingData.doctorId,
            appointmentTime: this.pendingData.time,
            notes: this.pendingData.notes
         };
         this.http.scheduleAppointmentByReceptionist(payload).subscribe({
            next: () => this.addBotMessage("✅ Appointment booked successfully!"),
            error: () => this.addBotMessage("❌ Failed to book appointment. Please check IDs and Time format.")
         });
         return;
      }
      
      // -- DOCTOR AVAILABILITY --
      if (this.conversationState === 'DOCTOR_AVAILABILITY_TEXT') {
         this.conversationState = 'IDLE';
         this.addBotMessage("Updating your availability schedule...");
         this.http.updateDoctorAvailability(text).subscribe({
            next: () => this.addBotMessage("✅ Availability updated to: " + text),
            error: () => this.addBotMessage("❌ Failed to update availability.")
         });
         return;
      }
  }

  private fetchDoctorsAndPrompt(msg = "Please select a Doctor from the list:") {
      this.http.getDoctors().subscribe({
          next: (docs) => {
              if (docs.length === 0) {
                  this.addBotMessage("Sorry, there are no doctors available right now.");
                  this.conversationState = 'IDLE';
                  return;
              }
              const opts = docs.map(d => ({ label: `Dr. ${d.name} (${d.specialty})`, value: d.id }));
              this.messages.push({
                  text: msg,
                  type: 'bot',
                  timestamp: new Date(),
                  options: opts
              });
              this.scrollToBottom();
          },
          error: () => {
              this.addBotMessage("Failed to load doctors list.");
              this.conversationState = 'IDLE';
          }
      });
  }

  private addBotMessage(text: string): void {
    this.messages.push({ text, type: 'bot', timestamp: new Date() });
    this.scrollToBottom();
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const container = document.querySelector('.chat-messages');
      if (container) {
        container.scrollTop = container.scrollHeight;
      }
    }, 100);
  }
}
