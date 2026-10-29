package com.plantwise.wateradvisor;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Plant Water Advisor.
 *
 * The user types a soil-dryness score from 0 to 10. A TensorFlow Lite model
 * in assets/water.tflite runs on the phone and returns how many cups of water
 * to give the plant. The model is one dense layer:
 *
 *     cups = (2.0 * dryness) + 0.0
 *
 * Nothing is sent to a server. The same load / input / run / output pattern
 * is used by the reference project NumberGuessingAppTflite.
 */
public class MainActivity extends AppCompatActivity {

    /** File name inside app/src/main/assets. */
    private static final String MODEL_FILE = "water.tflite";

    private EditText drynessInput;
    private TextView resultView;

    /**
     * The TFLite Interpreter holds the loaded model and runs one prediction
     * at a time. It is created in onCreate and closed in onDestroy.
     */
    private Interpreter interpreter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drynessInput = findViewById(R.id.drynessInput);
        resultView = findViewById(R.id.resultView);
        Button predictButton = findViewById(R.id.predictButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left + 24, bars.top + 24, bars.right + 24, bars.bottom + 24);
            return insets;
        });

        try {
            // Map the .tflite file into memory, then build the interpreter.
            interpreter = new Interpreter(loadModelFile());
        } catch (IOException exception) {
            resultView.setText(R.string.model_missing);
            predictButton.setEnabled(false);
            return;
        }

        predictButton.setOnClickListener(view -> predictWater());
    }

    /**
     * Reads the number, checks it, runs the model, and shows the cups.
     */
    private void predictWater() {
        String raw = drynessInput.getText().toString().trim();
        if (raw.isEmpty()) {
            resultView.setText(R.string.error_empty);
            return;
        }

        float dryness;
        try {
            dryness = Float.parseFloat(raw);
        } catch (NumberFormatException exception) {
            resultView.setText(R.string.error_number);
            return;
        }

        if (dryness < 0f || dryness > 10f) {
            resultView.setText(R.string.error_range);
            return;
        }

        // Input tensor shape [1]: one float feature (dryness).
        float[] input = new float[]{dryness};

        // Output tensor shape [1][1]: one float prediction (cups of water).
        float[][] output = new float[1][1];

        // Copies input into the model, runs the dense layer, writes output.
        interpreter.run(input, output);

        float cups = output[0][0];
        resultView.setText(getString(R.string.result_cups, cups));
    }

    /**
     * Opens assets/water.tflite and returns a read-only mapped buffer.
     * AssetFileDescriptor is required because Interpreter needs a MappedByteBuffer,
     * not a normal InputStream. The asset must be stored uncompressed.
     */
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor descriptor = getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(descriptor.getFileDescriptor());
        FileChannel channel = inputStream.getChannel();
        long start = descriptor.getStartOffset();
        long length = descriptor.getDeclaredLength();
        return channel.map(FileChannel.MapMode.READ_ONLY, start, length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interpreter != null) {
            interpreter.close();
            interpreter = null;
        }
    }
}
