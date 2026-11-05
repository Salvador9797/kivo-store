package app.kivo.store;
import java.util.ArrayDeque;
import java.util.HashSet;
/** Serializes installer confirmations; all Activity interactions stay on the UI thread. */
final class InstallQueue {
 private final ArrayDeque<String> waiting=new ArrayDeque<>();
 private final HashSet<String> known=new HashSet<>();
 private String active;
 synchronized boolean offer(String key){if(!known.add(key))return false;waiting.add(key);return true;}
 synchronized String take(boolean foreground){if(!foreground||active!=null||waiting.isEmpty())return null;active=waiting.remove();return active;}
 synchronized void complete(){if(active!=null)known.remove(active);active=null;}
 synchronized boolean busy(){return active!=null;}
 static boolean shouldOffer(boolean automatic,boolean attempted,boolean installed,String state){return automatic&&!attempted&&!installed&&"ready".equals(state);}
}
