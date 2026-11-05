package app.kivo.store;
import java.util.Arrays;
import java.util.Locale;
final class AbiNames {
 static String named(String name){String n=name.toLowerCase(Locale.ROOT);if(n.matches(".*(?:arm64|aarch64).*"))return "arm64-v8a";if(n.matches(".*(?:armeabi|arm[-_]?v?7|arm32|aarch32).*"))return "armeabi-v7a";if(n.matches(".*(?:x86_64|x86-64|amd64).*"))return "x86_64";if(n.matches(".*(?:x86|i[3-6]86).*"))return "x86";return "";}
 static int score(String name,String[] supported){String abi=named(name);if(!abi.isEmpty())return !Arrays.asList(supported).contains(abi)?-1:supported.length>0&&abi.equals(supported[0])?4:3;return name.toLowerCase(Locale.ROOT).contains("universal")?5:1;}
}
