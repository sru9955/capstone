import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-landing', // Fixed selector
  templateUrl: './landing.component.html', // Fixed filename!
  styleUrls: ['./landing.component.scss'] // Fixed filename!
})
export class LandingPageComponent implements OnInit, OnDestroy {
  // Chatbot toggle
  isChatOpen = false;

  // Carousel variables
  currentSlide = 0;
  autoPlayInterval: any;

  healthTips = [
    {
      image: 'https://images.unsplash.com/photo-1523362628745-0c100150b504?auto=format&fit=crop&w=800&q=80',
      title: 'Stay Hydrated',
      description: 'Drink at least 8 glasses of water daily to maintain energy and flush out toxins.'
    },
    {
      image: 'https://images.unsplash.com/photo-1490645935967-10de6ba17061?auto=format&fit=crop&w=800&q=80',
      title: 'Balanced Diet',
      description: 'Incorporate a colorful variety of vegetables and lean proteins into every meal.'
    },
    {
      image: 'https://images.unsplash.com/photo-1534438327276-14e5300c3a48?auto=format&fit=crop&w=800&q=80',
      title: 'Daily Movement',
      description: 'Aim for at least 30 minutes of moderate physical activity every single day.'
    },
    {
      image: 'https://images.unsplash.com/photo-1506126613408-eca07ce68773?auto=format&fit=crop&w=800&q=80',
      title: 'Mental Wellness',
      description: 'Take 10 minutes daily to unplug, breathe deeply, and practice mindfulness.'
    },
    {
      image: 'https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?auto=format&fit=crop&w=800&q=80',
      title: 'Quality Sleep',
      description: 'Ensure 7-9 hours of uninterrupted sleep to let your body repair and recharge.'
    },
    {
      image: 'https://images.unsplash.com/photo-1579684385127-1ef15d508118?auto=format&fit=crop&w=800&q=80',
      title: 'Regular Checkups',
      description: 'Prevention is key. Don\'t skip your annual exams and routine screenings.'
    }
  ];

  ngOnInit() {
    this.startAutoPlay();
  }

  ngOnDestroy() {
    this.stopAutoPlay();
  }

  // Opens and closes the chatbot component
  toggleChat() {
    this.isChatOpen = !this.isChatOpen;
  }

  // Carousel Logic
  startAutoPlay() {
    this.autoPlayInterval = setInterval(() => {
      this.nextSlide();
    }, 5000); 
  }

  stopAutoPlay() {
    if (this.autoPlayInterval) {
      clearInterval(this.autoPlayInterval);
    }
  }

  nextSlide() {
    this.currentSlide = (this.currentSlide + 1) % this.healthTips.length;
  }

  prevSlide() {
    this.currentSlide = (this.currentSlide - 1 + this.healthTips.length) % this.healthTips.length;
  }

  goToSlide(index: number) {
    this.currentSlide = index;
    this.stopAutoPlay(); 
    this.startAutoPlay(); 
  }
}