import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class CamViewer extends HttpServlet {
   public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
      CamSystem cam = CamSystem.getInstance();
      cam.addViewer(req.getRemoteHost(), req.getParameter("referal"));
      res.setContentType("multipart/x-mixed-replace;boundary=myboundary");
      try {
         OutputStream out = res.getOutputStream();
         while(true) {
            out.write("--myboundary\nContent-type: image/jpeg\n\n".getBytes()); 
            out.write(cam.getImage());
         }
      } catch (IOException ioe) {
         throw new ServletException("one of these days...i'm going to throw this piece of shit out de window");
      }
   }
}
