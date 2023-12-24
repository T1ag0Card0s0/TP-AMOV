package pt.isec.amov.tp1.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import pt.isec.amov.tp1.data.Local
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel


@Composable
fun ChooseCoordinates(
    locationViewModel: LocalViewModel,
    viewModel: AppViewModel,
    locals: List<Local>,
    modifier: Modifier = Modifier
) {
    val location = locationViewModel.currentLocation.observeAsState()
    val geoPoint by remember {
        mutableStateOf(
            GeoPoint(
                location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
            )
        )
    }
    var latitude by remember {
        mutableDoubleStateOf(0.0)
    }
    var longitude by remember {
        mutableDoubleStateOf(0.0)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Lat: ${latitude ?: "--"}")
            Text(text = "Lon: ${longitude ?: "--"}")
        }
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .clipToBounds()
                .background(Color(255, 240, 128)),
        ) {
            AndroidView(
                factory = { context ->
                    val mapView = MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setCenter(geoPoint)
                        controller.setZoom(18.0)
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
                    val tapOverlay = MapEventsOverlay(object: MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                            viewModel.addLocalForm!!.latitude = p!!.latitude
                            viewModel.addLocalForm!!.longitude = p.longitude
                            // Remove only the marker with the title "Selected Position"
                            mapView.overlays.removeAll { it is Marker && it.title == "Selected Position" }

                            // Add a new marker for the tapped position
                            mapView.overlays.add(
                                Marker(mapView).apply {
                                    position = p
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    title = "Selected Position"
                                }
                            )
                            return true
                        }
                        override fun longPressHelper(p: GeoPoint?): Boolean {
                            // Do whatever
                            return true
                        }
                    })

                    mapView.overlays.add(tapOverlay)
                    mapView
                },
                update = { view ->
                    view.controller.setCenter(geoPoint)
                }
            )
        }
    }
}


