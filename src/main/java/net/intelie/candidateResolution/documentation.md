# Challenge Resolution Notes

## Collections

I choose this collections in class EventStoreController because they are thread-safe collections. And both are recomended in "Java: How to program" in cases of concurrence. Despite these choices, I was unable to implement threads and performance improvements.

## Tests

The tests are in the root folder, in the same one that came ready. I tried to test each function and verify its operation, according to javadocs.

## Resolution of this repository

The resolution of this challange are in the folder candidateResolution. The main class is EventStoreController, this class insert, remove all especific type and retrieve types. The auxiliar class is QueryIterator, that move to next event, show current event and remove the current event. 

## Resolution of gist

The resolution of gist are in the gist folder, in the index.js archive. The main fuction that receive the facts is `updateState`. This resolution was done in javascript. 