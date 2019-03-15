package han.kunpeng.navigator;

import android.location.Location;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;

public class MainActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {
    private static final String TAG = "amap";
    private MapView mMapView;
    private AMap mAMap;
    private MyLocationStyle mMyLocationStyle;

    private TabLayout mTabLayout;
    private TabLayout.Tab mTabFood;
    private TabLayout.Tab mTabCarPark;
    private TabLayout.Tab mTabGasStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState); // 此方法必须重写

        init();
    }

    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        }

        mTabLayout = findViewById(R.id.tab_layout);
        mTabFood = mTabLayout.newTab().setText(R.string.tab_food);
        mTabCarPark = mTabLayout.newTab().setText(R.string.tab_car_park);
        mTabGasStation = mTabLayout.newTab().setText(R.string.tab_gas_station);
        mTabLayout.addTab(mTabFood);
        mTabLayout.addTab(mTabCarPark);
        mTabLayout.addTab(mTabGasStation);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mTabFood == tab) {
                    Toast.makeText(getApplicationContext(), R.string.tab_food, Toast.LENGTH_SHORT).show();
                }
                if (mTabCarPark == tab) {
                    Toast.makeText(getApplicationContext(), R.string.tab_car_park, Toast.LENGTH_SHORT).show();
                }
                if (mTabGasStation == tab) {
                    Toast.makeText(getApplicationContext(), R.string.tab_gas_station, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * 设置一些 AMap 的属性
     */
    private void setUpMap() {
        // 设置默认定位状态
        mMyLocationStyle = new MyLocationStyle().myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        mAMap.setMyLocationStyle(mMyLocationStyle);

        // 设置默认定位按钮是否显示
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);

        // 默认为 false 表示隐藏定位层并不可触发定位
        // 设置为 true 表示显示定位层并可触发定位
        mAMap.setMyLocationEnabled(true);

        // 设置 SDK 自带定位消息监听
        mAMap.setOnMyLocationChangeListener(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
            Log.e(TAG, "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为 GPS、WIFI 等，具体可以参考官网的定位 SDK 介绍。
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e(TAG, "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e(TAG, "定位信息， bundle is null ");

            }

        } else {
            Log.e(TAG, "定位失败");
        }
    }
}
