import subprocess,zipfile,tempfile,json,pathlib
root=pathlib.Path(__file__).resolve().parents[1];jar=root/'build/entry.jar';cp=root/'build/trust'
def verify(p):return subprocess.run(['java','-cp',str(cp),'app.kivo.store.RepoTrust',str(p)],capture_output=True)
r=verify(jar);assert r.returncode==0;entry=json.loads(r.stdout);assert entry['index']['size']>0
with tempfile.TemporaryDirectory() as d:
 unsigned=pathlib.Path(d)/'unsigned.jar'
 with zipfile.ZipFile(unsigned,'w') as z:z.writestr('entry.json',r.stdout)
 assert verify(unsigned).returncode!=0,'Unsigned metadata accepted'
 bad=pathlib.Path(d)/'tampered.jar'
 with zipfile.ZipFile(jar) as src,zipfile.ZipFile(bad,'w') as dst:
  for n in src.namelist():dst.writestr(n,src.read(n).replace(b'30000',b'30001') if n=='entry.json' else src.read(n))
 assert verify(bad).returncode!=0,'Tampered signed entry accepted'
print('PASS: official pinned repository signature accepted; unsigned and modified entries rejected.')
