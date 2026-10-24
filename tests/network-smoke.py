"""Public endpoints only. Never prints/persists authorization codes or tokens."""
import json,urllib.request,urllib.error,urllib.parse,time,hashlib
from pathlib import Path
root=Path(__file__).resolve().parents[1]
def request(url,data=None):
 req=urllib.request.Request(url,data=data,headers={'User-Agent':'Kivo-Store/0.9.2','Accept':'application/json' if data else '*/*'})
 try:
  with urllib.request.urlopen(req,timeout=25) as response:return response.status,response.read()
 except urllib.error.HTTPError as e:return e.code,e.read()
config=json.loads((root/'app/src/main/assets/community.json').read_text())
code,body=request('https://github.com/login/device/code',urllib.parse.urlencode({'client_id':config['oauthClientId'],'scope':'public_repo'}).encode())
start=json.loads(body);assert code==200 and start.get('device_code')
time.sleep(max(5,start.get('interval',5)))
code,body=request('https://github.com/login/oauth/access_token',urllib.parse.urlencode({'client_id':config['oauthClientId'],'device_code':start['device_code'],'grant_type':'urn:ietf:params:oauth:grant-type:device_code'}).encode())
reply=json.loads(body);assert reply.get('error')=='authorization_pending'
result={'deviceCodeIssued':True,'pendingHttp':code,'pendingError':reply['error'],'userAuthorizationCompleted':False}
entries=json.loads((root/'app/src/main/assets/fdroid-catalog.json').read_text())
candidates=sorted(((a,v) for a in entries for v in a['assets'] if 10000<v.get('size',0)<1000000),key=lambda x:x[1]['size'])
for a,v in candidates[:5]:
 try:
  code,body=request(v['url']);assert code==200 and len(body)==v['size'] and hashlib.sha256(body).hexdigest()==v['digest'].removeprefix('sha256:')
  result['fdroid']={'package':a['package'],'http':code,'size':len(body),'hashMatchesSignedSnapshot':True};break
 except Exception:pass
assert 'fdroid' in result,'No F-Droid APK could be verified'
code,body=request('https://api.github.com/repos/FossifyOrg/File-Manager/releases/latest');release=json.loads(body);assert code==200
asset=next(x for x in release['assets'] if x['name'].endswith('.apk'))
code,body=request(asset['browser_download_url']);assert code==200 and len(body)==asset['size'] and body[:2]==b'PK'
result['github']={'repository':'FossifyOrg/File-Manager','http':code,'size':len(body),'matchesReleaseSize':True,'zipHeader':True}
if asset.get('digest','').startswith('sha256:'):
 assert hashlib.sha256(body).hexdigest()==asset['digest'][7:];result['github']['hashMatchesPublisher']=True
result['limits']=['Python HTTP transport, not Android DownloadManager','No physical device installation','No GitHub account authorized']
(root/'validation-evidence/network-smoke.json').write_text(json.dumps(result,indent=2))
print(json.dumps(result,indent=2))
