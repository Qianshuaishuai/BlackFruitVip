package com.heiguo.blackfruitvip.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.UserBean;
import com.heiguo.blackfruitvip.bean.event.UpdateInfoEvent;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.ui.info.AddressActivity;
import com.heiguo.blackfruitvip.ui.info.VipActivity;
import com.heiguo.blackfruitvip.ui.user.ForgetActivity;
import com.heiguo.blackfruitvip.ui.user.LoginActivity;
import com.heiguo.blackfruitvip.util.CopyUtil;
import com.heiguo.blackfruitvip.util.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserBean userInfo;

    private OnFragmentInteractionListener mListener;

    private ImageView header;
    private ImageView img;
    private ImageView vip;
    private TextView phone;
    private TextView save;
    private TextView balance;
    private TextView vipDay;
    private LinearLayout phoneLayout;
    private LinearLayout changeLayout;
    private LinearLayout addressLayout;
    private LinearLayout infoLayout;
    private Button logout;
    private AlertDialog noLoginDialog;

    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
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

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        header = (ImageView) getActivity().findViewById(R.id.header);
        img = (ImageView) getActivity().findViewById(R.id.img);
        vip = (ImageView) getActivity().findViewById(R.id.go_vip);
        phone = (TextView) getActivity().findViewById(R.id.phone);
        save = (TextView) getActivity().findViewById(R.id.save);
        balance = (TextView) getActivity().findViewById(R.id.balance);
        vipDay = (TextView) getActivity().findViewById(R.id.vip_day);

        phoneLayout = (LinearLayout) getActivity().findViewById(R.id.layout_phone);
        changeLayout = (LinearLayout) getActivity().findViewById(R.id.layout_change_password);
        addressLayout = (LinearLayout) getActivity().findViewById(R.id.layout_address);
        infoLayout = (LinearLayout) getActivity().findViewById(R.id.info_layout);

        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        logout = (Button) getActivity().findViewById(R.id.logout);

        userInfo = new UserBean();

        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone();
            }
        });

        vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userInfo.getPhone().equals("未登录用户")) {
//                    T.s("请先登录");
                    noLoginDialog.show();
                    return;
                }

                if (userInfo.getVipDay() >= 0) {
                    T.s("你的Vip会员暂未过期！");
                    return;
                }

                Intent intent = new Intent(getContext(), VipActivity.class);
                startActivity(intent);
            }
        });

        changeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startForgetActivity();
            }
        });

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前登录的手机号码
                String phone = ((BlackFruitVipApplication) getActivity().getApplication()).getLoginPhone();

                if (phone == "") {
//                    T.s("请先登录");
                    noLoginDialog.show();
                    return;
                }

                Intent newIntent = new Intent(getContext(), AddressActivity.class);
                newIntent.putExtra("phone", phone);
                newIntent.putExtra("mode", Constant.ADDRESS_LIST_MODE_NORMAL);
                getActivity().startActivity(newIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_LOGOUT);
                params.addQueryStringParameter("phone", ((BlackFruitVipApplication) getActivity().getApplication()).getLoginPhone());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        CommonResponse response = gson.fromJson(result, CommonResponse.class);
                        if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                            T.s("登出成功");
                            backLoginActivity();
                        }
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
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String detail = ((BlackFruitVipApplication) getActivity().getApplication()).getShareDetail();
                CopyUtil.copy(getActivity(), detail);
                T.s("已将分享内容复制到粘帖板");
            }
        });
        initNoLoginDialog();
        updateData();
    }

    private void updateData() {
        //更新数据
        userInfo = ((BlackFruitVipApplication) getActivity().getApplication()).getUserInfo();
        phone.setText(userInfo.getPhone());
        balance.setText(Float.toString(userInfo.getBalance()) + "元");
        save.setText(Float.toString(userInfo.getSaveCount()) + "元");
        vipDay.setText(Integer.toString(userInfo.getVipDay()) + "天");
        if (userInfo.getVipDay() <= 0) {
            vipDay.setText("暂未开通");
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + Constant.CONNECTION_PHONE);
        intent.setData(data);
        startActivity(intent);
    }

    private void backLoginActivity() {
        Intent newIntent = new Intent(getContext(), LoginActivity.class);
        startActivity(newIntent);
        getActivity().finish();
    }

    private void startForgetActivity() {
        Intent newIntent = new Intent(getContext(), ForgetActivity.class);
        newIntent.putExtra("forget-mode", "1");
        startActivity(newIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateInfoEvent event) {
        updateData();
    }

    private void initNoLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_user_common, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        noLoginDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText("你还未登录，是否前去登录？");
        Button leftButton = (Button) view.findViewById(R.id.left);
        Button rightButton = (Button) view.findViewById(R.id.right);

        leftButton.setText("继续浏览");
        rightButton.setText("前往登录");

        leftButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                noLoginDialog.cancel();
            }
        });

        rightButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                noLoginDialog.cancel();
                startLoginActivity();
            }
        });

        noLoginDialog.setCancelable(false);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
