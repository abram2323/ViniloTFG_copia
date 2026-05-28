// Asegúrate de tener estos imports
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController // IMPORTANTE
import com.example.vinilotfg.ui.AppFooter
import com.example.vinilotfg.ui.AppHeader
import com.example.vinilotfg.viewmodel.VinylViewModel

@Composable
fun DireccionesScreen(viewModel: VinylViewModel, navController: NavController) { // Añadido navController
    val direcciones by viewModel.direcciones.collectAsState()
    val fondoOscuro = Color(0xFF120338)

    LaunchedEffect(Unit) {
        viewModel.obtenerDirecciones()
    }

    LaunchedEffect(Unit) {
        android.util.Log.d("DEBUG_DIRECCIONES", "Entrando en la pantalla, llamando a obtenerDirecciones")
        viewModel.obtenerDirecciones()
    }
    Scaffold(
        topBar = { AppHeader(title = "Vinyl Sounds") },
        bottomBar = { AppFooter(navController) }
    ) { paddingValues ->
        // Usamos paddingValues para que el contenido no se solape
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(direcciones) { dir ->
                Card(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = dir.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(text = dir.nombre)
                        Text(text = "${dir.linea1}, ${dir.pais}")
                    }
                }
            }
        }
    }
}