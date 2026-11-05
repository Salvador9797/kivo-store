package app.kivo.store;
/** Completed downloads are history after uninstall, not new install requests. */
final class DownloadLifecycle {
 static boolean redownload(boolean installed,boolean seenInstalled,boolean currentFlow,String state){return !installed&&"ready".equals(state)&&(seenInstalled||!currentFlow);}
}
