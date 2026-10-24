package app.kivo.store;
public class DownloadInstallPolicyTest {
 static void check(boolean b,String message){if(!b)throw new AssertionError(message);}
 public static void main(String[] args){
 check(DownloadInstallPolicy.installed(10,10,"same","same"),"Same verified version is installed");check(DownloadInstallPolicy.installed(10,11,"same","same"),"A newer installed version must not offer downgrade");check(!DownloadInstallPolicy.installed(11,10,"same","same"),"A downloaded newer APK must remain installable");check(!DownloadInstallPolicy.installed(10,10,"one","other"),"Different signer is not this APK installation");check(!DownloadInstallPolicy.installed(0,10,"same","same"),"Unknown APK code cannot be called installed");check(!DownloadInstallPolicy.installed(10,10,"",""),"Missing signer cannot confirm identity");
 check(DownloadInstallPolicy.legacyMatches("","","app.apk","app.apk","v1.2.0","1.2"),"GitHub v tag and actual APK version can match");check(!DownloadInstallPolicy.legacyMatches("","","app.apk","app.apk","2.0","1.2"),"Same filename from another release is not enough");check(!DownloadInstallPolicy.legacyMatches("","","app.apk","other.apk","1.2","1.2"),"Other variant cannot borrow package identity");String hash="aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";check(DownloadInstallPolicy.legacyMatches("sha256:"+hash,hash+".apk","a","b","custom-tag","different-version"),"Exact verified binary hash supports arbitrary release tags");check(!DownloadInstallPolicy.legacyMatches("sha256:"+hash,"other.apk","a","b","custom-tag","different-version"),"Hash mismatch cannot borrow identity");
 check(!DownloadInstallPolicy.legacyMatches("sha256:"+hash,"different.apk","app.apk","app.apk","1.0","1.0"),"A known hash mismatch must not fall back to filename/version");
 System.out.println("PASS: installed/downloaded version distinction, newer APK and certificate guards, legacy tag normalization and exact binary identity.");
 }
}
