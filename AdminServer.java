import java.io.*;
import java.net.*;

public class AdminServer extends Thread {
   public void run() {
      try {
         ServerSocket ss = new ServerSocket(31337);
         while(true) {
            try {
               Socket clientSock = ss.accept();
               new ClientThread(clientSock).start();
            } catch (IOException ioe) {
            }
         }
      } catch (IOException ioe) {
         System.out.println("Start of admin server failed: "+ioe.getMessage());
      }
   }
      
   private class ClientThread extends Thread implements ViewerListener {
      Socket clientSock;
      OutputStream os;

      public ClientThread(Socket clientSock) {
         this.clientSock = clientSock;
      }

      public void run() {
         try {
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            os = clientSock.getOutputStream();
            CamSystem.getInstance().addViewerListener(this);
            send("+ CAMSYSTEM 1.0 Ready");
            // read commands from client
            while(br.ready()) {
               String cmd = br.readLine();
               String serial = cmd.substring(0, cmd.indexOf(' '));
               cmd = cmd.substring(cmd.indexOf(' ')+1);
            }
         } catch (IOException ioe) {
         }
      }
      public synchronized void send(String cmd) {
         try {
            os.write(new String(cmd+'\n').getBytes());
         } catch (IOException ioe) {
         }
      }
      public void viewerEvent(ViewerTable vt) {
         for (int i = 0; i < vt.length(); i++) {
            send("+ "+vt.getHost(i)+":"+vt.getReferal(i));
         }
      }
   }
}
