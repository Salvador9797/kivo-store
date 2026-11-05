"""Build an unsigned APK using JDK 17+ and Android SDK 35; never reads signing keys."""
from pathlib import Path
import os,subprocess,sys,tempfile,zipfile,xml.etree.ElementTree as ET
root=Path(__file__).resolve().parents[1]
sdk_value=os.environ.get('ANDROID_SDK_ROOT') or os.environ.get('ANDROID_HOME')
if not sdk_value:raise SystemExit('Set ANDROID_SDK_ROOT to an Android SDK with platform 35 and build-tools 35.0.0')
sdk=Path(sdk_value).resolve();tools=sdk/'build-tools'/os.environ.get('KIVO_BUILD_TOOLS_VERSION','35.0.0');platform=sdk/'platforms/android-35/android.jar'
manifest=root/'app/src/main/AndroidManifest.xml';meta=ET.parse(manifest).getroot();ns='{http://schemas.android.com/apk/res/android}'
version=meta.attrib[ns+'versionName'];code=meta.attrib[ns+'versionCode']
output=root/'build/outputs'/f'Kivo-Market-{version}-code{code}-unsigned.apk';output.parent.mkdir(parents=True,exist_ok=True)
def run(args):subprocess.run([str(arg) for arg in args],cwd=root,check=True)
run([sys.executable,root/'tools/sync_ui.py','--check'])
with tempfile.TemporaryDirectory(prefix='unsigned-',dir=root/'build') as temp:
 work=Path(temp);classes=work/'classes';classes.mkdir();dex=work/'dex';dex.mkdir()
 sources=sorted((root/'app/src/main/java').rglob('*.java'))
 run(['java','-m','jdk.compiler/com.sun.tools.javac.Main','-source','8','-target','8','-bootclasspath',str(platform)+os.pathsep+str(tools/'core-lambda-stubs.jar'),'-d',classes,*sources])
 run([tools/'d8','--lib',platform,'--min-api','26','--output',dex,*sorted(classes.rglob('*.class'))])
 run([tools/'aapt2','compile','--dir',root/'app/src/main/res','-o',work/'resources.zip'])
 run([tools/'aapt2','link','-o',work/'resources.apk','--manifest',manifest,'-I',platform,'-A',root/'app/src/main/assets',work/'resources.zip'])
 with zipfile.ZipFile(work/'resources.apk','a',compression=zipfile.ZIP_DEFLATED) as apk:
  for file in dex.glob('*.dex'):apk.write(file,file.name)
 run([tools/'zipalign','-f','-p','4',work/'resources.apk',output])
 run([tools/'zipalign','-c','-p','4',output])
 print(output)
