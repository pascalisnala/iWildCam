package bangkit.project.wildcam.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import bangkit.project.wildcam.R
import bangkit.project.wildcam.ml.ImageClassifier
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_classify_image.*
import java.util.*


class ClassifyImageFragment : Fragment() {
    private var imageUri: Uri? = null
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private lateinit var classifier:ImageClassifier

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_classify_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classifier = ImageClassifier(requireContext())
        classifier.initClassifier()

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetResult)
        mBottomSheetBehavior.isHideable=false

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        view.findViewById<Toolbar>(R.id.tlbClassifyImage)
            .setupWithNavController(navController, appBarConfiguration)

        if (imageUri == null) {
            imgUploaded.visibility = View.GONE
            tvFirstResult.visibility = View.GONE
            tvSecondResult.visibility = View.GONE
            tvThirdResult.visibility = View.GONE
            tvFirstResultProb.visibility = View.GONE
            tvSecondResultProb.visibility = View.GONE
            tvThirdResultProb.visibility = View.GONE
        } else {
            imgEmpty.visibility = View.GONE
            tvEmpty.visibility = View.GONE
        }
        btnUploadImage.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2000)
            } else {
                openGallery()
            }
        }

        mBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState==BottomSheetBehavior.STATE_COLLAPSED){
                    viewClassifyLoading.visibility = View.GONE
                }else{
                    viewClassifyLoading.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun openGallery() {
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraIntent.type = "image/*"
        startActivityForResult(cameraIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                imgEmpty.visibility = View.GONE
                tvEmpty.visibility = View.GONE
                imgUploaded.visibility = View.VISIBLE
                tvFirstResult.visibility = View.VISIBLE
                tvSecondResult.visibility = View.VISIBLE
                tvThirdResult.visibility = View.VISIBLE
                tvFirstResultProb.visibility = View.VISIBLE
                tvSecondResultProb.visibility = View.VISIBLE
                tvThirdResultProb.visibility = View.VISIBLE
                imageUri = data!!.data
                if (Build.VERSION.SDK_INT < 28) {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        imageUri
                    )
                    imgUploaded.setImageBitmap(bitmap)
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    viewClassifyLoading.visibility = View.VISIBLE
                } else {
                    val source =
                        ImageDecoder.createSource(requireContext().contentResolver, imageUri!!)
                    var bitmap = ImageDecoder.decodeBitmap(source)
                    imgUploaded.setImageBitmap(bitmap)
                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true)
                    val output = classifier.classify(bitmap)

                    val sortedResult = output.toList().sortedByDescending { (key, value) -> value }

                    val first = sortedResult.slice(0..0).toMap()
                    val second = sortedResult.slice(1..1).toMap()
                    val third = sortedResult.slice(2..2).toMap()

                    first.forEach { (key, value) ->
                        tvFirstResult.text = key
                        tvFirstResultProb.text =  "${String.format("%.2f", value*100)}%"
                    }
                    second.forEach { (key, value) ->
                        tvSecondResult.text = key
                        tvSecondResultProb.text =  "${String.format("%.2f", value*100)}%"
                    }
                    third.forEach { (key, value) ->
                        tvThirdResult.text = key
                        tvThirdResultProb.text = "${String.format("%.2f", value*100)}%"
                    }


                    val top3result = sortedResult.slice(0..2).toMap()


                    Log.d("run_classifier", top3result.toString())

                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    viewClassifyLoading.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2000)
        } else {
            openGallery()
        }
    }
}
