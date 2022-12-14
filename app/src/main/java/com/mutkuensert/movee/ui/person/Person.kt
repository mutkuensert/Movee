package com.mutkuensert.movee.ui.person

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mutkuensert.movee.data.model.remote.person.PersonDetailsModel
import com.mutkuensert.movee.util.IMAGE_BASE_URL
import com.mutkuensert.movee.util.Resource
import com.mutkuensert.movee.util.SIZE_ORIGINAL
import com.mutkuensert.movee.util.Status


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Person(personId: Int?, viewModel: PersonViewModel = hiltViewModel(), navigateToMovieDetails: (movieId: Int) -> Unit){
    val personDetails = viewModel.personDetails.collectAsStateWithLifecycle()

    if(personId != null){
        viewModel.getPersonDetails(personId!!)
        PersonDetailsDataObserver(data = personDetails)
    }
}

@Composable
fun PersonDetailsDataObserver(data: State<Resource<PersonDetailsModel>>) {

    when (data.value.status) {
        Status.STANDBY -> {}

        Status.LOADING -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(50.dp))

                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    strokeWidth = 6.dp,
                    color = Color.Gray
                )
            }
        }

        Status.SUCCESS -> {
            data.value.data?.let { personDetails: PersonDetailsModel ->
                PersonDetailsItem(personDetails = personDetails)
            }
        }

        Status.ERROR -> {
            Toast.makeText(LocalContext.current, "${data.value.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun PersonDetailsItem(personDetails: PersonDetailsModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {

        personDetails.profilePath?.let { posterPathNonNull ->

            Card(
                elevation = 10.dp,
                shape = RectangleShape
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("$IMAGE_BASE_URL$SIZE_ORIGINAL${posterPathNonNull}")
                        .crossfade(true)
                        .build(),
                    loading = {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(50.dp))
                            CircularProgressIndicator(
                                color = Color.Gray,
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(Modifier.height(50.dp))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = "Tv Poster",
                    contentScale = ContentScale.FillWidth
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = personDetails.name,
                color = Color.DarkGray,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = personDetails.biography)

            Spacer(modifier = Modifier.height(15.dp))

        }
    }
}