package han.kunpeng.navigator;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.CameraPosition;

import static han.kunpeng.navigator.util.Constants.BEIJING;

/**
 * 基本地图（SupportMapFragment）实现
 */
public class SupportMapFragmentActivity extends BaseActivity {
    private AMap mMap = null;
    private AMapOptions mAMapOptions = new AMapOptions()
            .zoomGesturesEnabled(true) // 通过手势缩放地图
            .scrollGesturesEnabled(true) // 通过手势移动地图
            .tiltGesturesEnabled(true) // 禁止通过手势倾斜地图
            .camera(new CameraPosition.Builder()
                    .target(BEIJING) // 经纬度
                    .zoom(18) // 缩放
                    .tilt(30) // 倾斜
                    .bearing(0) // 旋转
                    .build());
    private static final String MAP_FRAGMENT_TAG = "map";
    private SupportMapFragment mSupportMapFragment = null;
    private final FragmentManager mFragmentManager = getSupportFragmentManager();
    private final FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_map_support);
        setTitle("SupportMapFragment");
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMap();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化 SupportMapFragment 对象
     */
    private void init() {
        if (null == mSupportMapFragment) {
            mSupportMapFragment = SupportMapFragment.newInstance(mAMapOptions);
//            mFragmentTransaction.add(android.R.id.content, mSupportMapFragment, MAP_FRAGMENT_TAG);
            mFragmentTransaction.add(R.id.content_fragment_map_support, mSupportMapFragment, MAP_FRAGMENT_TAG);
            mFragmentTransaction.commit();
        }
    }

    /**
     * 初始化 AMap 对象
     */
    private void initMap() {
        if (null == mMap) {
            mMap = mSupportMapFragment.getMap();
        }
    }
}
