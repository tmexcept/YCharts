package com.example.piechartcontainer

import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.piechartcontainer.ui.theme.YChartsTheme
import com.example.table.LayoutSize
import com.example.table.SizeType
import com.example.table.TableWidget
import com.example.table.TableZoneCanvasContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YChartsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Charts")
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(), onClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    BarChartActivity::class.java
                                )
                            )
                        }) {
                            Text(text = "Bar Chart")
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(), onClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    PieChartActivity::class.java
                                )
                            )
                        }) {
                            Text(text = "Pie Chart")
                        }

                        val context = LocalContext.current
                        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_combine_empty)
                        val layoutSize = LayoutSize()
                        val density = LocalDensity.current
                        val iconSize = with(density) { layoutSize.iconSize.toPx().toInt() }
                        val combineBitmap = drawable!!.toBitmap(iconSize, iconSize).asImageBitmap()

                        TableZoneCanvasContainer(
                            modifier = Modifier
                                .width(1440.dp)
                                .height(1080.dp),
                            layoutSize = layoutSize,
                            combineBitmap = combineBitmap,
                            widgets = listOf(
                                TableWidget(
                                    offset = Offset(100f, 100f),
                                    angle = 0f,
                                    radius = 60f,
                                    width = 60f,
                                    height = 60f,
                                    sizeType = SizeType.SMALL_CIRCLE,
                                ),
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YChartsTheme {
        Greeting("Android")
    }
}
