package com.mk.wehire.User.fragments.internships;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mk.wehire.R;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.databinding.FragmentWebViewBinding;

import net.skoumal.fragmentback.BackFragment;

import java.util.Objects;


public class WebViewFragment extends Fragment implements BackFragment {


    FragmentWebViewBinding binding;
    String formLink;
    private long pressTime;
    WeHireLoader weHireLoader;

    public WebViewFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWebViewBinding.inflate(inflater,container,false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Fill Form");
        MenuItem item = toolbar.getMenu().getItem(0).setVisible(false);
        MenuItem item1 = toolbar.getMenu().getItem(1).setVisible(false);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(view1 -> {

            onBackPressed();

        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleIntent();
        weHireLoader = new WeHireLoader(getContext());

        //WebView mWebview  = binding.linkWebview;
        binding.linkWebview.loadUrl(formLink);
        WebSettings webSettings = binding.linkWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);


        binding.linkWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });



//        binding.linkWebview.loadUrl(formLink);

//        mWebview.setWebViewClient(new WebViewClient() {
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
//            }
//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                // Redirect to deprecated method, so you can use it in all SDK versions
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//            }
//        });

//        binding.linkWebview.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                weHireLoader.ShowDialog();
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                weHireLoader.DismissDialog();
//            }
//        });

        binding.linkWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()==keyEvent.ACTION_DOWN){
                    switch (i){
                        case KeyEvent.KEYCODE_BACK:
                            if (binding.linkWebview.canGoBack()){
                                binding.linkWebview.goBack();
                            }
                    }
                }
                return false;
            }
        });

//        setContentView(mWebview);
    }

//    public class myWebClient extends WebViewClient {
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            // TODO Auto-generated method stub
//            super.onPageStarted(view, url, favicon);
//            //weHireLoader.ShowDialog();
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            //weHireLoader.DismissDialog();
//        }
//
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
//            }
//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                // Redirect to deprecated method, so you can use it in all SDK versions
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//            }
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            // TODO Auto-generated method stub
//
//            view.loadUrl(formLink);
//            return true;
//
//        }
//    }

    private void handleIntent() {
        formLink =getArguments().getString("formLink");
    }


    @Override
    public boolean onBackPressed() {

//        if (pressTime+2000>System.currentTimeMillis()){
//            requireActivity().onBackPressed();
//        }
//        pressTime = System.currentTimeMillis();


        InternDetailsFragment iDFrag = new InternDetailsFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_cont,iDFrag);
        transaction.commit();
        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}