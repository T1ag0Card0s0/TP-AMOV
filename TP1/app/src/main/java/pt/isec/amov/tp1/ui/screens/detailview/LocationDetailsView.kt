package pt.isec.amov.tp1.ui.screens.detailview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.ui.screens.login_register.CreditCard
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

@Composable
fun LocationDetailsView(
   location: Location,
    currentUserEmail:String,
    modifier: Modifier = Modifier,
    onValidate: (Location)->Unit
) {

    val geoPoint = GeoPoint(location.latitude, location.longitude)

    LazyColumn(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = location.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            Card (modifier = modifier.fillMaxWidth()){
                AsyncImage(
                    model = location.imageUri,
                    contentDescription = "Location Image",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            CreditCard(credit = stringResource(R.string.description) + ": " + location.description)

            Card(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
            ) {

                AndroidView(
                    factory = { context ->
                        val mapView = MapView(context).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setCenter(geoPoint)
                            controller.setZoom(13.0)
                            overlays.add(
                                Marker(this).apply {
                                    position = geoPoint
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    title = location.name
                                }
                            )
                        }
                        mapView
                    },
                    update = { view ->
                        view.controller.setCenter(geoPoint)
                    }
                )

            }

            if(!location.isAproved()) {
                Card(modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.validate))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        val progress = remember {
                            mutableStateOf(location.numberOfValidations().toFloat() / 2)
                        }

                        LinearProgressIndicator(
                            progress.value,
                            modifier = modifier
                                .border(1.dp, Color.Gray)
                                .height(10.dp)
                            )
                        if(location.canApprove(currentUserEmail)) {
                            IconButton(onClick = {
                                location.assignValidation(viewModel.user.value!!.email)
                                viewModel.updateLocation(location)
                                progress.value = location.numberOfValidations().toFloat() / 2
                            }) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            }
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.submited_by) + " " + location.authorEmail,
                    fontWeight = FontWeight.Light,
                    modifier = modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}