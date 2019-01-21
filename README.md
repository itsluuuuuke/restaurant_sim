# restaurant_sim

This project simulates a restaurant with the following "components":

## Multithreading
Head Waiter - Welcome customers at the "door" and allocate a table (of suitable size) for them
Waiter - Serve customers: take orders, serve dishes
Customer - Arriving the restaurant in groups of different sizes, "wait" if the restaurant is full

Each customer is a thread.
The head waiter is a thread.
The waiters are threads.

## RMI component
With a login GUI and a Control GUI, start/pause/resume the simulation process.
