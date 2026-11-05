package app.kivo.store;
public class DownloadLifecycleTest {
 static void check(boolean b,String why){if(!b)throw new AssertionError(why);}
 public static void main(String[] args){
 check(!DownloadLifecycle.redownload(true,true,true,"ready"),"Installed APK still offers Open");
 check(DownloadLifecycle.redownload(false,true,true,"ready"),"Uninstall retires completed APK even when file exists");
 check(!DownloadLifecycle.redownload(false,false,true,"ready"),"Fresh cancelled installation remains installable");
 check(DownloadLifecycle.redownload(false,false,false,"ready"),"Legacy completed history is not a new installation request");
 for(String state:new String[]{"downloading","queued","waiting","failed","cancelled"})check(!DownloadLifecycle.redownload(false,true,true,state),"Non-completed state: "+state);
 System.out.println("PASS: uninstall, cached APK history, migration, fresh pending install and active transfers.");
 }
}
