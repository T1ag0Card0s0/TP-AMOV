package pt.isec.amov.tp1.ui.screens.mapviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import pt.isec.amov.tp1.data.Local

@Composable
fun LocalMapView(
    initCenteredGeoPoint: GeoPoint?,
    locals: List<Local>,
    modifier: Modifier = Modifier
){
   Box(
       modifier = modifier
           .padding(8.dp)
           .fillMaxSize()
           .clipToBounds()
           .background(Color(255, 240, 128)),
   ){
       AndroidView(
           factory={context->
               val mapView = MapView(context).apply {
                   setTileSource(TileSourceFactory.MAPNIK)
                   setMultiTouchControls(true)
                   controller.setCenter(initCenteredGeoPoint)
                   controller.setZoom(12.0)
                   for (local in locals) {
                       overlays.add(
                           Marker(this).apply {
                               position = GeoPoint(local.latitude, local.longitude)
                               setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                               title = local.name
                           }
                       )
                   }
               }
               mapView
           },
           update = { view ->
               view.controller.setCenter(initCenteredGeoPoint)
           }
       )
   }
}