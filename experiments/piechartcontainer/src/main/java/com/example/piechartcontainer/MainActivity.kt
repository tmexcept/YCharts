package com.example.piechartcontainer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.piechartcontainer.ui.theme.YChartsTheme
import com.example.piechartcontainer.ui.theme.color_0xFFFFFFFF
import com.example.table.SizeType
import com.example.table.TableWidget
import com.example.table.TableZoneCanvasContainer
import com.example.table.WidgetColor
import com.example.table.circleLarge
import com.example.table.circleMedium
import com.example.table.circleSmall
import com.example.table.rectLarge
import com.example.table.rectMedium
import com.example.table.rectSmall

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


                        val widgets by remember {
                            val list = mutableListOf<TableWidget>()
                            list.addAll(circleSmall)
                            list.addAll(circleMedium)
                            list.addAll(circleLarge)
                            list.addAll(rectSmall)
                            list.addAll(rectMedium)
                            list.addAll(rectLarge)
                            mutableStateOf(list.toList())
                        }
                        TableZoneCanvasContainer(
                            widgets = widgets,
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
