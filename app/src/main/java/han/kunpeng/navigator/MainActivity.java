package han.kunpeng.navigator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.MyTrafficStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import han.kunpeng.navigator.util.Trace;

import static android.app.AlertDialog.*;
import static han.kunpeng.navigator.util.Constants.MAP_DEFAULT_BEARING;
import static han.kunpeng.navigator.util.Constants.MAP_DEFAULT_TILT;
import static han.kunpeng.navigator.util.Constants.MAP_DEFAULT_ZOOM;

public class MainActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {
    private MapView mMapView;
    private AMap mAMap;
    private UiSettings mUiSettings;
    private MyLocationStyle mMyLocationStyle;
    private LatLonPoint mMyLatLonPoint = null;
    private CustomMapStyleOptions mapStyleOptions = new CustomMapStyleOptions();
    private TabLayout mTabLayout;

/*
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {

        }
    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Trace.d("onCreate - begin");
        getWindow().getAttributes().setTitle(BuildConfig.APPLICATION_ID);
/*
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });
*/

        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState); // 此方法必须重写

        init();

/*
        startActivity(new Intent(this.getApplicationContext(),
                com.amap.api.maps.offlinemap.OfflineMapActivity.class));
*/

        Trace.d("onCreate - end");
    }

    private void init() {
        initAMap();
    }

    private void initAMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();

            mAMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    setUpMap();
                }
            });
        } else {
            Trace.d("AMap != null");
        }
    }
    /**
     * 设置一些 AMap 的属性
     */
    private void setUpMap() {
        // 默认定位样式是 MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE
        // 定位频率为 1 秒 1 次，且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        mMyLocationStyle = new MyLocationStyle();

        // 连续定位，蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
        mMyLocationStyle = mMyLocationStyle.myLocationType(
                MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

        // TODO 做一个位置图标
        // mMyLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_location));

        // 设置定位蓝点的样式
        mAMap.setMyLocationStyle(mMyLocationStyle);

        // 设置为 true 表示启动显示定位蓝点
        // 设置为 false 表示隐藏定位蓝点并不进行定位
        mAMap.setMyLocationEnabled(true);

        // 实现 AMap.OnMyLocationChangeListener 监听器
        // 通过 onMyLocationChange(android.location.Location location) 回调方法获取经纬度信息
        mAMap.setOnMyLocationChangeListener(this);

        // 控制是否显示定位蓝点
        mMyLocationStyle.showMyLocation(true);

        /* 设置最小缩放级别
         * 在 SDK 地图 zoom 级别 3 和 4（世界地图级别）显示中国以外地区时，
         * 由于没有详细地图数据，显示效果看起来和地图加载失败时一样的黄白色底图，
         * 因此我们把最小缩放级别设置为 5 。
         */
        mAMap.setMinZoomLevel(5);

        // TODO 构造 GeocodeSearch 对象并设置监听
        // mGeocoderSearch = new GeocodeSearch(this);
        // mGeocoderSearch.setOnGeocodeSearchListener(this);

        mUiSettings = mAMap.getUiSettings();

        // 不显示高德自带缩放按钮
        mUiSettings.setZoomControlsEnabled(false);

        // 不显示高德自带定位按钮
        mUiSettings.setMyLocationButtonEnabled(false);

        // 把高德 Logo 挪到屏幕显示区域外
        mUiSettings.setLogoBottomMargin(-666);

        // 设置地图可以手势滑动
        mUiSettings.setScrollGesturesEnabled(true);

        // 设置地图可以手势缩放
        mUiSettings.setZoomGesturesEnabled(true);

        // 设置地图可以倾斜
        mUiSettings.setTiltGesturesEnabled(true);

        // 设置地图可以旋转
        mUiSettings.setRotateGesturesEnabled(true);

        // 自定义实时交通信息的颜色样式
        MyTrafficStyle myTrafficStyle = new MyTrafficStyle();
        myTrafficStyle.setSeriousCongestedColor(0xff92000a);
        myTrafficStyle.setCongestedColor(0xffea0312);
        myTrafficStyle.setSlowColor(0xffff7508);
        myTrafficStyle.setSmoothColor(0xff00a209);
        mAMap.setMyTrafficStyle(myTrafficStyle);
    }

    private void setMapCustomStyleFile(Context context) {
        String styleName = "style_new.data";
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(styleName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            if (mapStyleOptions != null) {
                // 设置自定义样式
                mapStyleOptions.setStyleData(b);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        Trace.d("onResume - begin");
        mMapView.onResume();
//        Log.i(TAG, "MainActivity -onResume - Timer.schedule - begin");
//        mTimer.schedule(mTimerTask, 3000, 3000);
//        Log.i(TAG, "MainActivity -onResume - Timer.schedule - end");
        Trace.d("onResume - end");
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
//        mTimer.cancel();
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (null == location) {
            Trace.e("onMyLocationChange - location is null");
        } else {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            if (isValidPoint(latitude, longitude)) {
//                Trace.d("onMyLocationChange - latitude: " + latitude + ", longitude: " + longitude);
                if (null == mMyLatLonPoint) {
                    Trace.i("onMyLocationChange - valid location - first");
                    mMyLatLonPoint = new LatLonPoint(latitude, longitude);
                    northCamera(latitude, longitude);

                    // TODO 通过网络获取所在城市
                    // getCityAsyn();

                    // 首次定位成功时初始化界面
                    initViews();
                } else {
                    mMyLatLonPoint.setLatitude(location.getLatitude());
                    mMyLatLonPoint.setLongitude(location.getLongitude());
                }
            } else {
                Trace.e("onMyLocationChange - location is invalid");
            }
        }
    }

    public boolean isValidPoint(LatLng point) {
        if (0 == Double.compare(0, point.latitude) && 0 == Double.compare(0, point.longitude)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidPoint(double latitude, double longitude) {
        if (0 == Double.compare(0, latitude) && 0 == Double.compare(0, longitude)) {
            return false;
        } else {
            return true;
        }
    }

    // 把当前经纬度点移动到屏幕中心并指北
    private void northCamera(double latitude, double longitude) {
        /* CameraPosition(LatLng target, float zoom, float tilt, float bearing) 四个参数的含义
           target 经纬度
           zoom 缩放级别
           tilt 倾斜角度
           bearing 方向角度。正北向顺时针方向计算，从 0 度到 360 度。
        */
        CameraPosition currentCameraPosition = mAMap.getCameraPosition();
        Trace.d("northCamera - currentCameraPosition: " + currentCameraPosition.toString());
        CameraPosition newCameraPosition = currentCameraPosition.builder(currentCameraPosition)
                .target(new LatLng(latitude, longitude))
                .zoom(MAP_DEFAULT_ZOOM)
                .tilt(MAP_DEFAULT_TILT)
                .bearing(MAP_DEFAULT_BEARING)
                .build();
        Trace.i("northCamera - newCameraPosition: " + newCameraPosition.toString());
        mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
    }

    private void initViews() {
        initTabs();
    }

    private void initTabs() {
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO doTabSearchQuery(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        String[] tabs = getResources().getStringArray(R.array.tabs);
        for (int i = 0; i < tabs.length; i++) {
            String tab = tabs[i];
            Trace.d("initTabs - tab: " + tab);
            mTabLayout.addTab(mTabLayout.newTab().setText(tab));
        }

        mTabLayout.setScrollPosition(0, 0, true);
    }

    public void onClickShowDefaultDialog(View view) {
        new Builder(this)
                .setTitle("默认对话框标题")
                .setMessage("这是默认对话框的内容")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "点击了确定按钮", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    public void onClickShowQMUIDialog(View view) {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("QMUI对话框标题")
                .setMessage("这是QMUI框架对话框的内容")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();

                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "点击了确定按钮", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }

    public void onClickAidl(View view) {

    }

}
