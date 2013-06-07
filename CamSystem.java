import java.io.*;
import java.util.*;

public class CamSystem {
   public static final String IMAGE_PATH = "/tmp/cam.jpg";
   private static CamSystem instance;

   private byte[] image;
   private Vector viewerListeners;
   private ViewerTable vt;

   private CamSystem() {
      viewerListeners = new Vector();
      vt = new ViewerTable();
      new UpdateThread().start();
      new AdminServer().start();
      // set image to a blank one or somethign, so early attempts to get the image don't eat shit
   }

   public synchronized void setImage(byte[] image) {
      this.image = image;
   }

   public synchronized byte[] getImage() {
      return image;
   }

   public static synchronized CamSystem getInstance() {
      if (instance == null) {
         instance = new CamSystem();
      }
      return instance;
   }

   public void addViewerListener(ViewerListener vl) {
      viewerListeners.add(vl);
   }

   private class UpdateThread extends Thread {
      public void run() {
         int failCount = 0;
         while(true) {
            try {
               // gimme a pound of your sweetest cheba
               File imageFile = new File(CamSystem.IMAGE_PATH);
               if (imageFile.length() > 0) {
                  FileInputStream fis = new FileInputStream(imageFile);
                  byte[] image = new byte[(int)imageFile.length()];
                  fis.read(image);
                  CamSystem.getInstance().setImage(image);
                  failCount = 0;
               } else {
                  failCount++;
               }
               // i can't take it no mo....
            } catch (FileNotFoundException fnfe) {
               failCount++;
            } catch (IOException ioe) {
               failCount++;
            }
            if (failCount > 15) {
               // set some sort of technical difficulty banner
            }
            try {
               Thread.sleep(2000);
            } catch (InterruptedException ie) {
            }
         }
      }
   }
   public void addViewer(String host, String referal) {
      vt.add(host, referal);
      notifyViewerListeners();
   }
   private void notifyViewerListeners() {
      for (Iterator i = viewerListeners.iterator(); i.hasNext(); ) {
         ViewerListener vl = (ViewerListener)i.next();
         vl.viewerEvent(vt);
      }
   }
}
