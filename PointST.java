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
    
    // unit testing of the methods (not graded)
    public static void main(String[] args) {
        
        //test constructor, insert, and draw
        PointST<Integer> points = new PointST<Integer>();
        for (int i = 0; i < 100; i++) {
            int x = StdRandom.uniform(100);
            int y = StdRandom.uniform(100);
            points.insert(new Point2D(x, y),1);
        }
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(0, 100);
        StdDraw.setYscale(0, 100);
        StdDraw.setPenRadius(.01);
        points.draw();
        
        //test range
        RectHV rect = new RectHV(20, 20, 40, 40);
        rect.draw();
        Iterable<Point2D> list = points.range(rect);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(.02);
        for(Point2D key: list) {
            key.draw();
        }//end loop       
        
        //test nearest
        Point2D center = new Point2D(50, 50);
        points.insert(center,1);
        StdDraw.setPenColor(StdDraw.BLUE);
        center.draw();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.005);
        center.drawTo(points.nearest(center));
        

    }//end main
    
}//end class