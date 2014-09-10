public class KdTreeST<Value> {
    
    //constants for deciding which key to search
    private static boolean cmpX = true;
    
    //root of tree
    private Node root;
    
    //node of tree
    private class Node<Value> {
        private Point2D key;       // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private int N;             // number of nodes in subtree
        
        //node constructor
        public Node(Point2D key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }//end node constructor
    }//end node definition
    
    // is the tree empty?
    public boolean isEmpty() {
        return size() == 0;
    }//end isEmpty

    // return number of key-value pairs in BST
    public int size() {
        return size(root);
    }//end size()

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }//end size(Node)
    
    // add the point p to the tree or if it already exists, update
    public void insert(Point2D p, Value v) {
        if (v == null) {
            delete(p);
            return;
        }
        root = put(root, p, v, cmpX);
    }
    
    // helper method for insert
    private Node put(Node x, Point2D p, Value v, boolean dim) {
        //if the bottom of the tree has been reached, create a new node
        if (x == null) return new Node(p, v, 1);
        //compareTo by dimension
        int cmp = compareDim(p,x.key,dim);
        //go left
        if      (cmp < 0) x.left  = put(x.left,  p, v, !dim);
        //go right
        else if (cmp > 0) x.right = put(x.right, p, v, !dim);
        //found node, update value
        else x.val   = v;
        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }//end insert
    
    /*****************************************************************
      * Helper method for put that replaces compareTo
      * returns -1 for a.key < b
      * returns 1 for a.key > b
      * returns 0 for a.key == b
      * recursieve calls to this method should contain !dim
      * **************************************************************/
    private int compareDim(Point2D a, Point2D b, boolean dim) {
        //compare x coordinates
        if(dim==cmpX) {
            if(a.x() < b.x()) {
                return -1;
            }
            else if(a.x() > b.x()) {
                return 1;
            }
            else {
                return 0;
            }
        }//end if
        //compare y coordinates
        else {
            if(a.y() < b.y()) {
                return -1;
            }
            else if(a.y() > b.y()) {
                return 1;
            }
            else {
                return 0;
            }
        }//end else
    }//end compareDim
    
    // returns value mapped to by p   
    public Value get(Point2D p) {
        return get(root, p, cmpX);
    }//end get
    
    //helper method for get
    private Value get(Node x, Point2D p, boolean dim) {
        if (x == null) return null;
        int cmp = compareDim(p, x.key, dim);
        if      (cmp < 0) return get(x.left, p, !dim);
        else if (cmp > 0) return get(x.right, p, !dim);
        else              return x.val;
    }//end get
    
    // does the ST contain the point p?
    public boolean contains(Point2D p) {
        return get(p) != null;
    }//end contains
    
    //draw points and subdivisions to standard draw
    public void draw() {
        //draw tree, given unit square as bounds
        draw(root,1.0,0.0,0.0,1.0,cmpX);
    }//end draw
    
    //helper method for draw
    public void draw(Node x, double right, double bottom, double left, double top, boolean dim) {
        //check if node exists
        if(x == null) {return;}
        
        //draw point
        StdDraw.setPenRadius(.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        (x.key).draw();
        
        //if x, draw vertical line then draw subtrees
        if(dim == cmpX) {
            StdDraw.setPenRadius(.01);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line((x.key).x(), bottom, (x.key).x(), top);
            draw(x.left,(x.key).x(),bottom,left,top,!dim);
            draw(x.right,right,bottom,(x.key).x(),top,!dim);
        }//end if
        
        //if y, draw horizontal line then draw subtrees
        else {
            StdDraw.setPenRadius(.01);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(right, (x.key).y(), left, (x.key).y());
            draw(x.left,right,bottom,left,(x.key).y(),!dim);
            draw(x.left,right,(x.key).y(),left,top,!dim);          
        }//end else
    }//end draw
    
    //delete a node
    public void delete(Point2D p) {
        root = delete(root, p, cmpX);
    }

    //helper method for delete
    private Node delete(Node x, Point2D p, boolean dim) {
        if (x == null) return null;
        int cmp = compareDim(p, x.key, dim);
        if      (cmp < 0) x.left  = delete(x.left,  p, !dim);
        else if (cmp > 0) x.right = delete(x.right, p, !dim);
        else { 
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        } 
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    } //end delete
    

    // all points in the ST that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {        
        Queue<Point2D> queue = new Queue<Point2D>();
        return range(rect, queue, root, xcmp);
    }//end range

    //helper method for range
    public Queue<Point2D> range(RectHV rect, Queue<Point2D> queue, Node x, boolean dim) {
        if(rect.contains(x.key)) {
            queue.enqueue(x.key);
            queue = range(rect, queue, x.left);
            queue = range(rect, queue, x.right);
        }
        else {
            if(dim = cmpX) {
                if((x.key).x() > rect.xmin()) {
                    queue = range(rect, queue, x.left, !dim);
                }
                if((x.key).x() < rect.xmax()) {
                    queue = range(rect, queue, x.right, !dim);
                }
            }//end compare x
            else {
                if((x.key).y() > rect.ymin()) {
                    queue = range(rect, queue, x.left, !dim);
                }
                if((x.key).y() < rect.ymax()) {
                    queue = range(rect, queue, x.right, !dim);
                }
            }//end compare y
        }//end x not contained
        return queue;
    }//end range
    

/*****************************************************************************

    // a nearest neighbor in the ST to p; null if set is empty
    public Point2D nearest(Point2D p) {
        
        //return null if empty set
        if(isEmpty()) {
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
    
    /*********************************************
    
    public Point2D min() {
        if(isEmpty()) {return null;}
        return min(root);
    }
    
    public Point2D min(Node) {
        if(
    }
    
    *******************************************************************************/
    
    // unit testing of the methods (not graded)
    public static void main(String[] args) {
        
        //test constructor, insert, and draw
        KdTreeST<Integer> points = new KdTreeST<Integer>();
        for (int i = 0; i < 100; i++) {
            double x = StdRandom.uniform(0.0,1.0);
            double y = StdRandom.uniform(0.0,1.0);
            points.insert(new Point2D(x, y),1);
        }
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        points.draw();
        
        /************************************************************
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
        ****************************************************************/

    }//end main
    
}//end class