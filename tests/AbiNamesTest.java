package app.kivo.store;
public class AbiNamesTest {
 public static void main(String[] args){String[] arm64={"arm64-v8a"};for(String name:new String[]{"demo-arm-v7.apk","demo-armv7.apk","demo-arm_v7.apk","demo-armeabi-v7a.apk","demo-arm32.apk","demo-x86.apk","demo-x86_64.apk","demo-amd64.apk"})if(AbiNames.score(name,arm64)>=0)throw new AssertionError(name);if(AbiNames.score("demo-arm64.apk",arm64)<=0||AbiNames.score("demo-universal.apk",arm64)<=0)throw new AssertionError("compatible APK rejected");if(!"x86_64".equals(AbiNames.named("demo-x86_64.apk")))throw new AssertionError("x86_64 misread as x86");System.out.println("PASS: native ARMv7 aliases, ARM64-only rejection, x86_64 and universal variants.");}
}
