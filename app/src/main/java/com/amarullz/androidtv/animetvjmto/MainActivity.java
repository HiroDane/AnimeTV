package com.amarullz.androidtv.animetvjmto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.fragment.app.FragmentActivity;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity {
  public AnimeView aView;
  public static String ARG_URL=null;
  public static String ARG_TIP=null;

  public void updateInstance(Bundle savedInstanceState){
    /* Load Arguments */
    if (savedInstanceState == null) {
      Bundle extras = getIntent().getExtras();
      if(extras != null) {
        ARG_URL= extras.getString("viewurl");
        ARG_TIP= extras.getString("viewtip");
      }
      else{
        ARG_URL=null;
        ARG_TIP=null;
      }
    } else {
      ARG_URL= (String) savedInstanceState.getSerializable("viewurl");
      ARG_TIP= (String) savedInstanceState.getSerializable("viewtip");
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    updateInstance(savedInstanceState);
    aView=new AnimeView(this);

//    AnimeApi aApi=new AnimeApi(this);
//    // https://9anime.to/watch/demon-slayer-kimetsu-no-yaiba-entertainment-district-arc.vpml/ep-1
//    aApi.getData("https://9anime.to/watch/demon-slayer-kimetsu-no-yaiba-entertainment-district-arc.vpml/ep-1",result -> {
//      Log.d("ATVLOG","Result View = "+result.Text);
//      aApi.getData("https://9anime.to/watch/kizuna-no-allele.vvq72/ep-10",result2 -> {
//        Log.d("ATVLOG","Result View 2 = "+result2.Text);
//      });
//    });
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK) && aView.webView.canGoBack()) {
      aView.webView.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }
  private boolean sendKeyEvent(int code, boolean send){
    int c=0;
//    RED = 183
//    GREEN = 184
//    YELLOW = 185
//    BLUE = 186
//    PROG UP = 166
//    PROG DOWN = 167
//    EXIT = 4
//    0-9 = 7 - 16
//    INFO = 165
//    TTX = 233
//    EPG = 172
    switch(code){
      case KeyEvent.KEYCODE_ESCAPE:
      case KeyEvent.KEYCODE_BACK: c=27; break;
      case KeyEvent.KEYCODE_DPAD_UP: c=38; break;
      case KeyEvent.KEYCODE_DPAD_DOWN: c=40; break;
      case KeyEvent.KEYCODE_DPAD_LEFT: c=37; break;
      case KeyEvent.KEYCODE_DPAD_RIGHT: c=39; break;
      case KeyEvent.KEYCODE_ENTER:
      case KeyEvent.KEYCODE_DPAD_CENTER: c=13; break;
      case 166: /* PROG UP */
      case KeyEvent.KEYCODE_PAGE_UP: c=33; break;
      case 167: /* PROG DOWN */
      case KeyEvent.KEYCODE_PAGE_DOWN: c=34; break;
      case 183: /* red */
      case KeyEvent.KEYCODE_F5:
        if (send){
          aView.aApi.cleanWebView();
          aView.webView.clearCache(true);
          aView.webView.reload();
        }
        break;
    }
    if (c>0&&aView.webViewReady) {
      if (send) {
        aView.webView.evaluateJavascript("_KEYEV(" + c + ")", null);
      }
      return true;
    }
    return false;
  }

  @SuppressLint("RestrictedApi")
  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    if (sendKeyEvent(event.getKeyCode(),event.getAction() == KeyEvent.ACTION_DOWN)){
      return false;
    }
    return super.dispatchKeyEvent(event);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
    aView.webView.saveState(outState);
    aView.aApi.webView.saveState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState)
  {
    super.onRestoreInstanceState(savedInstanceState);
    aView.webView.restoreState(savedInstanceState);
    aView.aApi.webView.restoreState(savedInstanceState);
  }

  @Override
  protected void onNewIntent(Intent intent){
    super.onNewIntent(intent);
    ARG_URL= intent.getStringExtra("viewurl");
    ARG_TIP= intent.getStringExtra("viewtip");
    aView.updateArgs();
  }

}