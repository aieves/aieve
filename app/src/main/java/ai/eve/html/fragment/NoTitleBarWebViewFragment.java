package ai.eve.html.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ai.eve.html.webutil.EWebView;


public class NoTitleBarWebViewFragment extends BaseFragment {

	private EWebView mWebView;
	
	private static NoTitleBarWebViewFragment fragment;
	public static NoTitleBarWebViewFragment newInstance(String title,String url) {
		fragment = new NoTitleBarWebViewFragment();
		Bundle bundle = new Bundle();
        bundle.putSerializable("title", title);
        bundle.putSerializable("url", url);
        fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mWebView = new EWebView(getActivity());
		getArguments().getSerializable("estate");
		String title = (String) getArguments().getSerializable("title");
		String url = (String) getArguments().getSerializable("url");
		if (title == null || url == null) {
		}else{
			mWebView.loadUrl(url);
		}
		return mWebView;
	}
}
