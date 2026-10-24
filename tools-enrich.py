import json,re,html
from pathlib import Path
from html.parser import HTMLParser
class Text(HTMLParser):
 def __init__(self):super().__init__();self.parts=[]
 def handle_data(self,s):self.parts.append(s)
 def handle_starttag(self,tag,attrs):
  if tag in ['p','br','li']:self.parts.append('\n')
def local(d):
 if not isinstance(d,dict):return d or ''
 return d.get('es') or d.get('es-ES') or d.get('en-US') or next(iter(d.values()),'')
source=json.load(open('/workspace/scratch/kivo-build/fdroid-packages.json'));entries=json.load(open('kivo-v04/app/src/main/assets/directory.json'));lookup={}
for pkg,a in source.items():
 r=a['metadata'].get('sourceCode','').rstrip('/').replace('https://github.com/','').lower()
 lookup[r]=a
count=0
for a in entries:
 m=lookup.get(a['repo'].lower(),{}).get('metadata',{});s=Text();s.feed(local(m.get('description',{})));desc=''.join(s.parts);a['longDescription']=re.sub(r'\n{3,}','\n\n',desc).strip()[:6000];a['metadataLocales']=list(m.get('name',{})) if isinstance(m.get('name'),dict) else []
 shots=m.get('screenshots',{}).get('phone',{});chosen=shots.get('es') or shots.get('es-ES') or shots.get('en-US') or next(iter(shots.values()),[])
 a['screenshots']=[{'path':x['name'],'sha256':x['sha256']} for x in chosen[:4] if x.get('name','').startswith('/') and re.fullmatch('[a-f0-9]{64}',x.get('sha256',''))]
 a['website']=m.get('webSite','');a['license']=m.get('license','')
 versions=list(lookup.get(a['repo'].lower(),{}).get('versions',{}).values())
 if versions:
  version=max(versions,key=lambda x:x.get('manifest',{}).get('versionCode',0));man=version.get('manifest',{});a['referenceVersion']=man.get('versionName','');a['referenceMinSdk']=man.get('usesSdk',{}).get('minSdkVersion');a['referenceAbis']=man.get('nativecode',[])
 if a['screenshots']:count+=1
Path('kivo-v04/app/src/main/assets/directory.json').write_text(json.dumps(entries,ensure_ascii=False,separators=(',',':')))
print(len(entries),'projects;',count,'with screenshots;',Path('kivo-v04/app/src/main/assets/directory.json').stat().st_size,'bytes')
