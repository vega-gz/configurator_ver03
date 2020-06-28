package Tools;
/*@author Lev*/
public class BackgroundThread  extends Thread {
    private Thread t;
    private final String threadName;
    DoIt di;
    
    public BackgroundThread( String name, DoIt di) {
        threadName = name;
        this.di = di;
        //System.out.println("Creating " +  threadName );
   }
   public void run() { di.doIt(); }
   
   public void start () {
      //System.out.println("Starting " +  threadName );
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }
}

