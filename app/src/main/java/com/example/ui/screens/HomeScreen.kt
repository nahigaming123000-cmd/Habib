package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToGenerator: (String) -> Unit,
    onNavigateToWallet: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        containerColor = BlueBackground,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier.border(1.dp, Slate100)
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = BluePrimary) },
                    label = { Text("Home", color = BluePrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.History, contentDescription = "History", tint = Slate400) },
                    label = { Text("History", color = Slate400, fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToWallet,
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Wallet", tint = Slate400) },
                    label = { Text("Wallet", color = Slate400, fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = Slate400) },
                    label = { Text("Profile", color = Slate400, fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = BluePrimary)
                            .background(
                                brush = Brush.linearGradient(listOf(IndigoPrimary, BluePrimary)),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("N", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("NAHI AI", color = BluePrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
                        Text("App Builder", color = Color(0xFF1A1C1E), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Row(
                    modifier = Modifier
                        .clickable { onNavigateToWallet() }
                        .background(Color.White, RoundedCornerShape(50))
                        .border(1.dp, Blue50, RoundedCornerShape(50))
                        .padding(start = 8.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier.size(24.dp).background(BlueSecondary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("${state.userProfile?.credits ?: 0} Credits", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "CREATE NEW PROMPT",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate500,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GeneratorCard(
                        title = "Mobile App",
                        subtitle = "Android & iOS prompts",
                        icon = Icons.Default.PhoneAndroid,
                        iconBgColor = Blue50,
                        iconColor = BluePrimary,
                        modifier = Modifier.weight(1f).aspectRatio(1f),
                        onClick = { onNavigateToGenerator("App") }
                    )
                    GeneratorCard(
                        title = "Website",
                        subtitle = "React & Web prompts",
                        icon = Icons.Default.Web,
                        iconBgColor = Indigo50,
                        iconColor = IndigoSecondary,
                        modifier = Modifier.weight(1f).aspectRatio(1f),
                        onClick = { onNavigateToGenerator("Website") }
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "RECENT HISTORY",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate500,
                        letterSpacing = 1.sp
                    )
                    Text("View All", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary, modifier = Modifier.clickable { })
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.history.take(5)) { history ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .border(1.dp, Slate100, RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp).background(Slate50, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.History, contentDescription = null, tint = Slate400, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(history.projectName.ifBlank { history.idea }, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Generated for ${history.type}", fontSize = 11.sp, color = Slate400)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.background(Green50, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                                Text("Done", fontSize = 12.sp, color = Green600, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                            }
                        }
                    }
                    
                    if (state.history.isEmpty()) {
                        item {
                            Text("No recent prompts. Start generating!", fontSize = 14.sp, color = Slate500, modifier = Modifier.padding(top = 16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GeneratorCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconBgColor: Color, iconColor: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(28.dp), spotColor = Slate100)
            .background(Color.White, RoundedCornerShape(28.dp))
            .border(1.dp, Slate100, RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(iconBgColor, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Column {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
                Text(subtitle, fontSize = 12.sp, color = Slate500, lineHeight = 14.sp)
            }
        }
    }
}
