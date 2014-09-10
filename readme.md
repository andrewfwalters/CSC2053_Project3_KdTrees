Project 3: Kd-Trees
===================

Write a symbol table data type that provides the ability to map from Point2D objects to arbitrary values. Use a 2d-tree to support efficient range search (find all of the points contained in a query rectangle) and nearest neighbor search (find a closest point to a query point). 2d-trees have numerous applications, ranging from classifying astronomical objects to computer animation to speeding up neural networks to mining data to image retrieval.

![setofpoints](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree-ops.png)

Geometric primitives
--------------------

![geometricprimitives](http://www.csc.villanova.edu/~map/2053/s14/kdtree/RectHV.png)

To get started, use the following geometric primitives for points and axis-aligned rectangles in the plane.

Use the immutable data type [Point2D.java](http://algs4.cs.princeton.edu/code/Point2D.java.html) (part of algs4.jar) for points in the plane. Here is the subset of its API that you may use:

"public class Point2D"
{
   public Point2D(double x, double y)              // construct the point (x, y)
   public  double x()                              // x-coordinate
   public  double y()                              // y-coordinate
   public  double distanceSquaredTo(Point2D that)  // square of Euclidean distance between two points
   public     int compareTo(Point2D that)          // for use in an ordered symbol table
   public boolean equals(Object that)              // does this point equal that?
   public    void draw()                           // draw to standard draw
   public  String toString()                       // string representation
}

Use the immutable data type [RectHV.java](http://algs4.cs.princeton.edu/code/RectHV.java.html) (not part of algs4.jar) for axis-aligned rectanges. Here is the subset of its API that you may use:

"public class RectHV"
{
   public    RectHV(double xmin, double ymin,      // construct the rectangle [xmin, xmax] x [ymin, ymax]
                    double xmax, double ymax)      // throw a java.lang.IllegalArgumentException if (xmin > xmax) or (ymin > ymax)
   public  double xmin()                           // minimum x-coordinate of rectangle
   public  double ymin()                           // minimum y-coordinate of rectangle
   public  double xmax()                           // maximum x-coordinate of rectangle
   public  double ymax()                           // maximum y-coordinate of rectangle
   public boolean contains(Point2D p)              // does this rectangle contain the point p (either inside or on boundary)?
   public boolean intersects(RectHV that)          // does this rectangle intersect that rectangle (at one or more points)?
   public  double distanceSquaredTo(Point2D p)     // square of Euclidean distance from point p to closest point in rectangle
   public boolean equals(Object that)              // does this rectangle equal that?
   public    void draw()                           // draw to standard draw
   public  String toString()                       // string representation
}

Do not modify these data types.

Part 1
------

Brute-force implementation (NOT a 2d-tree). Write a mutable data type PointST.java that uses a symbol table to associate Point2D objects to a generic value. Implementing the following API using a Binary Search Tree implementation:

"public class PointST<Value>" 
{
   public         PointST()                               // construct an empty symbol table of points
   public           boolean isEmpty()                     // is the symbol table empty?
   public               int size()                        // number of points in the ST
   public              void insert(Point2D p, Value v)    // add the point p to the ST or if it already exists, update
   public             Value get(Point2D p)                // returns value mapped to by p   
   public           boolean contains(Point2D p)           // does the ST contain the point p?
   public              void draw()                        // draw points to standard draw
   public Iterable<Point2D> range(RectHV rect)            // all points in the ST that are inside the rectangle
   public           Point2D nearest(Point2D p)            // a nearest neighbor in the ST to p; null if set is empty
   public static void main(String[] args)                 // unit testing of the methods (not graded)
}

Your implementation should support insert(), get() and contains() in time proportional to the logarithm of the number of points in the set in the worst case; it should support nearest() and range() in time proportional to the number of points in the symbol table. You may assume that clients will not pass key or value arguments equal to null. Use the BST.java or ST.java

Part 2
------

2d-tree implementation. Write a mutable data type KdTreeST.java that uses a 2d-tree to implement the same API (but replace PointST with KdTreeST). A 2d-tree is a generalization of a BST to two-dimensional keys. The idea is to build a BST with points in the nodes, using the x- and y-coordinates of the points as keys in strictly alternating sequence, starting with the x-coordinates.

**Search and insert**. The algorithms for search and insert are similar to those for BSTs, but at the root we use the x-coordinate (if the point to be inserted has a smaller x-coordinate than the point at the root, go left; otherwise go right); then at the next level, we use the y-coordinate (if the point to be inserted has a smaller y-coordinate than the point in the node, go left; otherwise go right); then at the next level the x-coordinate, and so forth.
- Insert (0.7, 0.2)
![kd1](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree1.png) ![graph1](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree-insert1.png)
- Insert (0.5, 0.4)
![kd2](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree2.png) ![graph2](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree-insert2.png)
- Insert (0.2, 0.3)
![kd3](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree3.png) ![graph3](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree-insert3.png)
- Insert (0.4, 0.7)
![kd4](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree4.png) ![graph4](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree-insert4.png)
- Insert (0.9, 0.6)
![kd5](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree5.png) ![graph5](http://www.csc.villanova.edu/~map/2053/s14/kdtree/kdtree-insert5.png)

**Draw**. A 2d-tree divides a 2D plane in a simple way: all the points to the left of the root go in the left subtree; all those to the right go in the right subtree; and so forth, recursively. Your draw() method should draw all of the points to standard draw in black and the subdivisions in red (for vertical splits) and blue (for horizontal splits). For simplicity, you should leave your StdDraw scale at its default values (which include only the unit square). The provided inputs files contain only points in the unit square, and the provided visualizers also assume all points are in the unit square. This method need not be efficient—it is primarily for debugging.

The prime advantage of a 2d-tree over a BST is that it supports efficient implementation of range search and nearest neighbor search. Each node corresponds to an axis-aligned rectangle, which encloses all of the points in its subtree. The root corresponds to the infinitely large square from [(-8, -8), (+8, +8 )]; the left and right children of the root correspond to the two rectangles split by the x-coordinate of the point at the root; and so forth.

**Range search**. To find all points contained in a given query rectangle, start at the root and recursively search for points in both subtrees using the following pruning rule: if the query rectangle does not intersect the rectangle corresponding to a node, there is no need to explore that node (or its subtrees). A subtree is searched only if it might contain a point contained in the query rectangle.

**Nearest neighbor search**. To find a closest point to a given query point, start at the root and recursively search in both subtrees using the following pruning rule: if the closest point discovered so far is closer than the distance between the query point and the rectangle corresponding to a node, there is no need to explore that node (or its subtrees). That is, a node is searched only if it might contain a point that is closer than the best one found so far. The effectiveness of the pruning rule depends on quickly finding a nearby point. To do this, organize your recursive method so that when there are two possible subtrees to go down, you choose first the subtree that is on the same side of the splitting line as the query point; the closest point found while exploring the first subtree may enable pruning of the second subtree.

**Clients**.  You may use the following interactive client programs to test and debug your code.

[KdTreeVisualizer.java](http://www.csc.villanova.edu/~map/2053/s14/kdtree/code/KdTreeVisualizer.java) computes and draws the 2d-tree that results from the sequence of points clicked by the user in the standard drawing window.
[RangeSearchVisualizer.java](http://www.csc.villanova.edu/~map/2053/s14/kdtree/code/RangeSearchVisualizer.java) reads a sequence of points from a file (specified as a command-line argument) and inserts those points into a 2d-tree. Then, it performs range searches on the axis-aligned rectangles dragged by the user in the standard drawing window.
[NearestNeighborVisualizer.java](http://www.csc.villanova.edu/~map/2053/s14/kdtree/code/NearestNeighborVisualizer.java) reads a sequence of points from a file (specified as a command-line argument) and inserts those points into a 2d-tree. Then, it performs nearest neighbor queries on the point corresponding to the location of the mouse in the standard drawing window.

**Analysis of running time and memory usage**. Analyze the effectiveness of your approach to this problem by giving estimates of its time and space requirements.

Give the total memory usage in bytes (using tilde notation) of your 2d-tree data structure as a function of the number of points N, using the memory-cost model from lecture and Section 1.4 of the textbook. Count all memory that is used by your 2d-tree, including memory for the nodes, points, and rectangles. For the purposes of this assignment, assume that each Point2D object uses 32 bytes.
- Give the expected running time in seconds (using tilde notation) to build a 2d-tree on N uniformly random points in the unit square. (Do not count the time to read in or generate the points.) Using the given input files is not sufficient.
- How many nearest neighbor calculations can your 2d-tree implementation perform per second for  input100K.txt (100,000 points) and  input1M.txt (1 million points), where the query points are uniformly random points in the unit square? (Do not count the time to read in the points or to build the 2d-tree.) Repeat this question but with the brute-force implementation.

**Submission**.  Submit a zipped folder containing: PointST.java, KdTreeST.java, readme.txt , and your project report, which should also be printed and handed in on the due date. The report should include all of the above, plus some sample runs and screenshots of your program to demonstrate that it works well. (The code for Point2D.java and RectHV.java should not be included since it should not have been modified.) 

More details (faq, helpful suggestions, more test files). See the [kd-tree project checklist from the Princeton algorithms course](http://www.cs.princeton.edu/courses/archive/spring14/cos226/checklist/kdtree.html)
 
This assignment was developed by Kevin Wayne, with Boid simulation by Josh Hug.
