import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingPageComponent implements OnInit {
  healthTips = [
    { category: 'Diet', content: 'Drink at least 8 glasses of water daily for optimal organ functionality.' },
    { category: 'Fitness', content: 'Exercise for 30 minutes every day to maintain cardiovascular health.' },
    { category: 'Lifestyle', content: 'Get 7-8 hours of sleep each night to help your body recover.' },
    { category: 'Mental Health', content: 'Take 5 minutes daily for mindfulness or deep breathing exercises.' }
  ];

  doctors = [
    { name: 'Dr. Sarah Smith', specialty: 'Cardiologist', image: 'https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=150&h=150&fit=crop' },
    { name: 'Dr. John Doe', specialty: 'Neurologist', image: 'https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=150&h=150&fit=crop' },
    { name: 'Dr. Emily Chen', specialty: 'Pediatrician', image: 'https://images.unsplash.com/photo-1594824436951-7f12620ce501?w=150&h=150&fit=crop' }
  ];

  constructor() { }
  ngOnInit(): void { }
}
