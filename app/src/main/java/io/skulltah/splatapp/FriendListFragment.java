package io.skulltah.splatapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.skulltah.splatapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendListFragment} factory method to
 * create an instance of this fragment.
 */
public class FriendListFragment extends Fragment {
    private View fragmentView;

    private OnFragmentInteractionListener mListener;

    public FriendListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_friend_list, container, false);

        tryLogin();

        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void tryLogin() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        if (prefs.getString("mii_name", "").equals("")) {
            Intent loginIntent = new Intent(MainActivity.context, SplatoonNetLoginActivity.class);
            startActivity(loginIntent);
            MainActivity.goToDefaultFragment();
        } else {
            loadHtml();
        }
    }

    private void loadHtml() {
        if (fragmentView == null) return;

        final WebView webView = (WebView) fragmentView.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webView.addJavascriptInterface(new SplatoonNetJavaScriptInterface(MainActivity.context), "HtmlViewer");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webView, url);

                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('header')[0].style.display='none';" +
                        "document.getElementsByClassName('headline')[0].style.display='none';" +
                        "document.getElementById('toggle').style.display='none';" +
                        "})()");

                fragmentView.findViewById(R.id.loading_view).setVisibility(View.GONE);
//                Toast.makeText(MainActivity.context, "Done!", Toast.LENGTH_SHORT).show();
//
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
//                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Snackbar.make(fragmentView, "Loading...", Snackbar.LENGTH_LONG);
            }
        });

        CookieManager.getInstance().setAcceptCookie(true);

        webView.loadUrl("https://splatoon.nintendo.net/");

    }
}
