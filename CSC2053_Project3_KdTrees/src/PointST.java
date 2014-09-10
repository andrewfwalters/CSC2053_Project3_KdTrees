
import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wildcat
 */
public class PointST<Value> {
    private BST<Point2D, Value> st;
    
    // construct an empty symbol table of points
    public PointST() {
        st = new BST<Point2D, Value>();
    }//end constructor
    
    // is the symbol table empty?
    public boolean isEmpty() {
        return st.isEmpty();
    }//end isEmpty
    
    // number of points in the ST
    public int size() {
        return st.size();
    }//end size
    
    // add the point p to the ST or if it already exists, update
    public void insert(Point2D p, Value v) {
        st.put(p, v);
    }//end insert
    
    // returns value mapped to by p   
    public Value get(Point2D p) {
        return st.get(p);
    }//end get
    
    // does the ST contain the point p?
    public boolean contains(Point2D p) {
        return st.contains(p);
    }//end contains
    
    // draw points to standard draw
    public void draw() {
        Iterable<Point2D> list = st.keys();
        for(Point2D key: list) {
            key.draw();
        }
    }//end draw
    
    // all points in the ST that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        
        //get an iterable list of all points within the y range
        Point2D ymin = new Point2D(rect.ymin(),rect.xmin());
        Point2D ymax = new Point2D(rect.ymax(),rect.xmax());
        Iterable<Point2D> list = st.keys(ymin,ymax);
        
        //determine which of those points are within x range
        Queue<Point2D> queue = new Queue<Point2D>();
        for(Point2D key: list) {
            if(rect.xmin() <= key.x() && key.x() <= rect.xmax()) {
                queue.enqueue(key);
            }
        }//end loop
        
        return queue;
        
    }//end range
    
    // a nearest neighbor in the ST to p; null if set is empty
    public Point2D nearest(Point2D p) {
        
        //return null if empty set
        if(st.isEmpty()) {
            return null;
        }
        
        //temp variables to iterate through points
        Point2D near = st.min();
        double dist = p.distanceSquaredTo(near);
        Iterable<Point2D> list = st.keys();
        
        //loop through all the points, checking distance to each
        for(Point2D key: list) {
            //check if this is closer than current point
            if(p.distanceSquaredTo(key) < dist && p.compareTo(key) != 0) {
                near = key;
                dist = p.distanceSquaredTo(key);
            }
        }//end loop
        
        return near;
    }//end nearest

    
    public static void main(String[] args) {
        
        PointST<Integer> points = new PointST<Integer>();
        double x,y;
        In read = new In(new File("C:\\Users\\wildcat\\Google Drive\\Villanova\\Algorithms III\\CSC2053_Project3_KdTrees\\input1.txt"));
        while(read.hasNextLine()) {
            x = read.readDouble();
            y = read.readDouble();
            points.insert(new Point2D(x,y),1);
        }
        
        /***********************************************************************
         * used to time creating an instance of KdTreeST with N points
         * 
         * 
        double[] x = new double[800];
        double[] y = new double[800];
        
        //test constructor, insert, and draw
        KdTreeST<Integer> points = new KdTreeST<Integer>();
        for (int i = 0; i < 800; i++) {
            x[i] = StdRandom.uniform(0.0,1.0);
            y[i] = StdRandom.uniform(0.0,1.0);
        }
        
        Stopwatch watch = new Stopwatch();
        
        for (int i = 0; i < 800; i++) {
            points.insert(new Point2D(x[i], y[i]),1);
        }
        
        System.out.println("elapsed time: " + watch.elapsedTime());
        * 
        * 
        * *********************************************************************/
        
        
        /***********************************************************************
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setPenRadius(.01);
        points.draw();
        
        //test range
        RectHV rect = new RectHV(.2,.2,.4,.4);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.setPenRadius(.002);
        rect.draw();
        Iterable<Point2D> list = points.range(rect);
        StdDraw.setPenColor(StdDraw.PINK);
        StdDraw.setPenRadius(.02);
        for(Point2D key: list) {
            key.draw();
        }//end loop       
        * 
        ***********************************************************************/
        
        /***********************************************************************
        
        test nearest
        Point2D center = new Point2D(.5, .5);
        StdDraw.setPenColor(StdDraw.CYAN);
        StdDraw.setPenRadius(.02);
        center.draw();
        StdDraw.setPenRadius(.005);
        center.drawTo(points.nearest(center));
        * 
        * *********************************************************************/
        
        Stopwatch watch = new Stopwatch();
        for(int i = 0; i < 1000; i++) {
            points.nearest(new Point2D(StdRandom.uniform(0.0,1.0),StdRandom.uniform(0.0,1.0)));
        }
        System.out.println("elapsed time: " + watch.elapsedTime());

    }//end main
}
