package han.kunpeng.navigator;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    private AMap mAMap;

    TabLayout mTabLayout;
    TabLayout.Tab mTabFood;
    TabLayout.Tab mTabCarPark;
    TabLayout.Tab mTabGasStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写

        init();
    }

    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
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
}
