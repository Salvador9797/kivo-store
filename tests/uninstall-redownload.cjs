const assert=require('node:assert/strict'),fs=require('node:fs'),{JSDOM}=require('jsdom');
const calls=[],dom=new JSDOM(fs.readFileSync('app/src/main/assets/index.html','utf8'),{url:'https://kivo.local/',runScripts:'dangerously',beforeParse(w){w.matchMedia=()=>({matches:false});w.scrollTo=()=>{};w.confirm=()=>true;w.KivoNative=new Proxy({},{get:(_,k)=>(...args)=>calls.push([k,...args])});}});
const w=dom.window,d=w.document,app={repo:'test/tool',name:'Tool',package:'org.test.tool',version:'1',available:true,assets:[{id:7,name:'tool.apk',size:1000}]},file={repo:app.repo,id:42,assetId:7,name:'tool.apk',state:'ready',bytes:1000,total:1000,installed:false,reinstallRequired:true};
w.KivoReceive('catalog',[app]);w.KivoReceive('downloads',[file]);d.querySelector('[data-repo="test/tool"]').click();
assert(d.querySelector('#primary-app-action [data-asset="7"]'),'Cached APK after uninstall must offer Download');assert(!d.querySelector('#primary-app-action [data-install-id]'));
d.querySelector('#primary-app-action [data-asset="7"]').click();assert.deepEqual(calls.at(-1),['download',app.repo,7],'Download dispatches a new transfer');
w.KivoReceive('downloads',[file,{...file,id:43,reinstallRequired:false,autoInstall:true,autoInstallAttempted:true}]);assert(d.querySelector('#primary-app-action [data-install-id="43"]'),'A newly cancelled installation remains installable');
w.KivoReceive('downloads',[file,{...file,id:43,reinstallRequired:false,state:'downloading',bytes:10}]);assert(d.querySelector('#primary-app-action progress'),'An older APK cannot hide current download progress');
dom.window.close();console.log('PASS: Download after uninstall, cached file exclusion, fresh transfer and pending installation.');
