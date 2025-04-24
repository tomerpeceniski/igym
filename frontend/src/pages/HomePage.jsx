import React, { useState } from 'react';
import GreetingTitle from '../components/GreetingTitle.jsx';
import GymSelector from '../components/GymSelector.jsx';
import WorkoutCard from '../components/WorkoutCard.jsx';
import gyms from '../data/mockedGyms';
import '../styles/global.css';

export default function HomePage() {
  const [selectedGym, setSelectedGym] = useState(gyms[0].name);
  const currentGym = gyms.find(g => g.name === selectedGym);

  return (
    <div>
      <div className="header-container">
        <GreetingTitle />
      </div>

      <div className="layout-container">
        <div style={{ minWidth: '200px' }}>
          <GymSelector
            gyms={gyms}
            selectedGym={selectedGym}
            onChange={(e) => setSelectedGym(e.target.value)}
          />
        </div>

        <div style={{ flexGrow: 1 }}>
          <h2 style={{ textAlign: 'center' }}>{selectedGym}</h2>
          <div className="workout-cards-container">
            {currentGym.workouts.map((workout, index) => (
              <WorkoutCard key={index} workout={workout} />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
