import React, { useState } from 'react';
import GreetingTitle from '../components/GreetingTitle.jsx';
import GymSelector from '../components/GymSelector.jsx';
import WorkoutCard from '../components/WorkoutCard.jsx';
import gyms from '../data/mockedGyms';
import '../global.css'; // make sure this is imported

export default function HomePage() {
  const [selectedGym, setSelectedGym] = useState(gyms[0].name);
  const currentGym = gyms.find(g => g.name === selectedGym);

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ marginBottom: '30px' }}>
        <GreetingTitle />
      </div>

      <div className="layout-container">
        {/* Sidebar - Gym Selector */}
        <div style={{ minWidth: '200px' }}>
          <GymSelector
            gyms={gyms}
            selectedGym={selectedGym}
            onChange={(e) => setSelectedGym(e.target.value)}
          />
        </div>
        
        <div style={{ flexGrow: 1 }}>
          <h2>{selectedGym}</h2>
          <div style={{ display: 'flex', flexWrap: 'wrap' }}>
            {currentGym.workouts.map((workout, index) => (
              <WorkoutCard key={index} workout={workout} />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
