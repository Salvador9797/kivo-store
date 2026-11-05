const assert=require('node:assert/strict'),fs=require('node:fs'),crypto=require('node:crypto'),{JSDOM}=require('jsdom');
const html=fs.readFileSync('app/src/main/assets/index.html','utf8'),calls=[];
const dom=new JSDOM(html,{url:'https://kivo.local/',runScripts:'dangerously',beforeParse(w){w.matchMedia=()=>({matches:false});w.scrollTo=()=>{};w.confirm=()=>true;w.KivoNative=new Proxy({},{get:(_,key)=>(...args)=>calls.push([key,...args])});}});
const w=dom.window,d=w.document;
const policy=d.querySelector('meta[http-equiv="Content-Security-Policy"]').content;
const scripts=[...d.scripts];assert.equal(scripts.length,2);
for(const script of scripts){const hash=crypto.createHash('sha256').update(script.textContent).digest('base64');assert(policy.includes(`'sha256-${hash}'`),'Bundled scripts must match CSP');}
assert(!/script-src[^;]*unsafe-inline/.test(policy));
for(const node of d.querySelectorAll('*'))for(const attr of node.attributes)assert(!/^on/i.test(attr.name),'No executable HTML event attributes');
w.KivoReceive('device',{abis:['arm64-v8a'],sdk:35});
w.KivoReceive('catalog',[{repo:'test/arm-only',name:'ARMv7 only',available:true,assets:[{id:1,name:'demo-arm-v7.apk'}]}]);
d.querySelector('[data-repo="test/arm-only"]').click();assert(!d.querySelector('#primary-app-action [data-asset]'),'ARMv7 must not be recommended on ARM64-only devices');
w.KivoBack();w.KivoReceive('catalog',[{repo:'test/arm-only',name:'ARMv7 only',available:true,assets:[{id:2,name:'demo-arm64-v8a.apk'}]}]);d.querySelector('[data-repo="test/arm-only"]').click();assert(d.querySelector('[data-asset="2"]'));
const img=d.querySelector('.logo-icon img');assert(img);img.dispatchEvent(new w.Event('error'));assert(!d.contains(img),'Failed icon removed by listener');
d.querySelector('[data-account]').click();d.querySelector('[data-login]').click();assert.deepEqual(calls.at(-1),['login'],'Regular login uses read-only route');
w.KivoReceive('downloads',[{id:45,repo:'test/arm-only',name:'demo.apk',state:'downloading',bytes:5,total:10}]);assert(!d.querySelector('#system-downloads'),'Transfer status does not navigate away from account');
dom.window.close();console.log('PASS: hash CSP, no inline handlers, ABI rejection, icon fallback, ordinary login and no forced download navigation.');
