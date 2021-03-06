package camp.kuznetsov.rn.yandexmapkit;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

import ru.yandex.yandexmapkit.utils.GeoPoint;

public class RNYandexMapKitManager extends SimpleViewManager<RNYandexMapKitView> {
    public static final String REACT_CLASS = "RNYandexMapView";

    private static final int ANIMATE_TO_COORDINATE = 1;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected RNYandexMapKitView createViewInstance(ThemedReactContext reactContext) {
        RNYandexMapKitModule nativeModule = reactContext.getNativeModule(RNYandexMapKitModule.class);
        String apiKey = nativeModule.getApiKey();
        RNYandexMapKitView view = new RNYandexMapKitView(reactContext, apiKey);
        return view;
    }

    @ReactProp(name="showBuiltInScreenButtons", defaultBoolean = false)
    public void setShowBuiltInScreenButtons(RNYandexMapKitView mapView, Boolean showBuiltInScreenButtons){
        mapView.showBuiltInScreenButtons(showBuiltInScreenButtons);
    }

    @ReactProp(name="showFindMeButton", defaultBoolean = false)
    public void setShowFindMeButton(RNYandexMapKitView mapView, Boolean showFindMeButton){
        mapView.showFindMeButton(showFindMeButton);
    }

    @ReactProp(name="showJamsButton", defaultBoolean = false)
    public void setShowJamsButton(RNYandexMapKitView mapView, Boolean showJamsButton){
        mapView.showJamsButton(showJamsButton);
    }

    @ReactProp(name="showScaleView", defaultBoolean = false)
    public void setShowScaleView(RNYandexMapKitView mapView, Boolean showScaleView){
        mapView.showScaleView(showScaleView);
    }

    @ReactProp(name="showZoomButtons", defaultBoolean = false)
    public void setShowZoomButtons(RNYandexMapKitView mapView, Boolean showZoomButtons){
        mapView.showZoomButtons(showZoomButtons);
    }

    @ReactProp(name="showMyLocation", defaultBoolean = false)
    public void setShowMyLocation(RNYandexMapKitView mapView, Boolean showMyLocation){
        mapView.getMapController().getOverlayManager().getMyLocation().setEnabled(showMyLocation);
    }

    @ReactProp(name="interactive", defaultBoolean = true)
    public void setInteractive(RNYandexMapKitView mapView, Boolean interactive){
        mapView.getMapController().setEnabled(interactive);
    }

    @ReactProp(name="hdMode", defaultBoolean = false)
    public void setHDMode(RNYandexMapKitView mapView, Boolean hdMode){
        mapView.getMapController().setHDMode(hdMode);
    }

    @ReactProp(name="showTraffic", defaultBoolean = false)
    public void setShowTraffic(RNYandexMapKitView mapView, Boolean showTraffic){
        mapView.getMapController().setJamsVisible(showTraffic);
    }

    @ReactProp(name="nightMode", defaultBoolean = false)
    public void setNightMode(RNYandexMapKitView mapView, Boolean nightMode){
        mapView.getMapController().setNightMode(nightMode);
    }

    @ReactProp(name="geocodingEnabled", defaultBoolean = false)
    public void setGeocodingEnabled(RNYandexMapKitView mapView, Boolean geocodingEnabled){
        mapView.setGeocodingEnabled(geocodingEnabled);
    }

    @ReactProp(name="region")
    public void setRegion(RNYandexMapKitView mapView, ReadableMap region){
        GeoPoint point = new GeoPoint(region.getDouble("latitude"), region.getDouble("longitude"));
        mapView.getMapController().setPositionNoAnimationTo(point);
        mapView.getMapController().setZoomToSpan(region.getDouble("latitudeDelta"), region.getDouble("longitudeDelta"));
        GeoPoint newCenter = mapView.getMapController().getMapCenter();
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        builder.put(RNYandexMapKitView.MAP_EVENT,       MapBuilder.of("registrationName", RNYandexMapKitView.MAP_EVENT));
        builder.put(RNYandexMapKitView.GEOCODING_EVENT, MapBuilder.of("registrationName", RNYandexMapKitView.GEOCODING_EVENT));
        return builder.build();
    }


    @Override
    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
            "animateToCoordinate", ANIMATE_TO_COORDINATE
        );
    }

    @Override
    public void receiveCommand(RNYandexMapKitView view, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case ANIMATE_TO_COORDINATE:
                GeoPoint coordinate = null;
                ReadableMap latlon = args.getMap(0);
                if (latlon != null){
                    double latitude = latlon.getDouble("latitude");
                    double longitude = latlon.getDouble("longitude");
                    coordinate = new GeoPoint(latitude, longitude);
                }
                view.animateToCoordinate(coordinate);
                break;
        }
    }

}
