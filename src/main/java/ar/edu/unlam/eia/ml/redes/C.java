package ar.edu.unlam.eia.ml.redes;

import org.deeplearning4j.datasets.iterator.BaseDatasetIterator;

import java.io.IOException;

public class C extends BaseDatasetIterator {

    public C(int batch, int numExamples) throws IOException {
        this(batch, numExamples, false);
    }

    public C(int batch, int numExamples, boolean binarize) throws IOException {
        this(batch, numExamples, binarize, true, false, 0L);
    }

    public C(int batchSize, boolean train, int seed) throws IOException {
        this(batchSize, train ? '\uea60' : 10000, false, train, true, (long) seed);
    }

    public C(int batch, int numExamples, boolean binarize, boolean train, boolean shuffle, long rngSeed) throws IOException {
        super(batch, numExamples, new F(binarize, train, shuffle, rngSeed));
    }

}
