package com.heiguo.blackfruitvip.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.OrderAdapter;
import com.heiguo.blackfruitvip.bean.OrderBean;
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.bean.UserBean;
import com.heiguo.blackfruitvip.bean.event.OrderStatusEvent;
import com.heiguo.blackfruitvip.bean.event.UpdateInfoEvent;
import com.heiguo.blackfruitvip.response.AddressListResponse;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.response.OrderListResponse;
import com.heiguo.blackfruitvip.ui.order.OrderDetailActivity;
import com.heiguo.blackfruitvip.util.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView leftTextView;
    private TextView middleTextView;
    private TextView rightTextView;
    private TextView noneTipTextView;

    private View leftView;
    private View middleView;
    private View rightView;

    private SwipeRefreshLayout srl;
    private RecyclerView orderList;
    private OrderAdapter adapter1;
    private OrderAdapter adapter2;
    private OrderAdapter adapter3;

    private List<OrderBean> allList;
    private List<OrderBean> list1;
    private List<OrderBean> list2;
    private List<OrderBean> list3;

    private UserBean userBean;

    private int order_type = 0;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        userBean = ((BlackFruitVipApplication) getActivity().getApplication()).getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getOrderList();
    }

    private void getOrderList() {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ORDER_LIST);
        params.addQueryStringParameter("phone", userBean.getPhone());
        params.addQueryStringParameter("status", 0);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                OrderListResponse response = gson.fromJson(result, OrderListResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    allList = response.getF_data();
                    updateList();
                } else {
                    T.s("获取订单列表失败");
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
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }
        });
    }

    private void updateList() {
        list1.clear();
        list2.clear();
        list3.clear();
        for (int a = 0; a < allList.size(); a++) {
            switch (allList.get(a).getStatus()) {
                case 2:
                    list1.add(allList.get(a));
                    break;
                case 3:
                    list2.add(allList.get(a));
                    break;
                case 4:
                    list2.add(allList.get(a));
                    break;
                case 5:
                    list3.add(allList.get(a));
                    break;
            }
        }

        if (order_type == 0) {
            Bundle bundle = getArguments();
            int status = bundle.getInt("order-status", 0);

            switch (status) {
                case 0:
                    order_type = 0;
                    break;
                case 2:
                    order_type = 0;
                    break;
                case 3:
                    order_type = 1;
                    break;
                default:
                    order_type = 2;
                    break;
            }
        }


        switch (order_type) {
            case 0:
                adapter1.notifyDataSetChanged();
                orderList.setAdapter(adapter1);

                if (list1.size() <= 0) {
                    orderList.setVisibility(View.GONE);
                    noneTipTextView.setVisibility(View.VISIBLE);
                } else {
                    orderList.setVisibility(View.VISIBLE);
                    noneTipTextView.setVisibility(View.GONE);
                }

                leftTextView.setTextColor(getActivity().getResources().getColor(R.color.colorYellow));
                leftView.setVisibility(View.VISIBLE);
                middleTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                middleView.setVisibility(View.GONE);
                rightTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                rightView.setVisibility(View.GONE);

                break;
            case 1:
                adapter2.notifyDataSetChanged();
                orderList.setAdapter(adapter2);

                if (list2.size() <= 0) {
                    orderList.setVisibility(View.GONE);
                    noneTipTextView.setVisibility(View.VISIBLE);
                } else {
                    orderList.setVisibility(View.VISIBLE);
                    noneTipTextView.setVisibility(View.GONE);
                }

                leftTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                leftView.setVisibility(View.GONE);
                middleTextView.setTextColor(getActivity().getResources().getColor(R.color.colorYellow));
                middleView.setVisibility(View.VISIBLE);
                rightTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                rightView.setVisibility(View.GONE);
                break;
            case 2:
                adapter3.notifyDataSetChanged();
                orderList.setAdapter(adapter3);

                if (list3.size() <= 0) {
                    orderList.setVisibility(View.GONE);
                    noneTipTextView.setVisibility(View.VISIBLE);
                } else {
                    orderList.setVisibility(View.VISIBLE);
                    noneTipTextView.setVisibility(View.GONE);
                }

                leftTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                leftView.setVisibility(View.GONE);
                middleTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                middleView.setVisibility(View.GONE);
                rightTextView.setTextColor(getActivity().getResources().getColor(R.color.colorYellow));
                rightView.setVisibility(View.VISIBLE);
                order_type = 2;
                break;
        }
    }

    private void initView() {
        orderList = (RecyclerView) getActivity().findViewById(R.id.order_list);
        srl = (SwipeRefreshLayout) getActivity().findViewById(R.id.srl);

        leftTextView = (TextView) getActivity().findViewById(R.id.order_left);
        middleTextView = (TextView) getActivity().findViewById(R.id.order_middle);
        rightTextView = (TextView) getActivity().findViewById(R.id.order_right);
        noneTipTextView = (TextView) getActivity().findViewById(R.id.none_tip);

        leftView = (View) getActivity().findViewById(R.id.left_view);
        middleView = (View) getActivity().findViewById(R.id.middle_view);
        rightView = (View) getActivity().findViewById(R.id.right_view);

        leftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order_type != 0) {
                    leftTextView.setTextColor(getActivity().getResources().getColor(R.color.colorYellow));
                    leftView.setVisibility(View.VISIBLE);
                    middleTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                    middleView.setVisibility(View.GONE);
                    rightTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                    rightView.setVisibility(View.GONE);
                    order_type = 0;
                    changeOrderList();
                }
            }
        });

        middleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order_type != 1) {
                    leftTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                    leftView.setVisibility(View.GONE);
                    middleTextView.setTextColor(getActivity().getResources().getColor(R.color.colorYellow));
                    middleView.setVisibility(View.VISIBLE);
                    rightTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                    rightView.setVisibility(View.GONE);
                    order_type = 1;
                    changeOrderList();
                }
            }
        });

        rightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order_type != 2) {
                    leftTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                    leftView.setVisibility(View.GONE);
                    middleTextView.setTextColor(getActivity().getResources().getColor(R.color.colorTv));
                    middleView.setVisibility(View.GONE);
                    rightTextView.setTextColor(getActivity().getResources().getColor(R.color.colorYellow));
                    rightView.setVisibility(View.VISIBLE);
                    order_type = 2;
                    changeOrderList();
                }
            }
        });

        allList = new ArrayList<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();

        adapter1 = new OrderAdapter(this, list1);
        adapter2 = new OrderAdapter(this, list2);
        adapter3 = new OrderAdapter(this, list3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        adapter1.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startOrderDetailActivity(list1.get(position).getId());
            }
        });

        adapter2.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startOrderDetailActivity(list2.get(position).getId());
            }
        });

        adapter3.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startOrderDetailActivity(list3.get(position).getId());
            }
        });

        orderList.setLayoutManager(layoutManager);
        orderList.setAdapter(adapter1);

        srl.setColorSchemeResources(R.color.colorYellow, R.color.colorYellow, R.color.colorYellow, R.color.colorYellow, R.color.colorYellow);
        /*
         * 设置下拉刷新的监听
         */
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderList();
            }
        });
    }

    private void startOrderDetailActivity(long orderId) {
        Intent intent = new Intent(this.getActivity(), OrderDetailActivity.class);
        intent.putExtra("order-id", orderId);
        intent.putExtra("order-mode", Constant.ORDER_MODE_MAIN);
        startActivity(intent);
    }

    public void completeOrder(long orderId) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ORDER_COMPLETE);
        params.addQueryStringParameter("id", orderId);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    T.s("确认收货成功");
                    getOrderList();
                    ((BlackFruitVipApplication) getActivity().getApplication()).updateUserInfo();
                } else {
                    T.s("确认收货失败");
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

    public void changeOrderList() {
        switch (order_type) {
            case 0:
                adapter1.notifyDataSetChanged();
                orderList.setAdapter(adapter1);

                if (list1.size() <= 0) {
                    orderList.setVisibility(View.GONE);
                    noneTipTextView.setVisibility(View.VISIBLE);
                } else {
                    orderList.setVisibility(View.VISIBLE);
                    noneTipTextView.setVisibility(View.GONE);
                }
                break;
            case 1:
                adapter2.notifyDataSetChanged();
                orderList.setAdapter(adapter2);

                if (list2.size() <= 0) {
                    orderList.setVisibility(View.GONE);
                    noneTipTextView.setVisibility(View.VISIBLE);
                } else {
                    orderList.setVisibility(View.VISIBLE);
                    noneTipTextView.setVisibility(View.GONE);
                }
                break;
            case 2:
                adapter3.notifyDataSetChanged();
                orderList.setAdapter(adapter3);

                if (list3.size() <= 0) {
                    orderList.setVisibility(View.GONE);
                    noneTipTextView.setVisibility(View.VISIBLE);
                } else {
                    orderList.setVisibility(View.VISIBLE);
                    noneTipTextView.setVisibility(View.GONE);
                }
                break;
        }
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
