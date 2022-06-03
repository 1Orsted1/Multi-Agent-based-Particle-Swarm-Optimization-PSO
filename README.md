
# Multi-Agent-based-Particle-Swarm-Optimization (PSO)

A heuristic optimization method that minimizes a function, writed in Java and Jade


## Installation

 
## Jade
- Follow the installation process in the page.
##### 🔗 Jade WebSite:(https://jade.tilab.com/)

* Once you installed Jade and the 'jade.jar', clone this repository.
```bash
  $ git clone https://github.com/1Orsted1/Multi-Agent-based-Particle-Swarm-Optimization-PSO.git
```

## Documentation

- Inside the project create a 'classes' folder.

```bash
  $ mkdir classes
```

- Compile the project with the next command (note: the ~/Library path is where you put the jade.jar):

```bash
$ javac -classpath ~/Library/jade.jar -d classes src/pso_caveman/*.java src/math_caveman/*.java
```

- Execute with the next command:

```bash
$ java -cp ~/Library/JADE/jade.jar:classes jade.Boot -gui -agents Receiver:pso_caveman.PositionMatrix
```


## 🧠  To have in mind 

🧑🏽‍💻 The functionality still a bit sketchy, so once the project runs you must add manually every 'Particle' agents (with a *name* and their *initial population*( i'll change this in the future. 


## Windows

Since the project was writen in Java it definitively works on Windos, but the commands in the documentation section are ment for unix-based systems, so i don't think the commands work on windows, and i have no clue how to do it there, so is up to you find the way. 😄 


