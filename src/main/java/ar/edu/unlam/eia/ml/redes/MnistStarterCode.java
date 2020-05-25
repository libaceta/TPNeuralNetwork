package ar.edu.unlam.eia.ml.redes;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.GregorianCalendar;


/**
 * Basado en ejemplo de dl4j
 * <p>
 * Tutorial en: http://deeplearning4j.org/mnist-for-beginners
 */
public class MnistStarterCode {

    private static Logger log = LoggerFactory.getLogger(MnistStarterCode.class);

    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    public static void main(String[] args) throws Exception {


        long startTime = GregorianCalendar.getInstance().getTimeInMillis();

        // Filas y columnas de las imagenes de entrada (pixels)
        final int imageHeight = 28;
        final int imageWidth = 28;

        // Numero de neuronas de salida
        int outputNum = 10;

        // Tamaño del batch para entrenamiento
        int batchSize = 256;
        int rngSeed = 777;
        int numEpochs = 15;

        //Get the DataSetIterators:
        DataSetIterator mnistTrain = null;
        DataSetIterator mnistTest = null;
        try {
            mnistTrain = new C(batchSize, true, rngSeed);
            mnistTest = new C(batchSize, false, rngSeed);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Build model....");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(rngSeed)
                // Algoritmo de optimizacion/aprendizaje
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                // Cantidad de pasadas por la data
                .iterations(1)
                // Factor de aprendizaje
                .learningRate(0.006)
                .updater(Updater.NESTEROVS).momentum(0.9)
                .regularization(true).l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(imageHeight * imageWidth)
                        .nOut(20)
                        .activation("relu") // "identity" , "tanh"
                        .weightInit(WeightInit.XAVIER)
                        .build())
                //////////////////////////
                .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(20)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .pretrain(false)
                .backprop(true)
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(100));

        log.info("Train model....");
        for (int i = 0; i < numEpochs; i++) {
            model.fit(mnistTrain);
        }


        log.info("Evaluate model....");
        Evaluation eval = new Evaluation(outputNum);
        while (mnistTest.hasNext()) {
            DataSet next = mnistTest.next();
            INDArray output = model.output(next.getFeatureMatrix());
            eval.eval(next.getLabels(), output);
        }

        log.info(eval.stats());
        log.info(eval.confusionToString());
        log.info(Long.toString(GregorianCalendar.getInstance().getTimeInMillis() - startTime));
        log.info("****************Test finished********************");
    }

}
