import java.util.*;

public class ViewerTable {
   private Vector hosts;
   private Vector referals;
   public ViewerTable() {
      hosts = new Vector();
      referals = new Vector();
   }
   public void add(String host, String referal) {
      hosts.add(host);
      referals.add(referal);
   }
   public String getHost(int index) {
      return (String)hosts.elementAt(index);
   }
   public String getReferal(int index) {
      return (String)referals.elementAt(index);
   }
   public int length() {
      return hosts.size();
   }
   public void delete(int index) {
      hosts.removeElementAt(index);
      referals.removeElementAt(index);
   }
}
