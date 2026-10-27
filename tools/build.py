from pathlib import Path
import subprocess,zipfile,os,tempfile,shutil
root=Path(__file__).resolve().parents[1];build=root/'build';sdk=Path(os.environ.get('ANDROID_SDK_ROOT','/workspace/scratch/kivo-build/sdk'));tools=sdk/'build-tools/35.0.0';platform=sdk/'platforms/android-35/android.jar';build.mkdir(exist_ok=True)
def run(args):subprocess.run([str(x) for x in args],check=True,cwd=root)
classesdir=build/'classes';shutil.rmtree(classesdir,ignore_errors=True);classesdir.mkdir();dexdir=Path(tempfile.mkdtemp(prefix='dex-',dir=build))
java=list((root/'app/src/main/java').rglob('*.java'))
run(['java','-jar',os.environ.get('KIVO_ECJ_JAR','/workspace/scratch/kivo-build/ecj.jar'),'-8','-bootclasspath',str(platform)+':'+str(tools/'core-lambda-stubs.jar'),'-d',classesdir,*java])
args=build/'d8-args.txt';args.write_text('\n'.join(str(p) for p in classesdir.rglob('*.class')))
run([tools/'d8','--lib',platform,'--min-api','26','--output',dexdir,'@'+str(args)])
run([tools/'aapt2','compile','--dir',root/'app/src/main/res','-o',build/'resources.zip'])
run([tools/'aapt2','link','-o',build/'resources-base.apk','--manifest',root/'app/src/main/AndroidManifest.xml','-I',platform,'-A',root/'app/src/main/assets',build/'resources.zip'])
shutil.copyfile(build/'resources-base.apk',build/'unsigned-final.apk')
with zipfile.ZipFile(build/'unsigned-final.apk','a',compression=zipfile.ZIP_DEFLATED) as out:
 for p in dexdir.glob('*.dex'):out.write(p,p.name)
run([tools/'zipalign','-f','-p','4',build/'unsigned-final.apk',build/'aligned.apk'])
output=root.parent/'deliverables/Kivo-Prueba-0.9.13-mejorada.apk';output.parent.mkdir(exist_ok=True);staging=build/'signed-staging.apk'
run([tools/'apksigner','sign','--v4-signing-enabled','false','--ks',Path(os.environ.get('KIVO_KEYSTORE',str(root/'private-signing/kivo-alpha.jks'))),'--ks-key-alias','kivo-alpha','--ks-pass','pass:android','--key-pass','pass:android','--out',staging,build/'aligned.apk'])
with output.open('wb') as out:out.write(staging.read_bytes());out.flush();os.fsync(out.fileno())
run([tools/'apksigner','verify','--verbose','--print-certs',output]);print(output)
