# Convex Hull project notes
## Explanation
* [This video](https://www.youtube.com/watch?v=B2AJoQSZf4M) explains the convex hull problem well and goes through the two simple algorithms.
* [Here's the Wiki](https://en.wikipedia.org/wiki/Convex_hull_algorithms#Akl%E2%80%93Toussaint_heuristic)

There are several different algorithms for solving the problem.  
## Naive Approach
* Detailed [here](https://www.math.ucsd.edu/~ronspubs/83_09_convex_hull.pdf) the Naive Approach is basically an $O(n^3)$ algorithm

## Jarvis March
* Starts at a point that is definitely in the convex hull (point farthest in a certain direction).
* Choosing points at random the point is selected that makes the most "outward" angle.
* Marching on with that point, the process repeats, circling around the entire hull.   

## Graham's scan
Explained in [this video](https://www.youtube.com/watch?v=UUCKvHTP4Gg), Grahm's scan improves on the Jarvis March by  
* ordering the verticies by angle from starting point.  
This makes the algorithms $O(n \log n)$.  


## Optimized solutions
* Covered in [this paper](https://link.springer.com/content/pdf/10.1007/BF02712873.pdf) are some more optimal solutions.
* There is also the [Akl–Toussaint heuristic](https://en.wikipedia.org/wiki/Convex_hull_algorithms#Akl%E2%80%93Toussaint_heuristic) that should speed things up for any of these algorithms. I plan to have this as an optional addition to my algorithms.
* I'm thinking of implementing the [Kirkpatrick-Seidel](https://en.wikipedia.org/wiki/Kirkpatrick%E2%80%93Seidel_algorithm) optimal solution too.


