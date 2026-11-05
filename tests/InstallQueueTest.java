package app.kivo.store;
public final class InstallQueueTest {
 static void check(boolean value,String message){if(!value)throw new AssertionError(message);}
 public static void main(String[] args){
  InstallQueue q=new InstallQueue();
  check(q.offer("a"),"new download");check(!q.offer("a"),"poll and broadcast deduplicate");check(q.take(false)==null,"background never launches installer");
  check("a".equals(q.take(true)),"resume launches first");check(q.offer("b"),"second completion queues");check(q.take(true)==null,"no overlapping installers");check(!q.offer("a"),"active item cannot be queued twice");
  q.complete();check(q.take(false)==null,"pause between installers waits");check("b".equals(q.take(true)),"FIFO on return");q.complete();check(q.take(true)==null,"queue exhausted");check(q.offer("a"),"explicit retry after cancellation");
  check(InstallQueue.shouldOffer(true,false,false,"ready"),"ready new request proceeds");
  check(!InstallQueue.shouldOffer(true,true,false,"ready"),"durable attempted marker prevents cancellation loop");
  check(!InstallQueue.shouldOffer(false,false,false,"ready"),"legacy completed APKs never auto install");
  check(!InstallQueue.shouldOffer(true,false,true,"ready"),"already installed skipped");
  for(String state:new String[]{"queued","waiting","downloading","failed","cancelled"})check(!InstallQueue.shouldOffer(true,false,false,state),"not ready: "+state);
  System.out.println("PASS: foreground gating, serialized confirmations, deduplication, cancellation, explicit retry and legacy downloads.");
 }
}
