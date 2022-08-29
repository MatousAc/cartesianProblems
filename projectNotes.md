# Convex Hull project notes
## Explanation
[This video](https://www.youtube.com/watch?v=B2AJoQSZf4M) explains the convex hull problem well and goes through the two simple algorithms.

There are several different algorithms for solving the problem.  
## Jarvis March
* Starts at a point that is definitely in the convex hull (point farthest in a certain direction).
* Choosing points at random the point is selected that makes the most "outward" angle.
* Marching on with that point, the process repeats, circling around the entire hull.   

## Grahm's scan
Explained in [this video](https://www.youtube.com/watch?v=UUCKvHTP4Gg), Grahm's scan improves on the Jarvis March by  
* ordering the verticies by angle from starting point.  
This makes the algorithms *O(n log n)*.  


## Optimized solutions
Covered in [this paper](https://link.springer.com/content/pdf/10.1007/BF02712873.pdf) are some more optimal solutions.  