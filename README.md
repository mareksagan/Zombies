# Zombie Apocalypse
Coding assignment about zombies and recursion

# Requirements
* World is implemented by an **n x n** dimensional grid
* There is 1 initial zombie moving according to the given path which can infect immobile creatures on the grid
* Newly created zombies will start moving according to the same path as soon as the infecting zombie finishes his path (queue)
* Allowed path directions are following: **U** for UP, **D** for DOWN, **R** for RIGHT, **L** for LEFT
* Moving through the grid edge will result in crossing to the opposite edge of the grid
* Movements and infections should be logged

# Design decisions
As simple as possible, after all this is only a skills demo. I have used a static cache (treeset) since using a database
in this simple case would not be reasonable.

# Installation
Build & run the **Zombies** class and input your own parameters.

**OR**

Build & run the unit test class **ZombieTest** in *IntelliJ IDEA / Eclipse*. It covers most of the implemented features.
