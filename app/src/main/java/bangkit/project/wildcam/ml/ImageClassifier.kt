package bangkit.project.wildcam.ml

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer


class ImageClassifier(private val context: Context) {
    private lateinit var interpreter: Interpreter
    private lateinit var image: TensorImage
    private lateinit var output: TensorBuffer
    private lateinit var labels: List<String>

    fun initClassifier() {
        val model: ByteBuffer = FileUtil.loadMappedFile(context, "wild_cam_classifier.tflite")
        labels = FileUtil.loadLabels( context , "label.txt" )
        image = TensorImage( DataType.UINT8 )
        output = TensorBuffer.createFixedSize(intArrayOf(1, 14), DataType.FLOAT32)
        interpreter = Interpreter(model)
    }

    fun classify(bitmap: Bitmap): MutableMap<String, Float> {
        val imageProcessor: ImageProcessor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(128, 128))
            .add( NormalizeOp( 0f, 255f ))
            .build()

        image.load( bitmap )
        imageProcessor.process(image)

        interpreter.run(image.buffer, output.buffer)

        return TensorLabel(labels, output).mapWithFloatValue
    }

    fun nukeClassifier(){
        interpreter.close();
    }

}