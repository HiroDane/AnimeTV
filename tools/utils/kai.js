$ap('https://raw.githubusercontent.com/amarullz/kaicodex/refs/heads/main/generated/kai_codex.js?'+$time(),function(r){
  if (r.ok){
    try{
      eval(r.responseText+"\n\nwindow.KAICODEX=KAICODEX;");
    }catch(e){}
  }
});