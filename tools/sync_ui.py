"""Generate bundled scripts and a hash-only script CSP from their canonical sources."""
from pathlib import Path
import base64,hashlib,re,sys
assets=Path(__file__).resolve().parents[1]/'app/src/main/assets'
page=assets/'index.html'
original=page.read_text()
sources=[(assets/name).read_text() for name in ('lucide.js','app.js')]
if any('</script' in source.lower() for source in sources):
 raise SystemExit('A script source contains an HTML closing script tag')
iterator=iter(sources)
updated,count=re.subn(r'<script>.*?</script>',lambda _: '<script>'+next(iterator)+'</script>',original,flags=re.S)
if count!=2:raise SystemExit('Expected exactly two bundled script elements')
hashes=['sha256-'+base64.b64encode(hashlib.sha256(s.encode()).digest()).decode() for s in sources]
policy="script-src "+' '.join("'"+h+"'" for h in hashes)
updated=re.sub(r"script-src [^;\"]+",policy,updated,count=1)
if '--check' in sys.argv:
 if updated!=original:raise SystemExit('Bundled UI differs from canonical sources; run python3 tools/sync_ui.py')
else:page.write_text(updated)
