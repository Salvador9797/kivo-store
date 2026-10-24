package app.kivo.store;
/** Existing package must be the verified APK identity, at the same or newer version. */
final class DownloadInstallPolicy {
 static boolean installed(long apkCode,long installedCode,String expectedSigner,String installedSigner){return apkCode>0&&installedCode>=apkCode&&expectedSigner!=null&&!expectedSigner.isEmpty()&&expectedSigner.equals(installedSigner);}
 static boolean legacyMatches(String digest,String file,String name,String savedName,String release,String apkVersion){if(digest!=null&&digest.matches("sha256:[a-fA-F0-9]{64}"))return file!=null&&file.equalsIgnoreCase(digest.substring(7)+".apk");if(name==null||name.isEmpty()||!name.equals(savedName)||release==null||release.isEmpty()||apkVersion==null||apkVersion.isEmpty())return false;if(release.equals(apkVersion))return true;Integer compared=Versions.compare(release,apkVersion);return compared!=null&&compared==0;}
}
