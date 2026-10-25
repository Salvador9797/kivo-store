package app.kivo.store;
final class UpdatePolicy {
 static boolean newer(String source,long remoteCode,String remoteVersion,long installedCode,String installedVersion){if("fdroid".equals(source)||remoteCode>0)return remoteCode>0&&remoteCode>installedCode;return Versions.compare(remoteVersion,installedVersion)>0;}
}
