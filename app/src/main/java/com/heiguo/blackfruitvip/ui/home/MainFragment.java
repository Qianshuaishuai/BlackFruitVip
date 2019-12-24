package com.heiguo.blackfruitvip.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.MenuAdapter;
import com.heiguo.blackfruitvip.adapter.PicAdapter;
import com.heiguo.blackfruitvip.bean.MainBean;
import com.heiguo.blackfruitvip.response.MainResponse;
import com.heiguo.blackfruitvip.ui.info.CityActivity;
import com.heiguo.blackfruitvip.util.BannerImageLoader;
import com.heiguo.blackfruitvip.util.T;
import com.youth.banner.Banner;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Banner banner;
    private GridView menu;
    private MenuAdapter menuAdapter;
    private PicAdapter picAdapter;
    private RecyclerView picRecycleView;
    private LinearLayout cityLayout;
    private TextView cityTip;
    private ImageView search;

    private List<String> bannerImages = new ArrayList<>();
    private List<MainBean> adList = new ArrayList<>();
    private List<MainBean> menuList = new ArrayList<>();
    private List<MainBean> picList = new ArrayList<>();

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        loadMainData();
    }

    private void initBaseView() {
        cityLayout = (LinearLayout) getActivity().findViewById(R.id.city);
        cityTip = (TextView) getActivity().findViewById(R.id.city_tip);
        search = (ImageView) getActivity().findViewById(R.id.search);

        cityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCityActivity();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void startCityActivity(){
        Intent intent = new Intent(getContext(), CityActivity.class);
        startActivity(intent);
    }

    private void initBanner() {
        banner = getActivity().findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        banner.setImages(bannerImages);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initMenu() {
        menu = getActivity().findViewById(R.id.menu);
        menuAdapter = new MenuAdapter(this.getActivity(), menuList, new MenuAdapter.MenuClickListener() {
            @Override
            public void clickListener(View v) {
                MenuAdapter.ViewHolder holder = (MenuAdapter.ViewHolder) v.getTag();
                System.out.println(holder.menuImg.getTag());
            }
        });
        menu.setAdapter(menuAdapter);
    }

    private void loadMainData() {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_MAIN_ALL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                MainResponse response = gson.fromJson(result, MainResponse.class);

                List<MainBean> list = response.getF_data();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getType() == 1) {
                        adList.add(list.get(i));
                        bannerImages.add(list.get(i).getImage());
                    } else if (list.get(i).getType() == 2) {
                        menuList.add(list.get(i));
                    } else if (list.get(i).getType() == 3) {
                        picList.add(list.get(i));
                    }
                }

                initBanner();
                initMenu();
                initPic();
                initBaseView();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                T.s("请求出错，请检查网络");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initPic() {
        picRecycleView = (RecyclerView) getActivity().findViewById(R.id.pic);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        picRecycleView.setLayoutManager(layoutManager);
        picAdapter = new PicAdapter(picList);
        picRecycleView.setAdapter(picAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
