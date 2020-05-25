//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package ar.edu.unlam.eia.ml.redes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher;
import org.deeplearning4j.datasets.mnist.MnistManager;
import org.deeplearning4j.util.MathUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

public class F extends BaseDataFetcher {
    public static final int NUM_EXAMPLES = 60000;
    public static final int NUM_EXAMPLES_TEST = 10000;
    protected static final String TEMP_ROOT = System.getProperty("user.home");
    protected static final String MNIST_ROOT;
    protected transient MnistManager man;
    protected boolean binarize;
    protected boolean train;
    protected int[] order;
    protected Random rng;
    protected boolean shuffle;
    protected boolean oneIndexed;
    protected boolean fOrder;

    public F(boolean binarize) throws IOException {
        this(binarize, true, true, System.currentTimeMillis());
    }

    public F(boolean binarize, boolean train, boolean shuffle, long rngSeed) throws IOException {
        this.binarize = true;
        this.oneIndexed = false;
        this.fOrder = false;
        if (!this.mnistExists()) {
            (new FF()).downloadAndUntar();
        }

        String images;
        String labels;
        if (train) {
            images = MNIST_ROOT + "train-images-idx3-ubyte";
            labels = MNIST_ROOT + "train-labels-idx1-ubyte";
            this.totalExamples = 60000;
        } else {
            images = MNIST_ROOT + "t10k-images-idx3-ubyte";
            labels = MNIST_ROOT + "t10k-labels-idx1-ubyte";
            this.totalExamples = 10000;
        }

        try {
            this.man = new MnistManager(images, labels, train);
        } catch (Exception var9) {
            FileUtils.deleteDirectory(new File(MNIST_ROOT));
            (new FF()).downloadAndUntar();
            this.man = new MnistManager(images, labels, train);
        }

        this.numOutcomes = 10;
        this.binarize = binarize;
        this.cursor = 0;
        this.inputColumns = this.man.getImages().getEntryLength();
        this.train = train;
        this.shuffle = shuffle;
        if (train) {
            this.order = new int['\uea60'];
        } else {
            this.order = new int[10000];
        }

        for (int i = 0; i < this.order.length; this.order[i] = i++) {
        }

        this.rng = new Random(rngSeed);
        this.reset();
    }

    private boolean mnistExists() {
        File f = new File(MNIST_ROOT, "train-images-idx3-ubyte");
        if (!f.exists()) {
            return false;
        } else {
            f = new File(MNIST_ROOT, "train-labels-idx1-ubyte");
            if (!f.exists()) {
                return false;
            } else {
                f = new File(MNIST_ROOT, "t10k-images-idx3-ubyte");
                if (!f.exists()) {
                    return false;
                } else {
                    f = new File(MNIST_ROOT, "t10k-labels-idx1-ubyte");
                    return f.exists();
                }
            }
        }
    }

    public F() throws IOException {
        this(true);
    }

    public void fetch(int numExamples) {
        if (!this.hasMore()) {
            throw new IllegalStateException("Unable to getFromOrigin more; there are no more images");
        } else {
            float[][] featureData = new float[numExamples][0];
            float[][] labelData = new float[numExamples][0];
            int actualExamples = 0;
            byte[] working = null;

            for (int i = 0; i < numExamples && this.hasMore(); ++this.cursor) {
                byte[] img = this.man.readImageUnsafe(this.order[this.cursor]);
                int j;
                if (this.fOrder) {
                    if (working == null) {
                        working = new byte[784];
                    }

                    for (j = 0; j < 784; ++j) {
                        working[j] = img[28 * (j % 28) + j / 28];
                    }

                    byte[] temp = img;
                    img = working;
                    working = temp;
                }

                j = this.man.readLabel(this.order[this.cursor]);
                if (this.oneIndexed) {
                    --j;
                }

                float[] featureVec = new float[img.length];
                featureData[actualExamples] = featureVec;
                labelData[actualExamples] = new float[this.numOutcomes];
                labelData[actualExamples][j] = 1.0F;

                for (int i1 = 0; i1 < img.length; ++i1) {
                    float v = (float) (img[i1] & 255);
                    if (this.binarize) {
                        if (v > 30.0F) {
                            featureVec[i1] = 1.0F;
                        } else {
                            featureVec[i1] = 0.0F;
                        }
                    } else {
                        featureVec[i1] = v / 255.0F;
                    }
                }

                ++actualExamples;
                ++i;
            }

            if (actualExamples < numExamples) {
                featureData = (float[][]) Arrays.copyOfRange(featureData, 0, actualExamples);
                labelData = (float[][]) Arrays.copyOfRange(labelData, 0, actualExamples);
            }

            INDArray features = Nd4j.create(featureData);
            INDArray labels = Nd4j.create(labelData);
            this.curr = new DataSet(features, labels);
        }
    }

    public void reset() {
        this.cursor = 0;
        this.curr = null;
        if (this.shuffle) {
            MathUtils.shuffleArray(this.order, this.rng);
        }

    }

    public DataSet next() {
        DataSet next = super.next();
        return next;
    }

    static {
        MNIST_ROOT = TEMP_ROOT + File.separator + "MNIST" + File.separator;
    }
}
