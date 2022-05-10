# Challenge Resolution Notes

## Collections

I choose this collections in class EventStoreController because they are thread-safe collections. And both are recomended in "Java: How to program" in cases of concurrence. I preferred to apply threads in cases where many iterations are necessary, such as retrieving some event or filter or also removing by the iterator. I chose 100 milliseconds to wait for the thread as I thought it was a good amount of time, but I could increase it knowing the exact average of requests from the main function. Or Decrease.

## Tests

The tests are in the root folder, in the same one that came ready. I tried to test each function and verify its operation, according to javadocs.

## Resolution of this repository

The resolution of this challange are in the folder candidateResolution. The main class is EventStoreController, this class insert, remove all especific type and retrieve types. The auxiliar class is QueryIterator, that move to next event, show current event and remove the current event. 

## Resolution of gist

The resolution of gist are in the gist folder, in the index.js archive. The main fuction that receive the facts is `updateState`. This resolution was done in javascript. 