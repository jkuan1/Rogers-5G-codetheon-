# AR You It? 
Winner of the Best Presentation Award from the Roger's 5G Hackathon at the University of British Columbia, October 25-26, 2019.

## We AR It.
AR You It is a conceptual design for a local, AR, massive multiplayer game with a focus on taking advantage of Edge computing and 5G, the next stepping stones in data processing, storage, and connectivity. The game concept revolves around a modified version of paintball; players are divided into two teams, and, upon shooting a player from the other side, converts players between teams (i.e. if a person from the green team shoots a person from the orange team, that orange player would be converted to the green team). Paint sticking to player meshes, the facial tracking system that would identify regiestered players, and the additional element of converting non-players to NPCS, which may be shot for bonus points and/or powerups, push the game to the limits of current computational ability--hence, the game as a whole stands as a prime use case for edge computing.

## What We've Done
As of the end of the Rogers 5G Hackathon, we have a skeleton for a face-based login system built on top of Firebase's Face Dection MLK. Future implementations would likely revolve around porting the game to Unity, developing the database/backend, and building a functional prototype.

### Ideation Tibits and Trinkets
- Add a timer in place of shooting mechanism (must have face found in x number of last n frames to "tag"?) -> higher precision than using phone GPS?
- Edge compute: manually computing every face in order to identify if a) someone is a player and b) if they're tagged or not, managing all users, ect.
- Cost of rendering liquids requires edge compute for massive multiplayer experiences and designs
- Privacy issues of mapping non-registered players as objects is abstracted away by use of edge proximity features and facial recognition algorithms relying upon face meshes, rather than textured models

### Links, Resources, and Inspiration
- https://firebase.google.com/docs/ml-kit/android/detect-faces
- https://medium.com/androidiots/firebase-ml-kit-101-face-detection-5057190e58c0
- https://heartbeat.fritz.ai/building-a-real-time-face-detector-in-android-with-ml-kit-f930eb7b36d9
- https://github.com/husaynhakeem/android-face-detector?source=post_page-----f930eb7b36d9---------------------- (frame-by-frame processing built on top of the firebase faces MLK)
- https://developers.google.com/ar/reference/java/arcore/reference/com/google/ar/core/Frame: AR Core Frame Class; ray-tracing may map to paintball shot?
- https://natario1.github.io/CameraView/docs/frame-processing.html
