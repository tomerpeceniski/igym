const gyms = [
    {
      name: 'Gym A',
      workouts: [
        {
          name: 'Workout Alpha',
          exercises: [
            { name: 'Flat Bench Press', weight: 60, repetitions: 10, sets: 4, note: 'Watch your posture' },
            { name: 'Incline Bench Press', weight: 50, repetitions: 12, sets: 3, note: null },
            { name: 'Chest Fly', weight: 30, repetitions: 15, sets: 3, note: 'Focus on the stretch' },
            { name: 'Pec Deck Machine', weight: 40, repetitions: 10, sets: 4, note: null },
            { name: 'Push-ups', weight: 0, repetitions: 20, sets: 3, note: 'Go slow and controlled' },
          ]
        },
        {
          name: 'Workout Beta',
          exercises: [
            { name: 'Barbell Squat', weight: 80, repetitions: 8, sets: 4, note: 'Reach parallel depth' },
            { name: 'Leg Press', weight: 120, repetitions: 10, sets: 4, note: null },
            { name: 'Leg Extension', weight: 45, repetitions: 12, sets: 3, note: null },
            { name: 'Leg Curl', weight: 40, repetitions: 12, sets: 3, note: null },
            { name: 'Lunges', weight: 20, repetitions: 10, sets: 3, note: 'Alternate legs' },
          ]
        }
      ]
    },
    {
      name: 'Gym B',
      workouts: [
        {
          name: 'Workout Gamma',
          exercises: [
            { name: 'Bent Over Row', weight: 50, repetitions: 10, sets: 4, note: null },
            { name: 'Lat Pulldown', weight: 45, repetitions: 12, sets: 3, note: 'Hold 1s at the top' },
            { name: 'Single Arm Row', weight: 30, repetitions: 10, sets: 3, note: null },
            { name: 'Pull-ups', weight: 0, repetitions: 8, sets: 3, note: 'Use resistance band if needed' },
          ]
        },
        {
          name: 'Workout Delta',
          exercises: [
            { name: 'Barbell Curl', weight: 20, repetitions: 12, sets: 3, note: null },
            { name: 'Alternating Dumbbell Curl', weight: 18, repetitions: 10, sets: 3, note: null },
            { name: 'Hammer Curl', weight: 16, repetitions: 10, sets: 3, note: null },
            { name: 'Triceps Pushdown', weight: 30, repetitions: 12, sets: 3, note: null },
          ]
        },
        {
          name: 'Workout Epsilon',
          exercises: [
            { name: 'Overhead Triceps Extension', weight: 20, repetitions: 10, sets: 3, note: null },
            { name: 'Dips', weight: 0, repetitions: 15, sets: 3, note: 'Control the descent' },
            { name: 'Bench Triceps Dips', weight: 0, repetitions: 15, sets: 3, note: null },
          ]
        }
      ]
    },
    {
      name: 'Gym C',
      workouts: [
        {
          name: 'Workout Zeta',
          exercises: [
            { name: 'Standing Calf Raise', weight: 50, repetitions: 20, sets: 3, note: null },
            { name: 'Seated Calf Raise', weight: 40, repetitions: 15, sets: 3, note: 'Full range of motion' },
            { name: 'Machine Calf Raise', weight: 45, repetitions: 15, sets: 3, note: null },
          ]
        },
        {
          name: 'Workout Theta',
          exercises: [
            { name: 'Crunches', weight: 0, repetitions: 25, sets: 3, note: null },
            { name: 'Reverse Crunches', weight: 0, repetitions: 20, sets: 3, note: null },
            { name: 'Plank', weight: 0, repetitions: 1, sets: 3, note: 'Hold for 30 seconds' },
            { name: 'Leg Raises', weight: 0, repetitions: 20, sets: 3, note: null },
          ]
        }
      ]
    }
  ];
  
  export default gyms;
  