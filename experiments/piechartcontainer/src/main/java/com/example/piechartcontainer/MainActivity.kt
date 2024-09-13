package com.example.piechartcontainer

import android.content.Intent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.piechartcontainer.ui.theme.YChartsTheme
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


                        TableZoneCanvasContainer(
                            modifier = Modifier
                                .width(1440.dp)
                                .height(1080.dp),
                            widgets = listOf(
                                TableWidget(
                                    offset = Offset(100f, 100f),
                                    angle = 0f,
                                    radius = 60f,
                                    sizeType = SizeType.SMALL_CIRCLE,
                                ),
                                TableWidget(
                                    offset = Offset(300f, 100f),
                                    angle = 0f,
                                    radius = 100f,
                                    sizeType = SizeType.MEDIUM_CIRCLE,
                                ),
                                TableWidget(
                                    offset = Offset(500f, 100f),
                                    angle = 0f,
                                    radius = 140f,
                                    sizeType = SizeType.LARGE_CIRCLE,
                                ),
                                TableWidget(
                                    offset = Offset(800f, 100f),
                                    angle = 0f,
                                    width = 120f,
                                    height = 120f,
                                    sizeType = SizeType.SMALL_SQUARE,
                                ),
                                TableWidget(
                                    offset = Offset(1000f, 100f),
                                    angle = 0f,
                                    width = 200f,
                                    height = 200f,
                                    sizeType = SizeType.MEDIUM_SQUARE,
                                ),
                                TableWidget(
                                    offset = Offset(1300f, 100f),
                                    angle = 0f,
                                    width = 280f,
                                    height = 280f,
                                    sizeType = SizeType.LARGE_SQUARE,
                                ),
                                TableWidget(
                                    offset = Offset(1000f, 400f),
                                    angle = 0f,
                                    width = 260f,
                                    height = 200f,
                                    sizeType = SizeType.MEDIUM_RECT,
                                ),
                                TableWidget(
                                    offset = Offset(1300f, 400f),
                                    angle = 0f,
                                    width = 360f,
                                    height = 280f,
                                    sizeType = SizeType.LARGE_RECT,
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
