import React, { useState } from 'react';
import GreetingTitle from '../components/GreetingTitle.jsx';
import GymSelector from '../components/GymSelector.jsx';
import gyms from '../data/mockedGyms.js';

export default function HomePage() {
  const [selectedGym, setSelectedGym] = useState(gyms[0].name);

  return (
    <div style={{ padding: '20px' }}>
      <GreetingTitle />
      <GymSelector
        gyms={gyms}
        selectedGym={selectedGym}
        onChange={(e) => setSelectedGym(e.target.value)}
      />
    </div>
  );
}
