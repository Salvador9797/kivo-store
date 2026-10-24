"""Generate a compact snapshot only from the index authenticated by RepoTrust's signed entry."""
import json,hashlib,re,html,sys,datetime
from pathlib import Path
from html.parser import HTMLParser
root=Path(__file__).resolve().parents[1];entry=json.loads((root/'build/entry.json').read_text());src=Path(sys.argv[1]);ref=entry['index']
assert src.stat().st_size==ref['size'] and hashlib.sha256(src.read_bytes()).hexdigest()==ref['sha256'],'Index does not match signed entry'
class Text(HTMLParser):
 def __init__(self):super().__init__();self.text=[]
 def handle_data(self,t):self.text.append(t)
 def handle_starttag(self,t,a):
  if t in ('p','br','li'):self.text.append('\n')
def loc(o):
 if not isinstance(o,dict):return o or ''
 return next((o[l] for l in ['es','es-ES','en-US','en-GB'] if l in o),next(iter(o.values()),''))
apps=[]
for pkg,raw in json.loads(src.read_text())['packages'].items():
 if not re.fullmatch(r'[A-Za-z0-9_]+(?:\.[A-Za-z0-9_]+)+',pkg):continue
 m=raw['metadata'];versions=[]
 for v in raw.get('versions',{}).values():
  f=v.get('file',{});man=v.get('manifest',{});pins=man.get('signer',{}).get('sha256',[])
  if v.get('releaseChannels') or not f.get('name','').endswith('.apk') or not re.fullmatch('[a-f0-9]{64}',f.get('sha256','')) or not pins or not 0<f.get('size',0)<=500*1024**2:continue
  if m.get('preferredSigner') and m['preferredSigner'] not in pins:continue
  versions.append(v)
 if not versions:continue
 versions.sort(key=lambda v:v['manifest'].get('versionCode',0),reverse=True);v=versions[0];man=v['manifest'];assets=[]
 for a in versions[:16]:
  am=a['manifest'];f=a['file']
  if am.get('versionName')!=man.get('versionName'):continue
  assets.append(dict(id=am['versionCode'],name=f['name'][1:],url='https://f-droid.org/repo'+f['name'],size=f['size'],digest='sha256:'+f['sha256'],signers=am['signer']['sha256'],package=pkg,abis=am.get('nativecode',[]),minSdk=am.get('usesSdk',{}).get('minSdkVersion',1),maxSdk=am.get('usesSdk',{}).get('maxSdkVersion',10000)))
 desc=Text();desc.feed(loc(m.get('description')));icon=loc(m.get('icon',{}));shots=loc(m.get('screenshots',{}).get('phone',{})) or []
 apps.append(dict(repo='fdroid/'+pkg,package=pkg,source='fdroid',addedAt=datetime.datetime.fromtimestamp(m.get('added',0)/1000,datetime.timezone.utc).isoformat() if m.get('added') else '',name=loc(m.get('name')) or pkg,description=loc(m.get('summary')),longDescription=''.join(desc.text)[:6000],version=man.get('versionName',''),versionCode=man.get('versionCode',0),date=datetime.datetime.fromtimestamp(v.get('added',0)/1000,datetime.timezone.utc).strftime('%Y-%m-%d'),available=True,indexed=False,assets=assets,topics=m.get('categories',[]),license=m.get('license',''),publisher=m.get('authorName','F-Droid'),referenceMinSdk=man.get('usesSdk',{}).get('minSdkVersion',1),referenceVersion=man.get('versionName',''),website=m.get('webSite',''),sourceCode=m.get('sourceCode',''),antiFeatures=v.get('antiFeatures',{}),iconPath=icon.get('name','') if isinstance(icon,dict) else '',iconHash=icon.get('sha256','') if isinstance(icon,dict) else '',screenshots=[dict(path=x['name'],sha256=x['sha256']) for x in shots[:4]]))
(root/'app/src/main/assets/fdroid-catalog.json').write_text(json.dumps(apps,ensure_ascii=False,separators=(',',':')))
(root/'app/src/main/assets/fdroid-snapshot.json').write_text(json.dumps(dict(timestamp=entry['timestamp'],indexHash=ref['sha256'],repositorySigner='43238d512c1e5eb2d6569f4a3afbf5523418b82e0a3ed1552770abb9a9c9ccab',count=len(apps))))
print('Signed F-Droid snapshot:',len(apps),'apps')
