import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projekt1.adapters.ProductImageAdapter
import com.example.projekt1.Navigable
import com.example.projekt1.databinding.FragmentEditBinding
import com.example.projekt1.data.ProductDatabase
import com.example.projekt1.data.model.ProductEntity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.concurrent.thread


const val ARG_EDIT_IT = "edit_id"

class EditFragment : Fragment() {

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: ProductImageAdapter
    private lateinit var db: ProductDatabase
    private var product: ProductEntity? = null
    private var imageUri: Uri? = null;

    private val onTakePhoto = ActivityResultCallback<Boolean> { photography: Boolean ->
        if (photography) {
            binding.productPicture.setImageURI(imageUri)
            //todo: save image to database
        } else {
            imageUri?.let { requireContext().contentResolver.delete(it, null, null) }
        }
    }


    private fun createPicture() {
        val imagesUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "images/jpeg")
        }
        imageUri = requireContext().contentResolver.insert(imagesUri, ct)
        cameraLauncher.launch(imageUri)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = ProductDatabase.open(requireContext())
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture(),
            onTakePhoto
        )
        if (checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                if (it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    locationOn()
                }
            }.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            locationOn()
        }


    }

    @SuppressLint("MissingPermission")
    private fun locationOn() {
        binding.map.apply {
            overlays.add(
                MyLocationNewOverlay(this).apply {
                    enableMyLocation()
                }
            )
            val locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                // Use the retrieved location for your desired purpose
                // For example, you can access the latitude and longitude:
                val latitude = location.latitude
                val longitude = location.longitude
                // Do something with the latitude and longitude values
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditBinding.bind(view)

        adapter = ProductImageAdapter()
        if (arguments != null && requireArguments().containsKey(ARG_EDIT_IT)) {
            val id = requireArguments().getInt(ARG_EDIT_IT, -1)

            if (id != -1) {
                thread {
                    product = db.products.getMovie(id)
                    requireActivity().runOnUiThread {
                        binding.name.setText(product?.name ?: "")
                        binding.description.setText(product?.description ?: "")
                        binding.price.setText(product?.price.toString())

                        adapter.setSelection(product?.image?.let {
                            resources.getIdentifier(
                                it,
                                "drawable",
                                requireContext().packageName
                            )
                        })
                    }
                }
            }
        }

        // Call locationOn() after all initializations
        locationOn(binding.map)

        Configuration.getInstance().userAgentValue = activity?.packageName
        binding.map.apply {
            setTileSource(TileSourceFactory.MAPNIK)
        }

        binding.btnSave.setOnClickListener {
            val name = binding.name.text.toString()
            val description = binding.description.text.toString()
            val price = binding.price.text.toString().toDoubleOrNull() ?: 0.0
            val image = imageUri.toString() // Save the image URI as a string

            val product = product?.copy(
                name = name,
                description = description,
                image = image,
                price = price
            ) ?: ProductEntity(
                name = name,
                description = description,
                image = image,
                price = price
            )
            this.product = product
            if (name.isNotEmpty() && description.isNotEmpty()) {
                thread {
                    db.products.addMovie(product)
                    (activity as? Navigable)?.navigate(Navigable.Destination.List)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnCamera.setOnClickListener {
            createPicture()
        }
    }

    private fun locationOn(map: MapView) {
        map.apply {
            overlays.add(
                MyLocationNewOverlay(this).apply {
                    enableMyLocation()
                }
            )
        }
    }


    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
}