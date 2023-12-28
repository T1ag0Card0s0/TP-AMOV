package pt.isec.amov.tp1.ui.screens.detailview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.PlaceOfInterest
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.screens.login_register.CreditCard
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.EvaluateForm

@Composable
fun PlaceOfInterestDetailsView(
    viewModel: AppViewModel,
    navController: NavHostController,
    placeOfInterest: PlaceOfInterest,
    locationName: String,
    categoryName: String,
    modifier: Modifier = Modifier
) {
    val classifications = viewModel.classifications.observeAsState()
    val myClassifications = classifications.value!!.filter{ it.placeOfInterestId == placeOfInterest.id }
    val geoPoint = GeoPoint(placeOfInterest.latitude, placeOfInterest.longitude)
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
                    text = placeOfInterest.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            Card(modifier = modifier.fillMaxWidth()) {
                AsyncImage(
                    model = placeOfInterest.imageUri,
                    contentDescription = "Location Image",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            CreditCard(credit = stringResource(R.string.description) + ": " + placeOfInterest.description)
            CreditCard(credit = stringResource(R.string.current_location) + ": " + locationName)
            CreditCard(credit = stringResource(R.string.categories) + ": " + categoryName)
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
                                    title = placeOfInterest.name
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
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.submited_by) + " " + placeOfInterest.authorEmail,
                    fontWeight = FontWeight.Light,
                    modifier = modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
            if(!placeOfInterest.approved) {
                Card {
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
                        LinearProgressIndicator(
                            placeOfInterest.numberOfValidations().toFloat() / 2,
                            modifier = modifier
                                .border(1.dp, Color.Gray)
                                .height(10.dp)
                        )
                        if (placeOfInterest.canApprove(viewModel.user.value!!.email)) {
                            IconButton(onClick = {
                                placeOfInterest.assignValidation(viewModel.user.value!!.email)
                                viewModel.updatePlaceOfInterest(placeOfInterest)
                            }) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            }
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.comments),
                    modifier = modifier.padding(top = 8.dp, bottom = 8.dp)
                )
                if (viewModel.canEvaluate()) {
                    IconButton(onClick = {
                        viewModel.evaluateForm = EvaluateForm()
                        navController.navigate(Screens.EVALUATE_PLACE_OF_INTEREST.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            }

        }
        items(
            myClassifications,
            key = { it.id }
        ) {
            Card(modifier = modifier
                .padding(8.dp)
                .fillMaxSize()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxSize()
                ) {
                    Text(text = it.authorEmail)
                    (1..3).forEach { index ->
                        if (index <= it.value) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.Yellow
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }
                    if(it.authorEmail == viewModel.user.value!!.email){
                        IconButton(onClick = { viewModel.removeClassification(it) }) {
                            Icon(imageVector = Icons.Default.RestoreFromTrash, contentDescription = null)
                        }
                    }
                }
                Text(text = it.comment,modifier = modifier.padding(8.dp))
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (it.imageUri != null) {
                        AsyncImage(
                            model = it.imageUri!!,
                            contentDescription = "Local image",
                            contentScale = ContentScale.FillWidth,
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

    }
}