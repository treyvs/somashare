package com.example.somashare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.somashare.data.model.PastPaper
import com.example.somashare.util.toDateString

@Composable
fun PaperCard(
    paper: PastPaper,
    onView: () -> Unit,
    onDownload: (() -> Unit)? = null,
    showActions: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onView,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // PDF Icon
            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = null,
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(40.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                // Unit Badge
                Surface(
                    color = Color(0xFFEEF2FF),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = paper.unitCode,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Paper Name
                Text(
                    text = paper.paperName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Metadata
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = paper.paperType.displayName,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text("•", fontSize = 12.sp, color = Color(0xFF6B7280))
                    Text(
                        text = paper.paperYear.toString(),
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stats
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF6B7280)
                        )
                        Text(
                            text = paper.downloadCount.toString(),
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF6B7280)
                        )
                        Text(
                            text = paper.viewCount.toString(),
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    if (paper.isVerified) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF059669)
                        )
                    }
                }
            }

            if (showActions) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onView,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = "View",
                            tint = Color(0xFF4F46E5)
                        )
                    }

                    if (onDownload != null) {
                        IconButton(
                            onClick = onDownload,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.Download,
                                contentDescription = "Download",
                                tint = Color(0xFF4F46E5)
                            )
                        }
                    }
                }
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF)
                )
            }
        }
    }
}
